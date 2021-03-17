package com.bytecub.common.domain.dto.request.product;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ReleasePropReqDto.java, v 0.1 2021-01-14  Exp $$
  */
@Data
public class ReleasePropReqDto {
    @NotNull(message = "产品编码不能为空")
    String productCode;
    @NotNull(message = "属性类型不能为空")
    String funcType;
}
