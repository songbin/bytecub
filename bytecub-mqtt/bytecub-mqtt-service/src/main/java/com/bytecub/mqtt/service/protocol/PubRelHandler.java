package com.bytecub.mqtt.service.protocol;

import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.network.SendManager;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**PUBREL报文是对PUBREC报文的响应。它是QoS 2等级协议交换的第三个报文。
 * @author songbin
 * @version Id: PubRelHandler.java, v 0.1 2019/1/16   Exp $$
 */
@Component
public class PubRelHandler {
    private static final Logger logger = LoggerFactory.getLogger(PubRelHandler.class);
    public void onPubRel(ContextBo contextBo, MqttMessage msg) {
        int msgId = ((MqttMessageIdVariableHeader)msg.variableHeader()).messageId();
        MqttMessageIdVariableHeader msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
        MqttMessage ackMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBCOMP, false, MqttQoS.AT_MOST_ONCE, false, 2),
                msgIdVariableHeader,
                null);
        //ctx.write(ackMessage);

        SendManager.responseMsg(contextBo, ackMessage, msgId, true);
        logger.debug("发送PUBCOMP消息:{}", ackMessage);
    }
}
