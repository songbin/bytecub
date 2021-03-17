package com.bytecub.gateway.mq.producer;

import com.bytecub.common.domain.gateway.mq.ServiceInvokeMessageBo;
import com.bytecub.gateway.mq.storage.ServiceInvokeStorage;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 服务调用下发
  * @author bytecub@163.com  songbin
  * @version Id: ActiveMqProducer.java, v 0.1 2021-01-05  Exp $$
  */
public class ServiceInvokeMessageProducer {
    static public void send(ServiceInvokeMessageBo bo){
        ServiceInvokeStorage.push(bo);
    }
}
