package com.bytecub.mdm.service;

import com.bytecub.common.domain.dto.request.device.DevCreateReqDto;
import com.bytecub.mdm.dao.po.RolePermissionPo;
import com.bytecub.mdm.dao.po.RolePo;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IRolePermissionService.java, v 0.1 2021-02-23  Exp $$
  */
public interface IRolePermissionService {
    /**
     *
     * @param list
     */
    void batchCreate(List<RolePermissionPo> list);
    List<RolePermissionPo> queryByRoleCode(String roleCode);
    void deleteByRoleCode(String roleCode);
}
