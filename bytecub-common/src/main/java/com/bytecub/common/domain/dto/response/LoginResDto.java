package com.bytecub.common.domain.dto.response;

import lombok.Data;

import java.io.Serializable;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: LoginReqDto.java, v 0.1 2020-12-14  Exp $$
  */
@Data
public class LoginResDto implements Serializable {
    private static final long serialVersionUID = 7924126418546482089L;
    String token;
    String userNick;
    String userName;
    Integer roleType;
    String  roleTypeName;
}
