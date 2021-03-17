package com.bytecub.mqtt.service.protocol.resp;


import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.network.SendManager;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**PUBCOMP报文是对PUBREL报文的响应。它是QoS 2等级协议交换的第四个也是最后一个报文。
 * @author songbin
 * @version Id: PubCompHandler.java, v 0.1 2019/1/16   Exp $$
 */
public class PubCompResp {
    private static final Logger logger = LoggerFactory.getLogger(PubCompResp.class);


    public static void sendPubComp(ContextBo contextBo, MqttMessage msg) {
        logger.debug("准备向客户端发送compete");

        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
        MqttFixedHeader fixedHeader = null;

        // 不是级别最高的QOS 返回 puback 即可
        fixedHeader = new MqttFixedHeader(MqttMessageType.PUBCOMP, false, mqttQoS, false, 1);
        MqttMessage ackMessage = new MqttMessage(fixedHeader, msg.variableHeader());
        SendManager.responseMsg(contextBo, ackMessage, null, true);
    }
}
