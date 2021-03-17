package com.bytecub.common.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: PropCreateReqDto.java, v 0.1 2020-12-16  Exp $$
  */
@Data
public class ProductFuncItemResDto implements Serializable {

    private static final long serialVersionUID = -5133765300360470540L;
    @NotEmpty(  message = "产品编码不能为空")
    @Length(max = 20, message = "产品编码长度最大20")
    String productCode;
    @NotEmpty(message = "属性名不能为空")
    @Length(max = 20, message = "属性名长度最大20")
    String propName;
    @Length(max = 20, message = "标识符长度最大20")
    String identifier;
    @NotEmpty(message = "数据类型不能为空")
    String dataType;
    String unit;
    @NotEmpty(message = "请输入读写类型")
    Integer wrType;
    @NotEmpty(message = "请选择读写类型")
    String propDesc;
    Integer funcStatus;
    String attr;
    String length;
    String bool0;
    String bool1;
    String rangeMax;
    String rangeMin;
    String funcType;
    String              eventType;
    Map<String, Object> attrMap;
    List<ProductFuncItemResDto> data;
    String deviceCode;
    Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8" )
    Date createTime;
    Integer async;
    String inputParam;
    String outputParam;
}
