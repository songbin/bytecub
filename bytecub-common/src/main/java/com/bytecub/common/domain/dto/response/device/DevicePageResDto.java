package com.bytecub.common.domain.dto.response.device;

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
public class DevicePageResDto {
    private Long id;

    private String deviceCode;

    private String deviceName;

    private String gwDevCode;

    private String productCode;

    private Integer delFlag;

    private Integer enableStatus;

    private Integer activeStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date    lastOnlineTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date    createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    String deviceSecret;

    String firmwareVersion;

    String devHost;
    Integer devPort;

    /**下面是产品表**/
    String productName;
    String nodeType;
}
