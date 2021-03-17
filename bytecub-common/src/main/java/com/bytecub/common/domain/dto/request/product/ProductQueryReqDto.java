package com.bytecub.common.domain.dto.request.product;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProductQueryReqDto.java, v 0.1 2021-02-01  Exp $$
  */
@Data
public class ProductQueryReqDto {
    String nodeType;
    String productCode;
    String productName;
}
