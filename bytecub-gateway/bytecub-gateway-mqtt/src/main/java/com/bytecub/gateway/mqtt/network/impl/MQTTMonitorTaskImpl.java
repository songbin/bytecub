package com.bytecub.gateway.mqtt.network.impl;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.gateway.mqtt.network.IBCMQTTClientService;
import com.bytecub.gateway.mqtt.network.IMQTTMonitorTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: MQTTMonitorTaskImpl.java, v 0.1 2020-12-05  Exp $$
  */
@Service
@Slf4j
public class MQTTMonitorTaskImpl implements IMQTTMonitorTask {
    @Autowired
    IBCMQTTClientService ibcmqttClientService;
    @Override
    public void execute(String urls, String topic, String handleClass, String productCode) {
        try{
            ibcmqttClientService.createSubscribe(urls, topic, handleClass, productCode);
        }catch (Exception e){
            log.warn("内部MQTT客户端订阅出现异常:{}", topic, e);
        }
    }




}
