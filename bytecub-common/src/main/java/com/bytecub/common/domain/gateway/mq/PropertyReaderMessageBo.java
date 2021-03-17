package com.bytecub.common.domain.gateway.mq;

import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.common.domain.message.DeviceDownMessage;
import lombok.Data;

/**
 * 存储在队列中的对象
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: QueueMsgBo.java, v 0.1 2020-12-09  Exp $$
  */
@Data
public class PropertyReaderMessageBo {
    DevicePageResDto devicePageResDto;
    DeviceDownMessage deviceDownMessage;
}
