package com.bytecub.common.domain.dto.request;

import com.bytecub.common.metadata.ProductFuncTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: PropCreateReqDto.java, v 0.1 2020-12-16  Exp $$
  */
@Data
public class PropCreateReqDto implements Serializable {
    private static final long serialVersionUID = 8868037163599149959L;
    @NotNull(  message = "产品编码不能为空")
    @Size(max = 20, message = "产品编码长度最大20")
    String productCode;
    @NotNull(message = "属性名不能为空")
    @Size(max = 20, message = "属性名长度最大20")
    String propName;
    @Size(max = 20, message = "标识符长度最大20")
    @NotNull(  message = "标识符不能为空")
    String identifier;
    @NotNull(message = "数据类型不能为空")
    String dataType;
    String unit;
    Integer wrType;
    String propDesc;
    String attr;
    Integer async;
    /**功能类型ProductFuncTypeEnum*/
    String funcType;
    /**事件类型EventTypeEnum*/
    String eventType;
    String inputParam;
    String outputParam;
    Long id;
}
