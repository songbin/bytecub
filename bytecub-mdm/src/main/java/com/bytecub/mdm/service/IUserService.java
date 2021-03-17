package com.bytecub.mdm.service;

import com.bytecub.mdm.dao.po.UserPo;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IUserService.java, v 0.1 2020-12-14  Exp $$
  */
public interface IUserService {
    List<UserPo> queryUserByUnion(UserPo query);
    UserPo queryUserByName(String userName);
}
