package com.bytecub.gateway.mq.storage;

import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.domain.gateway.mq.DeviceActiveMqBo;

import java.util.concurrent.LinkedBlockingQueue;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ActiveStorage.java, v 0.1 2021-01-05  Exp $$
  */

public class ActiveMQStorage {
    static private LinkedBlockingQueue<DeviceActiveMqBo> queue = new LinkedBlockingQueue<>();

    static public void push(DeviceActiveMqBo e) {
         queue.offer(e);
    }

    static public int size(){
        return queue.size();
    }
    static public DeviceActiveMqBo pop() {
        try{
            return queue.take();
        }catch (Exception e){
            throw new BCGException(BCErrorEnum.INNER_EXCEPTION, "设备在线状态变更消费异常", e);
        }
    }
}
