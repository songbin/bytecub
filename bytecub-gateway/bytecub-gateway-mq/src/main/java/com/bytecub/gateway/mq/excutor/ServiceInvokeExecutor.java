package com.bytecub.gateway.mq.excutor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bytecub.mdm.cache.IServiceInvokeLockCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.bytecub.common.biz.EsUtil;
import com.bytecub.common.biz.RedisKeyUtil;
import com.bytecub.common.biz.TopicBiz;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.bo.BaseAttrItemBo;
import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.domain.message.DeviceDownMessage;
import com.bytecub.common.domain.storage.EsMessage;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.metadata.BcMetaType;
import com.bytecub.common.metadata.EsInsertDataBo;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.common.domain.gateway.mq.ServiceInvokeMessageBo;
import com.bytecub.gateway.mq.mqttclient.BcPubMqttClient;
import com.bytecub.mdm.cache.IMessageCountCache;
import com.bytecub.mdm.dao.po.ProductPo;
import com.bytecub.mdm.service.IProductFuncService;
import com.bytecub.mdm.service.IProductService;
import com.bytecub.plugin.redis.CacheTemplate;
import com.bytecub.protocol.base.IBaseProtocol;
import com.bytecub.protocol.service.IProtocolUtilService;
import com.bytecub.storage.IDataCenterService;
import com.bytecub.utils.JSONProvider;

import lombok.extern.slf4j.Slf4j;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: MQTTConsumeMonitor.java, v 0.1 2020-12-09  Exp $$  
 */
@Service
@Slf4j
public class ServiceInvokeExecutor {

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
    BcPubMqttClient bcPubMqttClient;
    @Autowired
    IMessageCountCache messageCountCache;
    @Autowired
    IServiceInvokeLockCache serviceInvokeLockCache;

    /***
     * 具体线程
     */
    @Async(BCConstants.TASK.SERVICE_INVOKE_NAME)
    public void execute(ServiceInvokeMessageBo msg) {

        try {
            messageCountCache.todayTotalIncr();
            this.rebuildMsg(msg);
            IBaseProtocol protocolService =   protocolUtilService.queryProtocolInstanceByCode(msg.getProtocolCode());
            /** 来自设备转化为json对象的消息实体 */
            EsInsertDataBo dataBo = this.parseMsg(msg, msg.getCommand());
            byte[] pubMsg = protocolService.encode(msg.getTopic(), msg.getDeviceCode(), this.buildDownMessage(dataBo));
            this.saveData2Es(dataBo);
            bcPubMqttClient.publish(msg.getTopic(), pubMsg);
            this.setRtCache(msg.getFuncType(), msg.getDeviceCode(), msg.getCurrTime(), dataBo);
        } catch (Exception e) {
            log.warn("内部队列消费异常", e);
        }

    }
    private void saveData2Es(EsInsertDataBo dataBo){
        Boolean ret = serviceInvokeLockCache.lock(dataBo.getEsMessage().getMessageId());
        if(ret){
            dataCenterService.saveData(dataBo);
        }
    }
    /** 组装下发给设备的消息 */
    private final DeviceDownMessage buildDownMessage(EsInsertDataBo dataBo) {
        DeviceDownMessage deviceDownMessage = new DeviceDownMessage();
        deviceDownMessage.setMessageId(dataBo.getEsMessage().getMessageId());
        deviceDownMessage.setTimestamp(System.currentTimeMillis());
        deviceDownMessage.setBody(dataBo.getEsMessage().getRequest());
        deviceDownMessage.setIdentifier(dataBo.getIdentifier());
        deviceDownMessage.setDeviceCode(dataBo.getEsMessage().getDeviceCode());
        deviceDownMessage.setProductCode(dataBo.getEsMessage().getProductCode());
        return deviceDownMessage;
    }

    /** 把实时状态存入缓存 */
    private Boolean setRtCache(ProductFuncTypeEnum funcType, String deviceCode, Date arriveTime,
        EsInsertDataBo dataBo) {
        String redisKey = RedisKeyUtil.buildRtCacheKey(deviceCode, funcType);
        cacheTemplate.addHashMap(redisKey, dataBo.getIdentifier(), JSONProvider.toJSONString(dataBo.getEsMessage()));
        return true;
    }

    private void rebuildMsg(ServiceInvokeMessageBo msg) {
        String topic = TopicBiz.buildServiceInvoke(msg.getDeviceCode(), msg.getProductCode());
        ProductPo productPo = productService.queryByCode(msg.getProductCode());

        msg.setTopic(topic);
        msg.setFuncType(ProductFuncTypeEnum.SERVICE);
        msg.setProtocolCode(productPo.getProtocolCode());
        return;
    }

    /**
     * 将协议解析后得到的json数据按照产品属性进行重新组装
     */
    private EsInsertDataBo parseMsg(ServiceInvokeMessageBo reqMsg, JSONObject json) {
        Map<String, Object> resultMap = new HashMap<>();
        Object finalResult = null;
        EsInsertDataBo dataBo = new EsInsertDataBo();
        try {
            ProductFuncItemResDto funcItemResDto =
                productFuncService.queryFunc(reqMsg.getProductCode(), reqMsg.getFuncType(), reqMsg.getIdentifier());

            /** 下发给设备的数据 */
            BaseAttrItemBo inputItem =
                JSONProvider.parseObjectDefValue(funcItemResDto.getInputParam(), BaseAttrItemBo.class);
            inputItem.setIdentifier(funcItemResDto.getIdentifier());
            /** 不是结构的话，这里就是简单类型，无法转换为map */
            /** 数据库中存储的改字段下的标识符列表 */
            if (BcMetaType.STRUCT.getCode().equals(inputItem.getDataType())) {
                resultMap = this.parseStructMsg(reqMsg, inputItem, json);
                finalResult = resultMap;
            } else {
                // 参数是简单数据或者没有参数
                resultMap = this.parseSampleMsg(reqMsg, inputItem, json);
                finalResult = resultMap.get(reqMsg.getIdentifier());
            }
            String indexName =
                EsUtil.buildDevIndex(reqMsg.getFuncType(), reqMsg.getProductCode(), reqMsg.getIdentifier());
            dataBo.setIndexName(indexName);
            EsMessage esMessage = new EsMessage();
            esMessage.setRequest(finalResult);
            esMessage.setDeviceCode(reqMsg.getDeviceCode());
            esMessage.setProductCode(reqMsg.getProductCode());
            esMessage.setMessageId(reqMsg.getMessageId());
            esMessage.setTimestamp(reqMsg.getCurrTime());
            dataBo.setIdentifier(reqMsg.getIdentifier());
            dataBo.setEsMessage(esMessage);
        } catch (Exception e) {
            log.warn("异常", e);
            return dataBo;
        }
        return dataBo;
    }

    private final Map<String, Object> parseSampleMsg(ServiceInvokeMessageBo reqMsg, BaseAttrItemBo inputItem,
        JSONObject json) {
        Map<String, Object> resultMap = new HashMap<>();
        if (!inputItem.getIdentifier().equals(reqMsg.getIdentifier())) {
            throw BCGException.genException(BCErrorEnum.INVALID_PARAM, "指令非法");
        }
        Object value = json.getObject(reqMsg.getIdentifier(), Object.class);
        Object retrieveVal = value;
        if (StringUtils.isEmpty(inputItem.getDataType())) {
            /** 这种情况下是空指令,此情况下默认ES类型是String， 默认值是如下设置 */
            retrieveVal = "/";
        } else if (BcMetaType.DATE.getCode().equals(inputItem.getDataType())) {
            Date date = new Date((Long)value);
            retrieveVal = value;
        }
        resultMap.put(inputItem.getIdentifier(), retrieveVal);
        return resultMap;

    }

    /**
     * 解析内容非结构体的下发指令
     */
    private final Map<String, Object> parseStructMsg(ServiceInvokeMessageBo reqMsg, BaseAttrItemBo inputItem,
        JSONObject json) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> resultFinalMap = new HashMap<>();

        Map<String, BaseAttrItemBo> attrMap = new HashMap<>();
        for (BaseAttrItemBo bo : inputItem.getData()) {
            attrMap.put(bo.getIdentifier(), bo);
        }
        Map<String, Object> dataMap =
            (Map)JSONProvider.parseJsonObject(json.getJSONObject(reqMsg.getIdentifier()), Map.class);
        dataMap.forEach((key, value) -> {
            Object retrieveVal = value;
            if (attrMap.containsKey(key)) {
                BaseAttrItemBo attrItemBo = attrMap.get(key);
                if (BcMetaType.DATE.getCode().equals(attrItemBo.getDataType())) {
                    Date date = new Date((Long)value);
                    retrieveVal = (Long)value;
                }
                resultMap.put(key, retrieveVal);
            }
        });
        return resultMap;
    }
}
