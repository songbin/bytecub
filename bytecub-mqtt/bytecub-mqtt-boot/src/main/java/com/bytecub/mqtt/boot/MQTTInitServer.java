package com.bytecub.mqtt.boot;

import com.bytecub.mqtt.domain.constants.MqttConstants;
import com.bytecub.mqtt.service.network.MQTTServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: MQTTInitServer.java, v 0.1 2020-12-02  Exp $$
  */
@Component
@Slf4j
public class MQTTInitServer {
    @Autowired
    MQTTServer mqttServer;
    @Value("${bytecub.device.ping.expire:600000}")
    Long devicePingExpired;


    public void start(){
        try{
            mqttServer.start();
        }catch (Exception e){
            log.error("启动MQTT异常", e);
            System.exit(-1);
        }
    }

    private void initData(){
        MqttConstants.DEVICE_PING_EXPIRED = devicePingExpired;
    }

    @PreDestroy
    public void destroy(){
        mqttServer.stop();
    }
}
