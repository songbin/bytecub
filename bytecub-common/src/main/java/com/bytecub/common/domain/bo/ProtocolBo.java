package com.bytecub.common.domain.bo;

import lombok.Data;

import java.util.Date;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProtocolBo.java, v 0.1 2020-12-05  Exp $$
  */
@Data
public class ProtocolBo {
    private Long id;

    private String protocolCode;

    private String protocolName;

    private String protocolHandle;

    private String protocolFileUrl;

    private Integer protocolType;

    private String jarSign;

    private Date createTime;

    private Date updateTime;

    private Integer protocolStatus;

    private Integer delFlag;

}
