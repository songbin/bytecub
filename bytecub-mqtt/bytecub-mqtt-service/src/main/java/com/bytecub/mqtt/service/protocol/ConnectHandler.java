package com.bytecub.mqtt.service.protocol;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.domain.bo.WillMsgBo;
import com.bytecub.mqtt.service.network.SendManager;
import com.bytecub.mqtt.service.state.AuthManager;
import com.bytecub.mqtt.service.state.SessionManger;
import com.bytecub.mqtt.service.state.WillMsgManager;
import com.bytecub.utils.SpringContextUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * MQTT Connect请求响应
 * @author songbin
 * @version Id: ConnectHandler.java, v 0.1 2019/1/16   Exp $$
 */
@Component
public class ConnectHandler {

    private  final       Logger      logger      = LoggerFactory.getLogger(ConnectHandler.class);
    @Autowired
    private AuthManager authManager ;
    public  void onConnect(ContextBo contextBo, MqttConnectMessage msg) {
        logger.info("新的链接请求:{}", msg);
        ChannelHandlerContext ctx = contextBo.getHandlerContext();
        MqttVersion version = MqttVersion.fromProtocolNameAndLevel(msg.variableHeader().name(), (byte) msg.variableHeader().version());
        String clientId = msg.payload().clientIdentifier();
        boolean cleanSession = msg.variableHeader().isCleanSession();
        contextBo.setVersion(version);
        contextBo.setClientId(clientId);
        contextBo.setCleanSession(cleanSession);
        contextBo.setUserName(msg.payload().userName());
        if (msg.variableHeader().keepAliveTimeSeconds() > 0 && msg.variableHeader().keepAliveTimeSeconds() <= contextBo.getKeepAliveMax()) {
            int keepAlive = msg.variableHeader().keepAliveTimeSeconds();
            contextBo.setKeepAlive(keepAlive);
        }
        if(!authCheck(contextBo, msg)){
            logger.warn("用户名密码错误:{}", msg);
            return;
        }
        //有可能发送俩次的连接包。如果已经存在连接就是关闭当前的连接。
//        if (contextBo.getConnected()) {
//            ctx.close();
//            return;
//        }
        SessionManger.removeContextByClientId(contextBo.getClientId());
        contextBo.setConnected(true);
        SessionManger.newConnect(contextBo.getClientId(), contextBo);
        processWillMsg(msg);
        SendManager.responseMsg(
                contextBo,
                MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                        new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, !contextBo.getCleanSession()),
                        null),
                null,
                true);
    }

    private  Boolean authCheck(ContextBo contextBo, MqttConnectMessage msg){
        InetSocketAddress address = (InetSocketAddress)contextBo.getHandlerContext().channel().remoteAddress();
        String host = address.getAddress().getHostAddress();
        String clientId = msg.payload().clientIdentifier();
//        if(clientId.startsWith(BCConstants.MQTT.SYS_CLIENT_TOPIC_PREFIX)){
//            /**系统用户不校验*/
//            return true;
//        }
        if("127.0.0.1".equals(host)){
            /**本地用户不校验*/
            return true;
        }
        Boolean hasPasswd = msg.variableHeader().hasPassword();
        //String clientId = contextBo.getClientId();
        Boolean hasUserName = msg.variableHeader().hasPassword();

        // 用户名和密码验证, 这里要求客户端连接时必须提供用户名和密码, 不管是否设置用户名标志和密码标志为1, 此处没有参考标准协议实现
        String username = msg.payload().userName();
        String password = msg.payload().passwordInBytes() == null ? null : new String(msg.payload().passwordInBytes(), CharsetUtil.UTF_8);
        if (!authManager.checkValid(clientId, username, password)) {
            MqttConnAckMessage connAckMessage = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                    new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                    new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD, false), null);
            SendManager.responseMsg(
                    contextBo,
                    connAckMessage,
                    null,
                    true);
            return false;
        }
        return true;
    }

    private  void processWillMsg(MqttConnectMessage msg){
        // 处理遗嘱信息
        if (!msg.variableHeader().isWillFlag()) {
            return;
        }
        MqttPublishMessage willMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBLISH, false,
                        MqttQoS.valueOf(msg.variableHeader().willQos()),
                        msg.variableHeader().isWillRetain(), 0),
                new MqttPublishVariableHeader(msg.payload().willTopic(), 0),
                Unpooled.buffer().writeBytes(msg.payload().willMessageInBytes()));

        WillMsgBo willMsgBo = new WillMsgBo(msg.payload().clientIdentifier(),
                msg.payload().willTopic(), msg.variableHeader().isCleanSession(), willMessage);
        WillMsgManager.pushWill(willMsgBo);

    }
}
