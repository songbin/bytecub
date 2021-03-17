package com.bytecub.protocol.plugin.hw;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bytecub.common.annotations.BcProtocolAnnotation;
import com.bytecub.common.biz.TopicBiz;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.message.DeviceDownMessage;
import com.bytecub.common.domain.message.DeviceReportMessage;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.enums.DeviceReplyEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.protocol.base.IBaseProtocol;
import com.bytecub.protocol.domain.demo.ReplyResultBo;
import com.bytecub.utils.JSONProvider;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: DemoProtocolService.java, v 0.1 2020-12-07  Exp $$  
 */
@Slf4j
@Data
@BcProtocolAnnotation(name = "标准协议", protocolCode = "standard", desc = "标准协议")
@Component
public class StandardProtocolService implements IBaseProtocol {

    @Override
    public DeviceReportMessage decode(String topic, String deviceId, byte[] payload) {
        try {
            DeviceReportMessage deviceMessage = new DeviceReportMessage<>();
            String topicDeviceCode = TopicBiz.parseDeviceCode(topic);
            if (!deviceId.equals(topicDeviceCode)) {
                log.warn("设备[{}]上报topic[{}]权限不足", deviceId, topic);
                String extraMsg = String.format("设备[%s]上报topic[%s]权限不足", deviceId, topic);
                throw BCGException.genException(BCErrorEnum.ES_CREATE_EXCEPTION, extraMsg);
            }

            String msg = new String(payload, BCConstants.GLOBAL.CHARSET_GB2312);
            log.info("[{}]准备解析协议【{}】:{}", topic, payload);
            this.parsePropMessage(topic, payload, deviceMessage);
            this.parseEventMessage(topic, payload, deviceMessage);
            this.parseReplyMessage(topic, payload, deviceMessage);
            /**设备属性读取回执*/
            this.parsePropertyGetMessage(topic, payload, deviceMessage);
            this.parseSubOffline(topic, payload, deviceMessage);
            this.parseSubOnline(topic, payload, deviceMessage);
            return deviceMessage;
        } catch (Exception e) {
            log.warn("解析协议异常:{}", payload, e);
        }
        return null;
    }

    @Override
    public byte[] encode(String topic, String deviceCode,  DeviceDownMessage deviceDownMessage) {
        try{
            String msg = JSONProvider.toJSONString(deviceDownMessage);
            return msg.getBytes(BCConstants.GLOBAL.CHARSET_GB2312);
        }catch (Exception e){
            log.warn("加密数据异常 topic:{} device:{} body:{}", topic, deviceCode, deviceDownMessage);
            return null;
        }
    }

    private void parsePropMessage(String topic, byte[] payload, DeviceReportMessage deviceMessage){
        try{
            ProductFuncTypeEnum funcTypeEnum = TopicBiz.parseFuncTypeEnum(topic);
            if(!funcTypeEnum.equals(ProductFuncTypeEnum.PROP)){
                return;
            }
            String msg = new String(payload, BCConstants.GLOBAL.CHARSET_GB2312);
            Map<String, Object> map = JSONProvider.parseObjectDefValue(msg, Map.class);
            deviceMessage.setValue(map);
        }catch (Exception e){
            log.warn("解析协议异常", e);
            String extraMsg = "解析主题:"+ topic +" 的上报消息异常";
            throw BCGException.genException(BCErrorEnum.PARSE_MSG_EXCEPTION, extraMsg);
        }
    }

    private void parseEventMessage(String topic, byte[] payload, DeviceReportMessage deviceMessage) {
        try {
            ProductFuncTypeEnum funcTypeEnum = TopicBiz.parseFuncTypeEnum(topic);
            if (!funcTypeEnum.equals(ProductFuncTypeEnum.EVENT)) {
                return;
            }
            String msg = new String(payload, BCConstants.GLOBAL.CHARSET_GB2312);
            Map<String, Object> map = JSONProvider.parseObjectDefValue(msg, Map.class);
            deviceMessage.setValue(map);
        } catch (Exception e) {
            log.warn("解析协议异常", e);
            String extraMsg = "解析主题:" + topic + " 的上报消息异常";
            throw BCGException.genException(BCErrorEnum.PARSE_MSG_EXCEPTION, extraMsg);
        }
    }
    private void parseReplyMessage(String topic, byte[] payload, DeviceReportMessage deviceMessage){
        try{
            ProductFuncTypeEnum funcTypeEnum = TopicBiz.parseFuncTypeEnum(topic);
            if(!topic.endsWith(BCConstants.TOPIC.MSG_REPLY)){
                return;
            }
            String msg = new String(payload, BCConstants.GLOBAL.CHARSET_GB2312);

            Map<String, Object> map = JSONProvider.parseObjectDefValue(msg, Map.class);
            deviceMessage.setMessageId((String)map.get("messageId"));
            deviceMessage.setReplyMessage(msg);
            ReplyResultBo replyResultBo = JSONProvider.parseJsonObject((JSONObject)map.get("result"), ReplyResultBo.class);
            if(200 == replyResultBo.getCode()){
                deviceMessage.setStatus(DeviceReplyEnum.SUCCESS);
            }else if(-1 == replyResultBo.getCode()){
                deviceMessage.setStatus(DeviceReplyEnum.UNKNOWN);
            }else{
                deviceMessage.setStatus(DeviceReplyEnum.FAIL);
            }
        }catch (Exception e){
            log.warn("解析协议异常", e);
            String extraMsg = "解析主题:"+ topic +" 的回执消息异常";
            throw BCGException.genException(BCErrorEnum.PARSE_MSG_EXCEPTION, extraMsg);
        }
    }

    /**
     * 读取设备属性回执上报
     * */
    private void parsePropertyGetMessage(String topic, byte[] payload, DeviceReportMessage deviceMessage){
        try{
            if(!topic.endsWith(BCConstants.TOPIC.PROP_GET_REPLY)){
                return;
            }
            String msg = new String(payload, BCConstants.GLOBAL.CHARSET_GB2312);
            Map<String, Object> map = JSONProvider.parseObjectDefValue(msg, Map.class);
            deviceMessage.setMessageId((String)map.get("messageId"));
            deviceMessage.setValue((Map)map.get("value"));
        }catch (Exception e){
            log.warn("解析协议异常", e);
            String extraMsg = "解析主题:"+ topic +" 的回执消息异常";
            throw BCGException.genException(BCErrorEnum.PARSE_MSG_EXCEPTION, extraMsg);
        }
    }
    /**
     * 网关子设备上线
     * */
    private void parseSubOnline(String topic, byte[] payload, DeviceReportMessage deviceMessage){
        try{
            if(!topic.endsWith("online")){
                return;
            }
            String msg = new String(payload, BCConstants.GLOBAL.CHARSET_GB2312);
            List<String> devices = JSONProvider.parseArrayObject(msg, String.class);
            deviceMessage.setDevices(devices);
        }catch (Exception e){
            log.warn("解析协议异常", e);
            String extraMsg = "解析主题:"+ topic +" 子设备上线解析异常";
            throw BCGException.genException(BCErrorEnum.PARSE_MSG_EXCEPTION, extraMsg);
        }
    }
    private void parseSubOffline(String topic, byte[] payload, DeviceReportMessage deviceMessage){
        try{
            if(!topic.endsWith("online")){
                return;
            }
            String msg = new String(payload, BCConstants.GLOBAL.CHARSET_GB2312);
            List<String> devices = JSONProvider.parseArrayObject(msg, String.class);
            deviceMessage.setDevices(devices);
        }catch (Exception e){
            log.warn("解析协议异常", e);
            String extraMsg = "解析主题:"+ topic +" 子设备下线解析异常";
            throw BCGException.genException(BCErrorEnum.PARSE_MSG_EXCEPTION, extraMsg);
        }
    }
}
