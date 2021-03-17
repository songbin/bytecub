package com.bytecub.common.domain.dto.request.device;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 属性设置
  * @author bytecub@163.com  songbin
  * @version Id: InvokeReqDto.java, v 0.1 2020-12-29  Exp $$
  */
@Data
public class PropertySetReqDto {
    @NotNull(message = "设备编码不能为空")
    String     deviceCode;
    /**下发服务标识符 */
    @NotNull(message = "标识符不能为空")
    String     identifier;
    /**消息体*/
    JSONObject command;
    /**调用的时间*/
    Date   timestamp = new Date();
}
