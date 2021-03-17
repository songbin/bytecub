package com.bytecub.common.domain.dto.request.device;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: DevicceImportReqDto.java, v 0.1 2021-01-05  Exp $$
  */
@Data
public class DeviceImportReqDto {
    String deviceCode;
    String productCode;
    String deviceName;
    String deviceSecret;
}
