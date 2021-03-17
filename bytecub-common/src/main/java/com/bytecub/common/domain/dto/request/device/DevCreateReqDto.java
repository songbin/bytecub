package com.bytecub.common.domain.dto.request.device;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: DevCreateReqDto.java, v 0.1 2020-12-22  Exp $$
  */
@Data public class DevCreateReqDto {
    String deviceCode = "";
    String deviceName = "";
    String gwDevCode = "";
    String productCode = "";
    String deviceSecret = "";
}
