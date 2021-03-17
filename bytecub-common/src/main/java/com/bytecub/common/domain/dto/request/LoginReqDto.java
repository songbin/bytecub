package com.bytecub.common.domain.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: LoginReqDto.java, v 0.1 2020-12-14  Exp $$
  */
@Data
public class LoginReqDto implements Serializable {
    private static final long serialVersionUID = 2103261624259751987L;
    @Size(min = 1, max = 20, message = "最大长度20")
    String userName;
    @NotNull(message = "密码不能为空")
    String password;
    /**图形验证码内容*/
    String capture;
    /**图形验证码序列号*/
    String captureNo;
}
