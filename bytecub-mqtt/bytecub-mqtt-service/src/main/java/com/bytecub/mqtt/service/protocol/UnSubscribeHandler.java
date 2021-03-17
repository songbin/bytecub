package com.bytecub.mqtt.service.protocol;


import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.network.SendManager;
import com.bytecub.mqtt.service.state.ClientManager;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author songbin
 * @version Id: UnSubcribeHandler.java, v 0.1 2019/1/16   Exp $$
 */
@Component
public class UnSubscribeHandler {
    private static final Logger logger = LoggerFactory.getLogger(UnSubscribeHandler.class);
    public void onUnsubscribe(ContextBo contextBo, MqttUnsubscribeMessage msg) {
        logger.info("接收到Unsubscribe请求{}", msg);
        List<String> topicList = msg.payload().topics();
        for(String topic:topicList){
            ClientManager.unsubscribe(topic, contextBo);
        }

        MqttUnsubAckMessage unsubAckMessage = (MqttUnsubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 2),
                MqttMessageIdVariableHeader.from(msg.variableHeader().messageId()),
                null);
        SendManager.responseMsg(contextBo, unsubAckMessage, null, true);
    }
}
