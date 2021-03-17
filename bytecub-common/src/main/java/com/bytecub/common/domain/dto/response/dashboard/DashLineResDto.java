package com.bytecub.common.domain.dto.response.dashboard;

import lombok.Data;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: DashLineResDto.java, v 0.1 2021-01-27  Exp $$
  */
@Data
public class DashLineResDto {
    List<String> dates;
    List<Long> count;
}
