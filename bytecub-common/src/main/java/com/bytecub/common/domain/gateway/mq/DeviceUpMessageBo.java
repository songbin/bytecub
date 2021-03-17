package com.bytecub.common.domain.gateway.mq;

import com.bytecub.common.metadata.ProductFuncTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 存储在队列中的对象
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: QueueMsgBo.java, v 0.1 2020-12-09  Exp $$
  */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceUpMessageBo {
    String        productCode;
    //IBaseProtocol baseProtocolService;
    byte[]        sourceMsg;
    String topic;
    /**mqtt消息中的packetId*/
    Long packetId;
    String deviceCode;
    Date currTime = new Date();
    /**物模型类型*/
    ProductFuncTypeEnum funcType;
}
