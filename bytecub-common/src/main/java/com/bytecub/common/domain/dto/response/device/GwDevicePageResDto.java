package com.bytecub.common.domain.dto.response.device;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: GwDevicePageResDto.java, v 0.1 2021-02-01  Exp $$
  */
@Data
public class GwDevicePageResDto extends DevicePageResDto {
    long deviceTotal;
    long deviceActive;
}
