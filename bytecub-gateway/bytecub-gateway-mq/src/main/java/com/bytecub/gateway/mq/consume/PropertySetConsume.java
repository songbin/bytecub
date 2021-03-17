package com.bytecub.gateway.mq.consume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bytecub.common.domain.gateway.mq.PropertySetMessageBo;
import com.bytecub.gateway.mq.excutor.PropertySetExecutor;
import com.bytecub.gateway.mq.storage.PropertySetStorage;

import lombok.extern.slf4j.Slf4j;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: MQTTConsumeMonitor.java, v 0.1 2020-12-09  Exp $$  
 */
@Service
@Slf4j
public class PropertySetConsume {

    @Autowired
    PropertySetExecutor propertySetExecutor;

    /***
     * 具体线程
     */
    @Async
    public void execute() {
        while (true) {
            try {
                PropertySetMessageBo msg = PropertySetStorage.pop();
                propertySetExecutor.execute(msg);
            } catch (Exception e) {
                log.warn("内部队列消费异常", e);

            }
        }

    }

}
