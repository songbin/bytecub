package com.bytecub.common.domain.gateway.direct.response;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: PropertySetResponse.java, v 0.1 2021-01-26  Exp $$
  */
@Data
public class PropertyGetResponse {
    String     deviceCode;
    /**下发服务标识符 */
    String     identifier;
    /**属性值*/
    Object value;
}
