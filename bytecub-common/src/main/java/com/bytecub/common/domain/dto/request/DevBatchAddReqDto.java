package com.bytecub.common.domain.dto.request;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: DevBatchAddReqDto.java, v 0.1 2020-12-22  Exp $$
  */
@Data
public class DevBatchAddReqDto {
    Integer number;
    String productCode = "";
    String gwDevCode = "";
    String deviceName = "";
}
