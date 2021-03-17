package com.bytecub.gateway.mqtt.network;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IMQTTMonitorTask.java, v 0.1 2020-12-05  Exp $$
  */
public interface IMQTTMonitorTask {
    public void execute( String urls, String topic, String handleClass, String productCode);
}
