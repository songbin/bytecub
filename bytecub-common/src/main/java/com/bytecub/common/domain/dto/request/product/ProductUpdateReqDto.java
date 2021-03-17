package com.bytecub.common.domain.dto.request.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProductDetailResDto.java, v 0.1 2020-12-16  Exp $$
  */
@Data
public class ProductUpdateReqDto {
    private Long id;
    private String productCode;
    private String productName;
    private List<String> transportList;
    private String protocolCode;
    private String nodeType;
    private String nodeName;
    private String productDesc;
    private Integer productStatus;
    /**协议开始字段*/
    private String protocolName;
    private Integer protocolType;

}
