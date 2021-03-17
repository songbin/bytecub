package com.bytecub.common.domain.dto.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProductResDto.java, v 0.1 2020-12-15  Exp $$
  */
@Data
public class ProductResDto {
    private Long id;

    private String productCode;

    private String productName;

    private String transportList;

    private String protocolCode;

    private String nodeType;

    private String productDesc;

    private String logUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss" ,timezone ="GMT+8")
    private Date createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss" ,timezone ="GMT+8")
    private Date updateTime;

    private Integer delFlag;

    private Integer productStatus;

    private Long createBy;
}
