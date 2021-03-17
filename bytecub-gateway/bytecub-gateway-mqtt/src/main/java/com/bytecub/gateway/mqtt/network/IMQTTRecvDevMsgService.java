package com.bytecub.gateway.mqtt.network;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 监听来自设备发送给mqtt server的消息
  * @author bytecub@163.com  songbin
  * @version Id: RecvDevMsgService.java, v 0.1 2020-12-03  Exp $$
  */
public interface IMQTTRecvDevMsgService {
    /**
     * 启动一个mqtt client
     * */
    void startListen();
}
