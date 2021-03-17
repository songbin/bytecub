package com.bytecub.mdm.service;

import java.util.Date;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 数据统计用
  * @author bytecub@163.com  songbin
  * @version Id: IStatisticsService.java, v 0.1 2021-01-27  Exp $$
  */
public interface IStatisticsService {
    /**
     * 按天查询消息总数
     * @param date
     * @return
     * */
    Long countMsgTotalByDay(Date date);
}
