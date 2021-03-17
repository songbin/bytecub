package com.bytecub.mqtt.service.protocol.resp;


import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.network.SendManager;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**PUBREC报文是对QoS等级2的PUBLISH报文的响应。它是QoS 2等级协议交换的第二个报文
 * @author songbin
 * @version Id: PubRecHandler.java, v 0.1 2019/1/16   Exp $$
 */

public class PubRelResp {
    private static final Logger logger = LoggerFactory.getLogger(PubRelResp.class);
    public static void sendPubRel(ContextBo contextBo, MqttMessage msg) {


        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
        logger.debug("准备向客户端发送rel");
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREL, false, MqttQoS.AT_LEAST_ONCE, false, 2);;

        // 不是级别最高的QOS 返回 puback 即可
       // MqttMessageIdVariableHeader msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
        MqttMessage ackMessage = new MqttMessage(fixedHeader, msg.variableHeader());
        SendManager.responseMsg(contextBo, ackMessage, null, true);
    }

}
