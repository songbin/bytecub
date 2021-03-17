package com.bytecub.mqtt.service.network;


import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.state.SessionManger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songbin
 * @version Id: BrokerSessionHelper.java, v 0.1 2019/1/15   Exp $$
 */
public class SendManager {
    private static final Logger logger = LoggerFactory.getLogger(SendManager.class);

    /**
     * 发送信息
     *用于服务端向客户端下发消息，通过clientID 找到当时的channel
     * @param msg
     * @param clientId
     * @param packetId
     * @param flush
     */
    public static void sendMessage(MqttMessage msg, String clientId, Integer packetId, boolean flush) {
        ContextBo contextBo = SessionManger.getContextByClientId(clientId);
        if (contextBo == null || null == contextBo.getHandlerContext()) {
            String pid = packetId == null || packetId <= 0 ? "" : String.valueOf(packetId);
            return;
        }
        responseMsg(contextBo, msg, packetId, flush);
    }


    /**
     * 发送信息
     * 用于服务端向客户端发送响应消息
     * @param contextBo
     * @param msg
     * @param clientId
     * @param packetId
     * @param flush
     */
    public static void responseMsg(ContextBo contextBo, MqttMessage msg, Integer packetId, boolean flush) {
        String pid = packetId == null || packetId <= 0 ? "" : String.valueOf(packetId);
        ChannelFuture future = flush ? contextBo.getHandlerContext().writeAndFlush(msg) : contextBo.getHandlerContext().write(msg);
        future.addListener(f -> {
            if (f.isSuccess()) {
                logger.debug("成功向[{}]发送消息:{}", contextBo.getClientId(), msg);
            } else {
                logger.debug("失败向[{}]发送消息:{}  失败原因:{}", contextBo.getClientId(), msg, f.cause());
            }
        });
    }


    /**
     * 向单个client发送publish消息
     *
     * @param msg       生产者的相关信息
     * @param contextBo 消息接收方的相关信息
     */
    public static void pubMsg(MqttPublishMessage msg, ContextBo contextBo) {

        try {
            final Channel channel = contextBo.getHandlerContext().channel();
            MqttQoS qos = msg.fixedHeader().qosLevel();
            int dupTimes = 0;
            ByteBuf sendBuf = msg.content().retainedDuplicate();
            sendBuf.resetReaderIndex();
            MqttFixedHeader Header = new MqttFixedHeader(MqttMessageType.PUBLISH,
                    dupTimes > 0, qos, msg.fixedHeader().isRetain(), 0);
            MqttPublishVariableHeader publishVariableHeader = new MqttPublishVariableHeader(
                    msg.variableHeader().topicName(), msg.variableHeader().packetId());
            MqttPublishMessage publishMessage = new MqttPublishMessage(Header,
                    publishVariableHeader, sendBuf);
            channel.writeAndFlush(publishMessage);

        } catch (Exception e) {
            logger.warn("发送消息异常 {}", msg, e);
        }
    }

}
