package com.bytecub.common.domain.bo;

import lombok.Data;

import java.util.Date;

@Data
public class ProductBo {
    private Long id;

    private String productCode;

    private String productName;

    private String transportList;

    private String protocolCode;

    private String nodeType;

    private String productDesc;

    private String logUrl;

    private Date createTime;

    private Date updateTime;

    private Integer delFlag;

    private Integer productStatus;

    private Long createBy;


}