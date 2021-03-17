package com.bytecub.mdm.service.impl;

import com.bytecub.mdm.dao.dal.UserPoMapper;
import com.bytecub.mdm.dao.po.UserPo;
import com.bytecub.mdm.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IUserServiceImpl.java, v 0.1 2020-12-14  Exp $$
  */
@Service
@Slf4j
public class IUserServiceImpl implements IUserService {
    @Autowired
    UserPoMapper userPoMapper;

    @Override public List<UserPo> queryUserByUnion(UserPo query) {
        List<UserPo> result = new ArrayList<>();
        try{
            result = userPoMapper.selectByUnion(query);
        }catch (Exception e){
            log.warn("", e);
        }
        return result;
    }

    @Override
    public UserPo queryUserByName(String userName) {
        UserPo query = new UserPo();
        query.setUserName(userName);
        return userPoMapper.selectOne(query);
    }
}
