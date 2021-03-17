package com.bytecub.application.boot;

import com.bytecub.common.biz.EsUtil;
import com.bytecub.gateway.boot.GatewayBootService;
import com.bytecub.mqtt.boot.MQTTInitServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: InitApplication.java, v 0.1 2020-12-02  Exp $$  
 */
@Component
@Slf4j
public class InitApplication implements ApplicationRunner {
    @Autowired
    MQTTInitServer mqttInitServer;
    @Autowired
    GatewayBootService gatewayBootService;
    @Value("${spring.profiles.active}")
    private String env;

    @Override public void run(ApplicationArguments args) throws Exception {
        try {
            mqttInitServer.start();
            gatewayBootService.boot();
            EsUtil.setEnv(env);
        } catch (Exception e) {
            log.error("启动异常", e);
            System.exit(-1);
        }
    }
}
