package com.bytecub.mqtt.domain.bo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttVersion;
import lombok.Data;

/**
 * 链接上下文信息元数据
 * @author songbin
 * @version Id: ContextBo.java, v 0.1 2019/1/16   Exp $$
 */
@Data
public class ContextBo {
    ChannelHandlerContext handlerContext;
    private int keepAlive;
    private MqttVersion version;
    private String clientId;
    private Boolean cleanSession = false;
    private int keepAliveMax;
    private String userName;
    private Boolean connected = false;
    private MqttPublishMessage willMessage;
}
