package com.bytecub.gateway.mq.excutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.gateway.mq.DeviceActiveMqBo;
import com.bytecub.mdm.service.IDeviceService;

import lombok.extern.slf4j.Slf4j;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: ActiveMqConsume.java, v 0.1 2021-01-05  Exp $$  
 */
@Slf4j
@Service
public class ActiveDeviceExecutor {
    @Autowired
    IDeviceService deviceService;

    @Async(BCConstants.TASK.DEVICE_ACTIVE_NAME)
    public void execute(DeviceActiveMqBo bo){
        try {
            deviceService.activeDevice(bo);
        } catch (Exception e) {
            log.warn("", e);
        }
    }

}
