package com.bytecub.gateway.mq.producer;

import com.bytecub.gateway.mq.storage.ActiveMQStorage;
import com.bytecub.common.domain.gateway.mq.DeviceActiveMqBo;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ActiveMqProducer.java, v 0.1 2021-01-05  Exp $$
  */
public class DeviceActiveMqProducer {
    static public void send(DeviceActiveMqBo bo){
        ActiveMQStorage.push(bo);
    }
}
