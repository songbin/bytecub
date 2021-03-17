package com.bytecub.mqtt.service.protocol;


import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.biz.ProducerBiz;
import com.bytecub.mqtt.service.network.SendManager;
import com.bytecub.mqtt.service.state.ClientManager;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author songbin
 * @version Id: PingReqHandler.java, v 0.1 2019/1/16   Exp $$
 */
@Component
public class PingReqHandler {
    private static final Logger logger = LoggerFactory.getLogger(PingReqHandler.class);
    public  void onPingReq(ContextBo contextBo) {
        logger.debug("收到ping {}", contextBo);
        try{
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttMessage mqttMessage = new MqttMessage(mqttFixedHeader);
            //contextBo.getHandlerContext().write(mqttMessage);
            ClientManager.updateClientOnLine(contextBo.getClientId());
            ProducerBiz.sendActiveMQ(contextBo, true);
            SendManager.sendMessage(mqttMessage, contextBo.getClientId(), null, true);
        }catch (Exception e){
            logger.warn("处理ping消息异常:{}", e);
        }

    }
}
