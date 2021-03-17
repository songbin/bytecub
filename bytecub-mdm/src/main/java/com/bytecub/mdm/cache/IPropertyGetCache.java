package com.bytecub.mdm.cache;

import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IPropertGetCache.java, v 0.1 2021-01-26  Exp $$
  */
public interface IPropertyGetCache {
    /**
     * 属性读取缓存设置
     * 当服务端接收都属性读取操作来自设备的publish之后会将读到的值通过该方法存储
     * @param messageId
     * @param value
     * @return
     * */
    void propGetValueWrite(String messageId, Object value);
    /**
     * 属性读取操作会在超时时间内不断的调用该方法获取属性值
     * @param messageId
     * @param tClass
     * @return 设备上报的属性值
     * */
    <T> T propGetReader(String messageId, Class<T> tClass);
    /**
     * 属性读取操作会在超时时间内不断的调用该方法获取属性值
     * @param messageId
     * @return 设备上报的属性值
     * */
    Map<String, Object> propGetReader(String messageId);
}
