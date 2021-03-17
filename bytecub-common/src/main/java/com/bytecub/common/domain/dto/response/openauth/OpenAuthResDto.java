package com.bytecub.common.domain.dto.response.openauth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: DevicePageResDto.java, v 0.1 2020-12-23  Exp $$
  */
@Data
public class OpenAuthResDto {
    private Long id;
    private String appKey;
    private String appName;
    private String appSecret;
    private String appDesc;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date    createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;


}
