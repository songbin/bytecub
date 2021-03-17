package com.bytecub.mdm.service.impl;

import com.bytecub.mdm.dao.dal.RolePoMapper;
import com.bytecub.mdm.dao.po.RolePo;
import com.bytecub.mdm.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: RoleServiceImpl.java, v 0.1 2021-02-23  Exp $$  
 */
@Slf4j
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RolePoMapper rolePoMapper;

    @Override
    public void create(RolePo rolePo) {
        rolePoMapper.insertSelective(rolePo);
    }

    @Override
    public void update(RolePo rolePo) {
        rolePoMapper.updateByPrimaryKeySelective(rolePo);
    }

    @Override
    public void delete(RolePo rolePo) {
        rolePoMapper.delete(rolePo);
    }
}
