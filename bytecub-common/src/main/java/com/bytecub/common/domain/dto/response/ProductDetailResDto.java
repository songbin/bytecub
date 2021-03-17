package com.bytecub.common.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProductDetailResDto.java, v 0.1 2020-12-16  Exp $$
  */
@Data
public class ProductDetailResDto {
    private Long id;

    private String productCode;

    private String productName;

    private String transportList;

    private String protocolCode;

    private String nodeType;

    private String nodeName;

    private String productDesc;

    private String logUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss" ,timezone ="GMT+8")
    private Date createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8" )
    private Date updateTime;

    private Integer delFlag;

    private Integer productStatus;

    private Long createBy;

    /**协议开始字段*/
    private String protocolName;

    private String protocolFileUrl;

    private Integer protocolType;

    private String jarSign;
    private Integer protocolStatus;
    /**协议字段 ends*/

}
