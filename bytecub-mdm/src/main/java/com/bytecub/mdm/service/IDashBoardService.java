package com.bytecub.mdm.service;

import com.bytecub.common.domain.dto.response.dashboard.DashBoardResDto;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IDashBoardService.java, v 0.1 2021-01-27  Exp $$
  */
public interface IDashBoardService {
    /**
     * 首页数据
     * @return 
     * */
    DashBoardResDto index();
}
