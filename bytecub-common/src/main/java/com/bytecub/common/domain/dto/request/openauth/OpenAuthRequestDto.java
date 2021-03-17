package com.bytecub.common.domain.dto.request.openauth;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: OpenAuthRequest.java, v 0.1 2021-01-08  Exp $$
  */
@Data
public class OpenAuthRequestDto {
    @NotNull(message = "key不能为空")
    @Size(max = 20, message = "key最大长度20")
    String appKey;
    @NotNull(message = "名称不能为空")
    @Size(max = 20, message = "名称最大长度20")
    String appName;
    @Size(max = 30, message = "描述最大长度30")
    String appDesc;
}
