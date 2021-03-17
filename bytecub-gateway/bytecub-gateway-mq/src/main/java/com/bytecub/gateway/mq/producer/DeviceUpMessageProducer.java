package com.bytecub.gateway.mq.producer;

import com.bytecub.common.domain.gateway.mq.DeviceUpMessageBo;
import com.bytecub.gateway.mq.storage.DeviceUpMessageStorage;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ActiveMqProducer.java, v 0.1 2021-01-05  Exp $$
  */
public class DeviceUpMessageProducer {
    static public void send(DeviceUpMessageBo bo){
        DeviceUpMessageStorage.push(bo);
    }
}
