package com.bytecub.gateway.boot;

import com.bytecub.gateway.mq.consume.*;
import com.bytecub.gateway.mq.mqttclient.BcPubMqttClient;
import com.bytecub.gateway.mqtt.network.IMQTTRecvDevMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: GatewayBootService.java, v 0.1 2021-01-05  Exp $$  
 */
@Service
@Slf4j
public class GatewayBootService {
    @Autowired
    ActiveMqConsume activeMqConsume;
    @Autowired
    DeviceUpConsume deviceUpConsume;
    @Autowired
    ServiceInvokeConsume serviceInvokeConsume;
    @Autowired
    PropertySetConsume            propertySetConsume;
    @Autowired
    DeviceReplyConsume deviceReplyConsume;
    @Autowired
    PropertyReaderConsume propertyReaderConsume;
    @Autowired
    BcPubMqttClient               bcPubMqttClient;

    public void boot() {
        try {
            for (int i = 0; i < 5; i++) {
                activeMqConsume.execute();
                deviceUpConsume.execute();
                serviceInvokeConsume.execute();
                propertySetConsume.execute();
                deviceReplyConsume.execute();
                propertyReaderConsume.execute();
            }
            bcPubMqttClient.init();
        } catch (Exception e) {
            log.warn("启动监听线程异常，系统将终止", e);
            System.exit(-1);
        }

    }
}
