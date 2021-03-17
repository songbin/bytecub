package com.bytecub.mqtt.service.protocol.resp;


import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.network.SendManager;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**PUBREC报文是对QoS等级2的PUBLISH报文的响应。它是QoS 2等级协议交换的第二个报文
 * @author songbin
 * @version Id: PubRecHandler.java, v 0.1 2019/1/16   Exp $$
 */

public class PubRecResp {
    private static final Logger logger = LoggerFactory.getLogger(PubRecResp.class);
    public static void sendPubRec(ContextBo contextBo, MqttPublishMessage msg) {
        logger.debug("准备向客户端发送rec");

        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
        MqttFixedHeader fixedHeader = null;

        // 不是级别最高的QOS 返回 puback 即可
        fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC, false, mqttQoS, false, 0);

        int msgId = msg.variableHeader().messageId();
        MqttMessageIdVariableHeader msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
        MqttPubAckMessage ackMessage = new MqttPubAckMessage(fixedHeader, msgIdVariableHeader);
        SendManager.responseMsg(contextBo, ackMessage, msgId, true);
    }

}
