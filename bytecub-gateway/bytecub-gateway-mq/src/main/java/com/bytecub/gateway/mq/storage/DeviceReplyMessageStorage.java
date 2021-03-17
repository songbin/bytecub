package com.bytecub.gateway.mq.storage;

import java.util.concurrent.LinkedBlockingQueue;

import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.domain.gateway.mq.DeviceUpMessageBo;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ActiveStorage.java, v 0.1 2021-01-05  Exp $$
  */

public class DeviceReplyMessageStorage {
    static private LinkedBlockingQueue<DeviceUpMessageBo> queue = new LinkedBlockingQueue<>();

    static public void push(DeviceUpMessageBo e) {
         queue.offer(e);
    }
    static public int size(){
        return queue.size();
    }
    static public DeviceUpMessageBo pop() {
        try{
            return queue.take();
        }catch (Exception e){
            throw new BCGException(BCErrorEnum.INNER_EXCEPTION, "数据上报消费异常", e);
        }

    }
}
