package com.bytecub.mqtt.service.core;

import com.bytecub.mqtt.service.protocol.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: HandlerFactory.java, v 0.1 2021-01-06  Exp $$  
 */
@Component
public class HandlerFactory {
    @Autowired
    ConnectHandler connectHandler;
    @Autowired
    DisconnectHandler disconnectHandler;
    @Autowired
    PingReqHandler pingReqHandler;
    @Autowired
    PubAckHandler pubAckHandler;
    @Autowired
    PubCompHandler pubCompHandler;
    @Autowired
    PublishHandler publishHandler;
    @Autowired
    PubRecHandler pubRecHandler;
    @Autowired
    PubRelHandler pubRelHandler;
    @Autowired
    SubscribeHandler subscribeHandler;
    @Autowired
    UnSubscribeHandler unSubscribeHandler;
}
