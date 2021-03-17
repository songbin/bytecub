package com.bytecub.common.domain.message;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 服务端发送到设备的下行数据格式
  * @author bytecub@163.com  songbin
  * @version Id: DeviceDownMessage.java, v 0.1 2021-01-20  Exp $$
  */
@Data
public class DeviceDownMessage {
    String messageId;
    /**时间戳，单位毫秒*/
    Long timestamp;
    /**消息体*/
    Object body;
    /**下发的指令,服务调用的时候就是服务标识符*/
    String identifier;
    String productCode;
    String deviceCode;
}
