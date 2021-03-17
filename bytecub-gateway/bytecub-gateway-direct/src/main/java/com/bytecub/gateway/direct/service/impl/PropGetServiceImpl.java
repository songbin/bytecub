package com.bytecub.gateway.direct.service.impl;

import java.util.Map;

import com.bytecub.common.domain.gateway.mq.PropertyReaderMessageBo;
import com.bytecub.gateway.mq.redis.publish.PropertyReaderPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.common.domain.message.DeviceDownMessage;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.metadata.BcMetaType;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.common.domain.gateway.direct.response.PropertyGetResponse;
import com.bytecub.gateway.direct.service.IPropGetService;
import com.bytecub.gateway.mq.mqttclient.BcPubMqttClient;
import com.bytecub.mdm.cache.IPropertyGetCache;
import com.bytecub.mdm.service.IDeviceService;
import com.bytecub.mdm.service.IProductFuncService;
import com.bytecub.mdm.service.IProductService;
import com.bytecub.protocol.service.IProtocolUtilService;
import com.bytecub.utils.IdGenerate;
import com.bytecub.utils.JSONProvider;

import lombok.extern.slf4j.Slf4j;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: PropGetServiceImpl.java, v 0.1 2021-01-26  Exp $$  
 */
@Slf4j
@Service
public class PropGetServiceImpl implements IPropGetService {
    @Autowired
    IDeviceService deviceService;
    @Autowired
    IProductService productService;
    @Autowired
    IProtocolUtilService protocolUtilService;
    @Autowired
    BcPubMqttClient bcPubMqttClient;
    @Autowired
    IPropertyGetCache propGetReader;
    @Autowired IProductFuncService productFuncService;

    @Override
    public PropertyGetResponse fetchProperty(String deviceCode, String identifier, Long timeout) {
        DevicePageResDto devicePageResDto = deviceService.queryByDevCode(deviceCode);
        if(null == devicePageResDto){
            log.warn("读取设备时发现设备不存在,设备编码=>{}", deviceCode);
            throw BCGException.genException(BCErrorEnum.RESOURCE_NOT_EXISTS);
        }
        DeviceDownMessage deviceDownMessage = this.buildDownMessage(devicePageResDto);
        deviceDownMessage.setIdentifier(identifier);
        log.info("属性读取messageId:{}", deviceDownMessage.getMessageId());
        //this.sendMsg(devicePageResDto, deviceDownMessage);
        PropertyReaderMessageBo readerMessageBo = new PropertyReaderMessageBo();
        readerMessageBo.setDeviceDownMessage(deviceDownMessage);
        readerMessageBo.setDevicePageResDto(devicePageResDto);
        PropertyReaderPublisher.send(readerMessageBo);
        return this.loopProperty(deviceDownMessage, timeout, identifier);
    }

    private PropertyGetResponse loopProperty(DeviceDownMessage deviceDownMessage, Long timeout, String identifier) {
        PropertyGetResponse response = new PropertyGetResponse();
        /**属性读取回执在redis里面存的value是一个map*/
        Map<String, Object> cacheValue = null;
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < timeout * 1000) {
            try{
                cacheValue = propGetReader.propGetReader(deviceDownMessage.getMessageId());
                if (null != cacheValue) {
                    break;
                }
                Thread.sleep(1000);
            }catch (Exception e){
                log.warn("属性回执读取异常",e);
            }

        }
        response.setDeviceCode(deviceDownMessage.getDeviceCode());
        response.setIdentifier(identifier);
        if(null == cacheValue){
            response.setValue(null);
        }else{
            response.setValue(cacheValue.get(identifier));
        }
        return response;
    }



    private DeviceDownMessage buildDownMessage(DevicePageResDto devicePageResDto) {
        DeviceDownMessage deviceDownMessage = new DeviceDownMessage();
        deviceDownMessage.setProductCode(devicePageResDto.getProductCode());
        deviceDownMessage.setDeviceCode(devicePageResDto.getDeviceCode());
        //deviceDownMessage.setIdentifier(devicePageResDto.get);
        deviceDownMessage.setTimestamp(System.currentTimeMillis());
        deviceDownMessage.setMessageId(IdGenerate.genId());
        return deviceDownMessage;
    }

    /**
     * 把从redis取出来的字符串值根据物模型类型都转化为对应的数据类型
     * */
    private Object parseValueFromString(String value, String productCode, String identifier) {
        Object result = value;
        ProductFuncItemResDto resDto = this.productFuncService.queryFunc(productCode, ProductFuncTypeEnum.PROP, identifier);
        if(null == resDto){
            log.warn("标识符资源不存在:identifier=>{} productCode=>{}", identifier, productCode);
            throw BCGException.genException(BCErrorEnum.INVALID_PARAM, "标识符资源不存在");
        }
        String dataType = resDto.getDataType();
        BcMetaType bcMetaType = BcMetaType.explain(dataType);
        if(null == bcMetaType){
            log.warn("标识符的数据类型异常:identifier=>{} productCode=>{}", identifier, productCode);
            throw BCGException.genException(BCErrorEnum.INVALID_PARAM, "标识符的数据类型异常");
        }
        switch (bcMetaType){
            case DATE:
            case STRING:
                result = value;
                break;
            case FLOAT:
            case DOUBLE:
                result = Double.valueOf(value);
                break;
            case LONG:
                result = Long.valueOf(value);
                break;
            case INTEGER:
                result = Integer.valueOf(value);
                break;
            case BOOLEAN:
                result = Boolean.valueOf(value);
                break;
            case STRUCT:
                result = JSONProvider.fromString(value);
                break;

        }
        return result;
    }

}
