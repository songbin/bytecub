package com.bytecub.gateway.mqtt.network.impl;

import com.bytecub.common.annotations.BcProtocolAnnotation;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.utils.SpringContextUtil;
import com.bytecub.gateway.mqtt.config.BCMqttClient;
import com.bytecub.gateway.mqtt.network.IBCMQTTClientService;
import com.bytecub.mdm.dao.po.ProductPo;
import com.bytecub.mdm.service.IProductService;
import com.bytecub.protocol.base.IBaseProtocol;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: BCMQTTClientServiceImpl.java, v 0.1 2020-12-04  Exp $$
  */
@Slf4j
@Service
@DependsOn("springContextUtil")
public class BCMQTTClientServiceImpl implements IBCMQTTClientService {
    @Value("${bytecub.mqtt.client.timeout:10}")
    int timeout;
    @Value("${bytecub.mqtt.client.keepalive:10}")
    int keepAlive;

    @Value("${bytecub.mqtt.broker.id}")
    String brokerId;
    @Autowired IProductService productService;
    private BCMqttClient bcMqttClient;

    @Override
    public void createSubscribe(String url, String topic, String handleClass, String productCode) {
        this.bcMqttClient = new BCMqttClient();
        this.bcMqttClient.setServerId(brokerId);
        this.bcMqttClient.connect(url, this.genClientId(),
                null, null, timeout, keepAlive,
                null, productCode);
        this.bcMqttClient.subscribe(topic);

    }

    private final String genClientId() {
        String clientId = BCConstants.MQTT.SYS_CLIENT_TOPIC_PREFIX + UUID.randomUUID().toString().replaceAll("-", "");
        return clientId;
    }


    @Override public Boolean publish(String topic, byte[] msg, MqttQoS mqttQoS) {
        this.bcMqttClient.publish(mqttQoS.value(), false, topic, msg);
        return true;
    }
}
