package com.bytecub.mqtt.service.protocol;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.gateway.mq.DeviceUpMessageBo;
import com.bytecub.gateway.mq.producer.DeviceReplyMessageProducer;
import com.bytecub.gateway.mq.producer.DeviceUpMessageProducer;
import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.network.SendManager;
import com.bytecub.mqtt.service.state.ClientManager;
import com.bytecub.mqtt.service.state.RetainMsgManager;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author songbin
 * @version Id: PublishHandle.java, v 0.1 2019/1/16   Exp $$
 */
@Component
public class PublishHandler {
    private static final Logger logger = LoggerFactory.getLogger(PublishHandler.class);

    public void onPublish(ContextBo contextBo, MqttPublishMessage msg) {
        try{
            logger.debug("接收到publish请求:{}", msg);
            this.retainMsg(msg);
            this.pubAck(contextBo, msg);
            this.sendMsgToClient(msg);
            this.sendMq(msg);
        }catch (Exception e){
            logger.warn("发消息异常:{}",msg, e);
        }

    }
    private void sendMq(MqttPublishMessage msg){
        String topic = msg.variableHeader().topicName();
        if(!topic.startsWith(BCConstants.MQTT.GLOBAL_UP_PREFIX)){
            /**只处理上行*/
            return;
        }
        DeviceUpMessageBo deviceUpMessageBo = new DeviceUpMessageBo();
        deviceUpMessageBo.setTopic(topic);
        deviceUpMessageBo.setPacketId((long)msg.variableHeader().packetId());
        deviceUpMessageBo.setSourceMsg(ByteBufUtil.getBytes(msg.content()));
        if(topic.endsWith(BCConstants.TOPIC.MSG_REPLY) || topic.endsWith(BCConstants.TOPIC.PROP_GET_REPLY)){
            DeviceReplyMessageProducer.send(deviceUpMessageBo);
        }else{
            DeviceUpMessageProducer.send(deviceUpMessageBo);
        }
    }
    /**先给发送的人反馈下收到了*/
    private void pubAck(ContextBo contextBo, MqttPublishMessage msg){
        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
        MqttFixedHeader fixedHeader = null;
        if (mqttQoS.value() <= 1) {
            // 不是级别最高的QOS 返回 puback 即可
            fixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK, false, mqttQoS, false, 0);
        } else {
            // 否则发送发布收到 QOS级别会2
            fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC, false, MqttQoS.AT_MOST_ONCE, false, 0);
        }
        String topicName = msg.variableHeader().topicName();
        int msgId = msg.variableHeader().packetId();
        MqttMessageIdVariableHeader msgIdVariableHeader = null;
        if(msgId > 0){
            msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
        }
        MqttPubAckMessage ackMessage = new MqttPubAckMessage(fixedHeader, msgIdVariableHeader);
        if(mqttQoS.value() < 1){
           //是0不需要确认
        }else{
            SendManager.responseMsg(contextBo, ackMessage, msgId, true);
        }

    }
    /**
     * 将publish接收到的消息主动推送到该topic下所有终端上去
     * */
    private void sendMsgToClient(MqttPublishMessage msg){
        try{
            ClientManager.pubTopic(msg);
        }catch (Exception e){
            logger.warn("发消息异常:{}",e);
        }
    }

    private void retainMsg( MqttPublishMessage msg){
        try{
            RetainMsgManager.pushRetain(msg);
        }catch (Exception e){
            logger.warn("遗留消息处理异常",msg, e);
        }
    }
}
