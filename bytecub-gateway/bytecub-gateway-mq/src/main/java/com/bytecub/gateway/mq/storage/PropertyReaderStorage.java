package com.bytecub.gateway.mq.storage;

import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.domain.gateway.mq.PropertyReaderMessageBo;

import java.util.concurrent.LinkedBlockingQueue;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ActiveStorage.java, v 0.1 2021-01-05  Exp $$
  */

public class PropertyReaderStorage {
    static private LinkedBlockingQueue<PropertyReaderMessageBo> queue = new LinkedBlockingQueue<>();

    static public void push(PropertyReaderMessageBo e) {
         queue.offer(e);
    }
    static public int size(){
        return queue.size();
    }
    static public PropertyReaderMessageBo pop() {
        try{
            return queue.take();
        }catch (Exception e){
            throw new BCGException(BCErrorEnum.INNER_EXCEPTION, "属性设备读取队列消费异常", e);
        }
    }
}
