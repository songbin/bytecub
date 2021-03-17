package com.bytecub.common.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: PageDto.java, v 0.1 2020-12-15  Exp $$
  */
@Data
public class PageReqDto<T> implements Serializable {

    private static final long serialVersionUID = -3214634852095029897L;
    private int pageNo = 1;
    private int          limit = 20;
    private              T paramData;

}

