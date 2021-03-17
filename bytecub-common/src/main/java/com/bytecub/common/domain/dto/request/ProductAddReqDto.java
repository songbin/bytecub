package com.bytecub.common.domain.dto.request;

import lombok.Data;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProductResDto.java, v 0.1 2020-12-15  Exp $$
  */
@Data
public class ProductAddReqDto {
    private String       productName;
    private String protocolCode;
    private String nodeType;
    private String       productDesc;
    private List<String> transportList;

}
