package com.bytecub.manager.service.impl;

import com.bytecub.common.domain.dto.request.openauth.OpenAuthRequestDto;
import com.bytecub.manager.service.IAdminOpenAuthService;
import com.bytecub.mdm.service.IOpenAuthService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: AdminOpenAuthServiceImpl.java, v 0.1 2021-01-08  Exp $$
  */
@Service
@Slf4j
public class AdminOpenAuthServiceImpl implements IAdminOpenAuthService {
    @Autowired IOpenAuthService openAuthService;
    @Override public void create(OpenAuthRequestDto requestDto) {
        String secret = RandomString.make(10).toLowerCase();
        openAuthService.insert(requestDto.getAppKey(), secret, requestDto.getAppName(), requestDto.getAppDesc());
    }
}
