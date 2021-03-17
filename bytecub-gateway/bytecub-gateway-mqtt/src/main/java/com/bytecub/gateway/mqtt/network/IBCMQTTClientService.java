package com.bytecub.gateway.mqtt.network;

import com.bytecub.protocol.base.IBaseProtocol;
import io.netty.handler.codec.mqtt.MqttQoS;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IBCMQTTClientService.java, v 0.1 2020-12-04  Exp $$
  */
public interface IBCMQTTClientService {
    void createSubscribe(String urls, String topic, String handleClass, String productCode);
    Boolean publish(String topic, byte[] msg, MqttQoS mqttQoS);
}
