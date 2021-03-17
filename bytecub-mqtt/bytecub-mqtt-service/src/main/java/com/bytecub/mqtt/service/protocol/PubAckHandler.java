package com.bytecub.mqtt.service.protocol;


import com.bytecub.mqtt.domain.bo.ContextBo;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * PUBACK报文是对QoS 1等级的PUBLISH报文的响应。
 *
 * @author songbin
 * @version Id: PublishBackHandler.java, v 0.1 2019/1/16   Exp $$
 */
@Component
public class PubAckHandler {
    private static final Logger logger = LoggerFactory.getLogger(PubAckHandler.class);

    public  void onPubAck(ContextBo contextBo, MqttPubAckMessage msg) {
        logger.debug("接收客户端发布确认,即将给客户端返回PubRec");
    }



}


