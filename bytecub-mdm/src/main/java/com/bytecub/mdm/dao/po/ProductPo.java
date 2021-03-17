package com.bytecub.mdm.dao.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_product")
public class ProductPo {
    @Id
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
