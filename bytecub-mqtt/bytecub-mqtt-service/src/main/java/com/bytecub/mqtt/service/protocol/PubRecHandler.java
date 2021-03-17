package com.bytecub.mqtt.service.protocol;


import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.network.SendManager;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**PUBREC报文是对QoS等级2的PUBLISH报文的响应。它是QoS 2等级协议交换的第二个报文
 * @author songbin
 * @version Id: PubRecHandler.java, v 0.1 2019/1/16   Exp $$
 */
@Component
public class PubRecHandler {
    private static final Logger logger = LoggerFactory.getLogger(PubRecHandler.class);
    public void onPubRec(ContextBo contextBo, MqttMessage msg) {
        logger.debug("客户端发布收到:{}", msg);

        int msgId = ((MqttMessageIdVariableHeader)msg.variableHeader()).messageId();
        MqttMessageIdVariableHeader msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
        MqttMessage ackMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBREL, false,
                        MqttQoS.AT_LEAST_ONCE, false, 2),
                msgIdVariableHeader,
                null);
        //ctx.write(ackMessage);
        SendManager.responseMsg(contextBo, ackMessage, msgId, true);
        logger.debug("发送PUBREL消息:{}", ackMessage);
    }

}
