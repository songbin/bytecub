package com.bytecub.mdm.service.impl;

import com.bytecub.mdm.dao.dal.RolePermissionPoMapper;
import com.bytecub.mdm.dao.po.RolePermissionPo;
import com.bytecub.mdm.service.IRolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: RolePermissionServiceImpl.java, v 0.1 2021-02-23  Exp $$  
 */
@Slf4j
@Service
public class RolePermissionServiceImpl implements IRolePermissionService {
    @Autowired
    RolePermissionPoMapper mapper;

    @Override
    public void batchCreate(List<RolePermissionPo> list) {
        mapper.insertList(list);
    }

    @Override
    public List<RolePermissionPo> queryByRoleCode(String roleCode) {
        RolePermissionPo query = new RolePermissionPo();
        query.setRoleCode(roleCode);
        return mapper.select(query);
    }

    @Override
    public void deleteByRoleCode(String roleCode) {
        RolePermissionPo query = new RolePermissionPo();
        query.setRoleCode(roleCode);
        mapper.delete(query);
    }
}
