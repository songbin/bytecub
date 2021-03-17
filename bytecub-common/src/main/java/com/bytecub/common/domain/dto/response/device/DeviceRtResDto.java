package com.bytecub.common.domain.dto.response.device;

import com.bytecub.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: DeviceRtResDto.java, v 0.1 2020-12-24  Exp $$
  */
@Data
public class DeviceRtResDto {
    String propName;
    String identifier;
    Object value = "/";
    String unit;
    String unitName;
    String dataType;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    Object   arrivedTime;
}
