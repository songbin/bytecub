package com.bytecub.mdm.service;

import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.response.ProductResDto;
import com.bytecub.mdm.dao.po.OpenAuthPo;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IOpenAuthService.java, v 0.1 2021-01-08  Exp $$
  */
public interface IOpenAuthService {
    PageResDto<OpenAuthPo> searchByPage(PageReqDto searchPage);
    OpenAuthPo selectByKey(String key);
    void insert(String key, String secret, String name, String desc);
    void update(Long id, String key, String secret );
    void delete(Long id);
}
