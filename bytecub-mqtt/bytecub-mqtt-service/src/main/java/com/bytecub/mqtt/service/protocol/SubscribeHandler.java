package com.bytecub.mqtt.service.protocol;


import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.domain.bo.RetainMsgBo;
import com.bytecub.mqtt.service.network.SendManager;
import com.bytecub.mqtt.service.state.ClientManager;
import com.bytecub.mqtt.service.state.RetainMsgManager;
import com.bytecub.mqtt.service.state.SessionManger;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author songbin
 * @version Id: SubscribeHandler.java, v 0.1 2019/1/16   Exp $$
 */
@Component
public class SubscribeHandler {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeHandler.class);
    public void onSubscribe(ContextBo contextBo, MqttSubscribeMessage msg) {
        logger.debug("新的订阅:{}", msg);
        //List<String> topicList = msg.payload().topicSubscriptions().get(0).topicName();
        List<MqttTopicSubscription> topicList = msg.payload().topicSubscriptions();
        if(!validTopicFilter(topicList)){
            logger.warn("订阅的主题非法{}", topicList);
            //TODO 这个需要返回一个错误码
            return;
        }
        MqttSubAckPayload mqttSubAckPayload = new MqttSubAckPayload(MqttQoS.AT_MOST_ONCE.value());

       MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(
                MqttMessageType.SUBACK,
                false,
                MqttQoS.AT_MOST_ONCE,
                false,
                0
        );
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(msg.variableHeader().messageId());
        MqttSubAckMessage mqttSubAckMessage = (MqttSubAckMessage)MqttMessageFactory.newMessage(
                mqttFixedHeader, mqttMessageIdVariableHeader, mqttSubAckPayload);

        for(MqttTopicSubscription item:topicList){
            ClientManager.addClient(item.topicName(), contextBo);
        }
        ClientManager.updateClientOnLine(contextBo.getClientId());
        SendManager.responseMsg(contextBo, mqttSubAckMessage, null, true);
        for(MqttTopicSubscription item:topicList){
            this.sendRetain(item.topicName(),contextBo, item.qualityOfService());
        }

    }
    /**检查topic合法性*/
    private boolean validTopicFilter(List<MqttTopicSubscription> topicSubscriptions) {
        for (MqttTopicSubscription topicSubscription : topicSubscriptions) {
            String topicFilter = topicSubscription.topicName();
            if(StringUtils.isEmpty(topicFilter)){
                return false;
            }
            // 以#或+符号开头的、以/符号结尾的及不存在/符号的订阅按非法订阅处理, 这里没有参考标准协议
//            if (StringUtils.startsWithIgnoreCase(topicFilter, "#") || StringUtils.startsWithIgnoreCase(topicFilter, "+") || StringUtils.endsWithIgnoreCase(topicFilter, "/") || !topicFilter.contains("/")) return false;
//            if (topicFilter.contains( "#")) {
//                // 不是以/#字符串结尾的订阅按非法订阅处理
//                if (!StringUtils.endsWithIgnoreCase(topicFilter, "/#")) return false;
//                // 如果出现多个#符号的订阅按非法订阅处理
//                if (StringUtils.countOccurrencesOf(topicFilter, "#") > 1) return false;
//            }
//            if (topicFilter.contains( "+")) {
//                //如果+符号和/+字符串出现的次数不等的情况按非法订阅处理
//                if (StringUtils.countOccurrencesOf(topicFilter, "+") != StringUtils.countOccurrencesOf(topicFilter, "/+")) return false;
//            }
        }
        return true;
    }
    /**检查是否connect过*/
    private boolean checkSession(String clientId){
        ContextBo contextBo = SessionManger.getContextByClientId(clientId);
        if(null == contextBo){
            return false;
        }
        return true;
    }
    /**发送遗留消息*/
    private void sendRetain(String topic, ContextBo contextBo, MqttQoS mqttQoS){
        try{
            RetainMsgBo retainMsg = RetainMsgManager.latestRetain(topic);
            if(null == retainMsg){
                return;
            }
            MqttQoS respQoS = retainMsg.getMqttQoS() > mqttQoS.value() ? mqttQoS : MqttQoS.valueOf(retainMsg.getMqttQoS());
            if (respQoS == MqttQoS.AT_MOST_ONCE) {
                MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.PUBLISH, false, respQoS, false, 0),
                        new MqttPublishVariableHeader(retainMsg.getTopic(), 0), Unpooled.buffer().writeBytes(retainMsg.getMessageBytes()));

                SendManager.pubMsg(publishMessage, contextBo);
            }
            if (respQoS == MqttQoS.AT_LEAST_ONCE) {
                //TODO 需要生成一个独一无二的ID
                int messageId = 0;
                MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.PUBLISH, false, respQoS, false, 0),
                        new MqttPublishVariableHeader(retainMsg.getTopic(), messageId), Unpooled.buffer().writeBytes(retainMsg.getMessageBytes()));
                SendManager.pubMsg(publishMessage, contextBo);
            }
            if (respQoS == MqttQoS.EXACTLY_ONCE) {
                //TODO 需要生成一个独一无二的ID
                int messageId = 0;
                MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.PUBLISH, false, respQoS, false, 0),
                        new MqttPublishVariableHeader(retainMsg.getTopic(), messageId), Unpooled.buffer().writeBytes(retainMsg.getMessageBytes()));
                SendManager.pubMsg(publishMessage, contextBo);
            }
        }catch (Exception e){
            logger.warn("订阅消息时发遗留消息异常:{}", contextBo, e);
        }

    }
}
