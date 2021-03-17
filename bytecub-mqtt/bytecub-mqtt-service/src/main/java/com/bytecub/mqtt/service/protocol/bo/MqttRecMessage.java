package com.bytecub.mqtt.service.protocol.bo;

import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;

/**
 * @author songbin
 * @version Id: MqttRecMessage.java, v 0.1 2019/7/15   Exp $$
 */
public class MqttRecMessage extends MqttMessage {

    public MqttRecMessage(MqttFixedHeader mqttFixedHeader, MqttMessageIdVariableHeader variableHeader) {
        super(mqttFixedHeader, variableHeader);
    }

    public MqttMessageIdVariableHeader variableHeader() {
        return (MqttMessageIdVariableHeader)super.variableHeader();
    }
}
