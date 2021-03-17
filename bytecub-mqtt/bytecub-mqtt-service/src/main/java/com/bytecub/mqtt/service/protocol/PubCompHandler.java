package com.bytecub.mqtt.service.protocol;


import com.bytecub.mqtt.domain.bo.ContextBo;
import io.netty.handler.codec.mqtt.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**PUBCOMP报文是对PUBREL报文的响应。它是QoS 2等级协议交换的第四个也是最后一个报文。
 * @author songbin
 * @version Id: PubCompHandler.java, v 0.1 2019/1/16   Exp $$
 */
@Component
public class PubCompHandler {
    private static final Logger logger = LoggerFactory.getLogger(PubCompHandler.class);
    public void onPubComp(ContextBo contextBo, MqttMessage msg) {
        logger.debug("客户端发布完成:{}", msg);

    }
}
