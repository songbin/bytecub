package com.bytecub.plugin.es.domain;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: EsRange.java, v 0.1 2020-12-27  Exp $$
  */
@Data
public class EsRange<M,N> {
    M max;
    N min;
    String field;
}
