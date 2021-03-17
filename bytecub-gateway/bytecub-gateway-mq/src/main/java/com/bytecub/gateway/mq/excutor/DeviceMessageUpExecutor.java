package com.bytecub.gateway.mq.excutor;

import com.bytecub.common.biz.EsUtil;
import com.bytecub.common.biz.RedisKeyUtil;
import com.bytecub.common.biz.TopicBiz;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.common.domain.message.DeviceReportMessage;
import com.bytecub.common.domain.storage.EsMessage;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.enums.BatchOpEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.metadata.BcMetaType;
import com.bytecub.common.metadata.EsInsertDataBo;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.common.domain.gateway.mq.DeviceUpMessageBo;
import com.bytecub.mdm.cache.IMessageCountCache;
import com.bytecub.mdm.cache.IPropertyGetCache;
import com.bytecub.mdm.dao.po.ProductPo;
import com.bytecub.mdm.service.IDeviceService;
import com.bytecub.mdm.service.IProductFuncService;
import com.bytecub.mdm.service.IProductService;
import com.bytecub.plugin.redis.CacheTemplate;
import com.bytecub.protocol.base.IBaseProtocol;
import com.bytecub.protocol.service.IProtocolUtilService;
import com.bytecub.storage.IDataCenterService;
import com.bytecub.storage.IMessageReplyService;
import com.bytecub.storage.entity.MessageReplyEntity;
import com.bytecub.utils.DateUtil;
import com.bytecub.utils.JSONProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: DeviceMessageUpExcutor.java, v 0.1 2021-02-25  Exp $$  
 */
@Service
@Slf4j
public class DeviceMessageUpExecutor {
    @Autowired
    IDataCenterService dataCenterService;
    @Autowired
    IProductFuncService productFuncService;
    @Autowired
    CacheTemplate cacheTemplate;
    @Autowired
    IProductService productService;
    @Autowired
    IProtocolUtilService protocolUtilService;
    @Autowired
    IMessageReplyService messageReplyService;
    @Autowired
    IPropertyGetCache propertyGetCache;
    @Autowired
    IMessageCountCache messageCountCache;
    @Autowired
    IDeviceService deviceService;

    /***
     * 具体线程
     */
    @Async(BCConstants.TASK.DEVICE_UP_MESSAGE_NAME)
    public void execute(DeviceUpMessageBo msg) {
        try {
            messageCountCache.todayTotalIncr();
            this.rebuildMsg(msg);

            IBaseProtocol protocolService = this.queryProtocolByProductCode(msg.getProductCode());
            /** 来自设备转化为平台需要的数据结构 */
            DeviceReportMessage deviceMessage = protocolService.decode(msg.getTopic(), msg.getDeviceCode(), msg.getSourceMsg());
            deviceMessage.setProductCode(msg.getProductCode());
            deviceMessage.setDeviceCode(msg.getDeviceCode());
            this.processUpMessage(msg, deviceMessage);
            this.processReplyMessage(msg, deviceMessage);
            this.processPropertyGetReplyMessage(msg, deviceMessage);
            this.subDeviceActive(msg, deviceMessage);
        } catch (Exception e) {
            log.warn("内部队列消费异常", e);

        }

    }

    /**
     * 网关批量上报子设备状态
     */
    private final void subDeviceActive(DeviceUpMessageBo msg, DeviceReportMessage deviceMessage) {
        String topic = msg.getTopic();
        if (topic.endsWith("offline")) {
            deviceService.batchChangeStatusByCode(deviceMessage.getDevices(), BatchOpEnum.OFFLINE);
        } else if (topic.endsWith("online")) {
            deviceService.batchChangeStatusByCode(deviceMessage.getDevices(), BatchOpEnum.ONLINE);
        }
    }

    /** 服务调用消息回执 */
    private final void processReplyMessage(DeviceUpMessageBo msg, DeviceReportMessage deviceMessage) {
        String topic = msg.getTopic();
        if (!topic.endsWith(BCConstants.TOPIC.MSG_REPLY)) {
            return;
        }
        MessageReplyEntity messageReplyEntity = new MessageReplyEntity();
        messageReplyEntity.setMessageId(deviceMessage.getMessageId());
        messageReplyEntity.setBody(deviceMessage.getReplyMessage());
        messageReplyEntity.setDeviceCode(deviceMessage.getDeviceCode());
        messageReplyEntity.setDevTimestamp(deviceMessage.getDeviceTimestamp());
        messageReplyEntity.setProductCode(deviceMessage.getProductCode());
        messageReplyEntity.setTimestamp(msg.getCurrTime());
        messageReplyEntity.setStatus(deviceMessage.getStatus().getCode());
        messageReplyService.create(messageReplyEntity);
    }

    /** 处理设备主动上报的消息 */
    private final void processUpMessage(DeviceUpMessageBo msg, DeviceReportMessage deviceMessage) {
        String topic = msg.getTopic();
        if (!topic.endsWith("prop") && !topic.endsWith("event")) {
            return;
        }
        List<EsInsertDataBo> dataList = this.parseMsg(msg, deviceMessage);
        for (EsInsertDataBo item : dataList) {
            try {
                dataCenterService.saveData(item);
                this.setRtCache(msg.getFuncType(), msg.getDeviceCode(), msg.getCurrTime(), item);
            } catch (Exception e) {
                log.warn("", e);
            }
        }
    }

    /** 处理设备对属性读取的响应 */
    private final void processPropertyGetReplyMessage(DeviceUpMessageBo msg, DeviceReportMessage deviceMessage) {
        String topic = msg.getTopic();
        if (!topic.endsWith(BCConstants.TOPIC.PROP_GET_REPLY)) {
            return;
        }
        if (StringUtils.isEmpty(deviceMessage.getMessageId())) {
            log.warn("属性读取响应回执缺失messageId");
            return;
        }
        List<EsInsertDataBo> dataList = this.parseMsg(msg, deviceMessage);
        for (EsInsertDataBo item : dataList) {
            try {
                dataCenterService.saveData(item);
                this.setRtCache(msg.getFuncType(), msg.getDeviceCode(), msg.getCurrTime(), item);
            } catch (Exception e) {
                log.warn("", e);
            }
        }
        propertyGetCache.propGetValueWrite(deviceMessage.getMessageId(), deviceMessage.getValue());

    }

    private void rebuildMsg(DeviceUpMessageBo msg) {
        String deviceCode = TopicBiz.parseDeviceCode(msg.getTopic());
        DevicePageResDto devicePageResDto = deviceService.queryByDevCode(deviceCode);
        String funcType = TopicBiz.parseFuncType(msg.getTopic());
        ProductFuncTypeEnum funcTypeEnum = ProductFuncTypeEnum.explain(funcType);
        msg.setProductCode(devicePageResDto.getProductCode());
        msg.setDeviceCode(deviceCode);
        msg.setFuncType(funcTypeEnum);
        return;
    }

    /** 根据产品编码查询协议实现 */
    public IBaseProtocol queryProtocolByProductCode(String productCode) {
        try {
            ProductPo productPo = productService.queryByCode(productCode);
            return protocolUtilService.queryProtocolInstanceByCode(productPo.getProtocolCode());
        } catch (Exception e) {
            log.warn("", e);
            throw BCGException.genException(BCErrorEnum.INNER_EXCEPTION);
        }

    }

    /** 把实时状态存入缓存 */
    private Boolean setRtCache(ProductFuncTypeEnum funcType, String deviceCode, Date arriveTime,
        EsInsertDataBo dataBo) {
        String redisKey = RedisKeyUtil.buildRtCacheKey(deviceCode, funcType);
        cacheTemplate.addHashMap(redisKey, dataBo.getIdentifier(), JSONProvider.toJSONString(dataBo.getEsMessage()));
        return true;
    }

    /**
     * 将协议解析后得到的json数据按照产品属性进行重新组装
     */
    private List<EsInsertDataBo> parseMsg(DeviceUpMessageBo reqMsg, DeviceReportMessage deviceMessage) {

        List<EsInsertDataBo> dataList = new ArrayList<>();

        try {
            List<ProductFuncItemResDto> funcList =
                productFuncService.ListFuncByProductCode(reqMsg.getProductCode(), 1, reqMsg.getFuncType());
            /** 属性和类型对应的map映射关系，key是属性 value是类型 */
            Map<String, String> propsMap = new HashMap<>();
            for (ProductFuncItemResDto item : funcList) {
                propsMap.put(item.getIdentifier(), item.getDataType());
            }
            /** 设备传出过来的原始数据 */
            Map<String, Object> dataMap = deviceMessage.getValue();
            dataMap.forEach((key, value) -> {
                EsInsertDataBo esInsertDataBo = new EsInsertDataBo();
                EsMessage esMessage = new EsMessage();
                esMessage.setDeviceCode(deviceMessage.getDeviceCode());
                esMessage.setMessageId(deviceMessage.getMessageId());
                esMessage.setProductCode(deviceMessage.getProductCode());
                esMessage.setTimestamp(reqMsg.getCurrTime());
                esMessage.setDeviceTimestamp(deviceMessage.getDeviceTimestamp());

                String dataType = (String)propsMap.get(key);
                Object retrieveVal = value;
                if (propsMap.containsKey(key)) {
                    if (BcMetaType.DATE.getCode().equals(dataType)) {
                        Date date = new Date((Long)value);
                        retrieveVal = DateUtil.formatForEsGMT8(date);
                    }
                    esMessage.setRequest(retrieveVal);
                    String index = EsUtil.buildDevIndex(reqMsg.getFuncType(), deviceMessage.getProductCode(), key);
                    esInsertDataBo.setIndexName(index);
                    esInsertDataBo.setEsMessage(esMessage);
                    esInsertDataBo.setIdentifier(key);
                    dataList.add(esInsertDataBo);
                }
            });

        } catch (Exception e) {
            log.warn("", e);
            return dataList;
        }
        return dataList;
    }
}
