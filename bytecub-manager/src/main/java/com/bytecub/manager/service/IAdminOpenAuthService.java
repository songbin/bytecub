package com.bytecub.manager.service;

import com.bytecub.common.domain.dto.request.openauth.OpenAuthRequestDto;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IAdminOpenAuthService.java, v 0.1 2021-01-08  Exp $$
  */
public interface IAdminOpenAuthService {
    void create(OpenAuthRequestDto requestDto);
}
