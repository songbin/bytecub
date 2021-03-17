package com.bytecub.mqtt.service.protocol.resp;


import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.network.SendManager;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**PUBACK报文是对QoS 1等级的PUBLISH报文的响应。
 * @author songbin
 * @version Id: PublishBackHandler.java, v 0.1 2019/1/16   Exp $$
 */
public class PubAckResp {
    private static final Logger logger = LoggerFactory.getLogger(PubAckResp.class);
    public void onPubAck(ContextBo contextBo, MqttPublishMessage msg) {
        logger.debug("客户端发布确认");
        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
        MqttFixedHeader fixedHeader = null;
        if (mqttQoS.value() <= 1) {
            // 不是级别最高的QOS 返回 puback 即可
            fixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK, false, mqttQoS, false, 0);
        } else {
            // 否则发送发布收到 QOS级别会2
            fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC, false, MqttQoS.EXACTLY_ONCE, false, 0);
        }

        int msgId = msg.variableHeader().messageId();
        MqttMessageIdVariableHeader msgIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);

        MqttPubAckMessage ackMessage = new MqttPubAckMessage(fixedHeader, msgIdVariableHeader);
        //ctx.write(ackMessage);
        SendManager.responseMsg(contextBo, ackMessage, msgId, true);
    }

}
