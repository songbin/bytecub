package com.bytecub.mdm.service;

import com.bytecub.mdm.dao.po.RolePo;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IRoleService.java, v 0.1 2021-02-23  Exp $$
  */
public interface IRoleService {
    void create(RolePo rolePo);
    void update(RolePo rolePo);
    void delete(RolePo rolePo);
}
