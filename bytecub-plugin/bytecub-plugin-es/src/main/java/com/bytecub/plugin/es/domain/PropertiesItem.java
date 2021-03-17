package com.bytecub.plugin.es.domain;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 创建模版时单独某个属性的配置
  * @author bytecub@163.com  songbin
  * @version Id: PropertiesItem.java, v 0.1 2021-01-13  Exp $$
  */
@Data
public class PropertiesItem {
    String type;
    String format;
    /**
     * not_analyzed=>不索引
     *
     * */
    String index;
}
