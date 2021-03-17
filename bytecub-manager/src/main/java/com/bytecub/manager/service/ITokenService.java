package com.bytecub.manager.service;

import com.bytecub.common.domain.dto.response.CaptchResDto;
import com.bytecub.manager.common.domain.security.SecurityUserBo;
import com.bytecub.mdm.dao.po.UserPo;

import java.io.IOException;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ITokenService.java, v 0.1 2020-12-14  Exp $$
  */
public interface ITokenService {
    String genToken(UserPo userPo);
    UserPo getDataFromToken(String token);
    SecurityUserBo queryByToken(String token);
    void removeToken(String token);
    CaptchResDto getCaptchaCode() throws IOException ;
    boolean verifyCaptcha(String token, String text);
}
