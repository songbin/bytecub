package com.bytecub.common.domain.gateway.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: DeviceActiveMqBo.java, v 0.1 2021-01-05  Exp $$
  */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceActiveMqBo {
    String deviceCode;
    Boolean active;
    String host;
    Integer port;
    Date timestamp;
}
