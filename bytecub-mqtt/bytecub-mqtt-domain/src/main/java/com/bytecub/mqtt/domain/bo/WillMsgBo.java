package com.bytecub.mqtt.domain.bo;

import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by songbin on 2020-11-27.
 */
@Data
public class WillMsgBo implements Serializable {
    private static final long serialVersionUID = -6054051345590588841L;
    private String clientId;
    private boolean cleanSession;
    private String topic;
    private MqttPublishMessage msg;
    public WillMsgBo(String clientId, String topic, boolean cleanSession, MqttPublishMessage willMessage) {
        this.clientId = clientId;
        this.cleanSession = cleanSession;
        this.msg = willMessage;
        this.topic = topic;
    }

}
