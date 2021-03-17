package com.bytecub.plugin.es.domain;

import com.bytecub.plugin.es.config.enums.KeyMatchTypeEnum;
import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 用来过滤非null和null的，哪些key要查 哪些key不需要查
  * @author bytecub@163.com  songbin
  * @version Id: SearchItem.java, v 0.1 2020-12-27  Exp $$
  */
@Data
public class SearchItem {
    String key;
    Object value;
    KeyMatchTypeEnum matchType;
}
