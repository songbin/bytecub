package com.bytecub.mqtt.service.core;

import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.biz.ProducerBiz;
import com.bytecub.mqtt.service.network.SendManager;
import com.bytecub.mqtt.service.protocol.*;
import com.bytecub.mqtt.service.state.SessionManger;
import com.bytecub.mqtt.service.state.WillMsgManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songbin
 * @version Id: MqttHandler.java, v 0.1 2019/1/15   Exp $$
 */
public class MqttHandler extends SimpleChannelInboundHandler<MqttMessage> {
    private static final Logger logger = LoggerFactory.getLogger(MqttHandler.class);
    private  ContextBo contextBo = new ContextBo();
    private HandlerFactory handlerFactory;

    public MqttHandler(HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    @Override
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
        try{
            logger.debug("收到消息:{}", msg);
            this.contextBo.setHandlerContext(ctx);
            this.verify(ctx, msg);
            this.dispatch(ctx,msg);
        }catch (Exception e){
            ReferenceCountUtil.release(msg);
            logger.warn("处理消息异常{}",msg, e);
        }

    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                Channel channel = ctx.channel();
                String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
                // 发送遗嘱消息
                WillMsgManager.pubWill(clientId);
                ctx.close();
                SessionManger.removeContextByCtx(ctx);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("exceptionCaught", cause);
       // if(null != ctx) ctx.close();
        SessionManger.removeContextByCtx(ctx);
    }

    private void dispatch(ChannelHandlerContext ctx, MqttMessage msg){

        switch (msg.fixedHeader().messageType()) {
            case CONNECT:
                //this.updateContext(ctx, (MqttConnectMessage) msg);
                this.handlerFactory.connectHandler.onConnect(this.contextBo, (MqttConnectMessage) msg);
                break;
            case PUBLISH:// 客户端发布普通消息
                this.handlerFactory.publishHandler.onPublish(this.contextBo, (MqttPublishMessage) msg);
                break;
            case PUBACK://客户端发布确认
                this.handlerFactory.pubAckHandler.onPubAck(this.contextBo, (MqttPubAckMessage)msg);
                break;
            case PUBREC:// 客户端发布收到
                this.handlerFactory.pubRecHandler.onPubRec(this.contextBo, msg);
                break;
            case PUBREL:// 客户端发布释放
                this.handlerFactory.pubRelHandler.onPubRel(this.contextBo, msg);
                break;
            case PUBCOMP://客户端发布完成
                this.handlerFactory.pubCompHandler.onPubComp(this.contextBo, msg);
                break;
            case SUBSCRIBE:
                this.handlerFactory.subscribeHandler.onSubscribe(contextBo, (MqttSubscribeMessage) msg);
                break;
            case UNSUBSCRIBE:
                this.handlerFactory.unSubscribeHandler.onUnsubscribe(contextBo, (MqttUnsubscribeMessage) msg);
                break;
            case PINGREQ:
                this.handlerFactory.pingReqHandler.onPingReq(contextBo);
                break;
            case DISCONNECT:
                this.handlerFactory.disconnectHandler.onDisconnect(contextBo);
                break;
        }
    }

    private void verify(ChannelHandlerContext ctx, MqttMessage msg){
        if (msg.decoderResult().isFailure()) {

            Throwable cause = msg.decoderResult().cause();

            if (cause instanceof MqttUnacceptableProtocolVersionException) {

                SendManager.responseMsg(
                        contextBo,
                        MqttMessageFactory.newMessage(
                                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION, false),
                                null),
                        null,
                        true);

            } else if (cause instanceof MqttIdentifierRejectedException) {

                SendManager.responseMsg(
                        contextBo,
                        MqttMessageFactory.newMessage(
                                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED, false),
                                null),
                        null,
                        true);
            }
            SessionManger.removeContextByCtx(ctx);
            return;
        }
    }



















}
