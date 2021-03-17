package com.bytecub.mdm.cache;

import com.bytecub.common.domain.dto.response.dashboard.DashLineResDto;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IMessageCoutCache.java, v 0.1 2021-01-27  Exp $$
  */
public interface IMessageCountCache {
    /**
     * 当日实时消息总数
     * @return
     * */
    Integer todayTotal();
    /**
     * 当日实时消息总数自增
     * @return
     * */
    void todayTotalIncr();
    /**
     * 读取首页面板近七日数据线
     * @return
     * */
    DashLineResDto dashLineRead();
    /**
     * 写入首页面板近七日数据线
     * @param resDto
     * @return
     * */
    void dashLineWrite(DashLineResDto resDto);
}
