package com.bytecub.mdm.core;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: BaseDao.java, v 0.1 2021-01-08  Exp $$
  */
public interface BaseDao<T> extends Mapper<T>, MySqlMapper<T> {
}
