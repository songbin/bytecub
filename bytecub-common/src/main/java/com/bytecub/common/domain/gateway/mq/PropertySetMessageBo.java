package com.bytecub.common.domain.gateway.mq;

import com.alibaba.fastjson.JSONObject;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import lombok.Data;

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
public class PropertySetMessageBo {
    String     deviceCode;
    /**下发属性标识符 */
    String              identifier;
    JSONObject command;
    String              dataType;
    String     productCode;
    Date       currTime = new Date();
   // IBaseProtocol baseProtocolService;
    String topic;
    String protocolCode;
    ProductFuncTypeEnum funcType;
    /**
     * 因为要分布式，所以messageId生成放到调用接口的时候生成，后面多个消费者根据messageId来判断是不是要存数据到es
     * */
    String messageId;
}
