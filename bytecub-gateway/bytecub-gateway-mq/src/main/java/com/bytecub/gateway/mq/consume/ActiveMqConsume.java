package com.bytecub.gateway.mq.consume;

import com.bytecub.gateway.mq.excutor.ActiveDeviceExecutor;
import com.bytecub.gateway.mq.storage.ActiveMQStorage;
import com.bytecub.common.domain.gateway.mq.DeviceActiveMqBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ActiveMqConsume.java, v 0.1 2021-01-05  Exp $$
  */
@Slf4j
@Service
public class ActiveMqConsume {
    @Autowired
    ActiveDeviceExecutor activeDeviceExecutor;
    @Async
    public void execute() throws InterruptedException {
         while(true){
             try{
                 DeviceActiveMqBo bo = ActiveMQStorage.pop();
                 activeDeviceExecutor.execute(bo);
             }catch (Exception e){
                 log.warn("",e);
                 continue;
             }
         }
    }

}
