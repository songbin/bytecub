package com.bytecub.common.domain.dto.request.device;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 属性读取
  * @author bytecub@163.com  songbin
  * @version Id: InvokeReqDto.java, v 0.1 2020-12-29  Exp $$
  */
@Data
public class PropertyGetReqDto {
    @NotNull(message = "设备编码不能为空")
    String     deviceCode;
    /**下发服务标识符 */
    @NotNull(message = "标识符不能为空")
    String     identifier;
    /**调用的时间*/
    Date   timestamp = new Date();
    /**超时时间，单位秒 默认10s*/
    Long timeout = 10L;
}
