package com.bytecub.mdm.dao.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_product_func")
public class ProductFuncPo {
    @Id
    private Long id;

    private String productCode;

    private String funcName;

    private String identifier;

    private String funcType;

    private String dataType;

    private String dataDefine;

    private Integer wrType;

    private Integer funcStatus;

    private Integer delFlag = 0;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8" )
    private Date createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8" )
    private Date updateTime;

    private Long createBy;

    private String funcDesc;

    private String unit;

    private String unitName;
    private String attr;

    private String eventType;

    private Integer async;

    private String inputParam;

    private String outputParam;


}
