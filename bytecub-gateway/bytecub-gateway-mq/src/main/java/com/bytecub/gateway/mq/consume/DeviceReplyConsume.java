package com.bytecub.gateway.mq.consume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bytecub.common.domain.gateway.mq.DeviceUpMessageBo;
import com.bytecub.gateway.mq.excutor.DeviceMessageReplyExecutor;
import com.bytecub.gateway.mq.storage.DeviceReplyMessageStorage;

import lombok.extern.slf4j.Slf4j;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: MQTTConsumeMonitor.java, v 0.1 2020-12-09  Exp $$  
 */
@Service
@Slf4j
public class DeviceReplyConsume {
    @Autowired
    private DeviceMessageReplyExecutor deviceMessageReplyExecutor;

    @Async
    public void execute() {
        while (true) {
            try {
                DeviceUpMessageBo msg = DeviceReplyMessageStorage.pop();
                deviceMessageReplyExecutor.execute(msg);
            } catch (Exception e) {
                log.warn("内部队列消费异常", e);
                continue;
            }
        }
    }

}
