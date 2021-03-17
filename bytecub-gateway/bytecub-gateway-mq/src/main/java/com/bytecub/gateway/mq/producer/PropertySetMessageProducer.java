package com.bytecub.gateway.mq.producer;

import com.bytecub.common.domain.gateway.mq.PropertySetMessageBo;
import com.bytecub.gateway.mq.storage.PropertySetStorage;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 服务调用下发
  * @author bytecub@163.com  songbin
  * @version Id: ActiveMqProducer.java, v 0.1 2021-01-05  Exp $$
  */
public class PropertySetMessageProducer {
    static public void send(PropertySetMessageBo bo){
        PropertySetStorage.push(bo);
    }
}
