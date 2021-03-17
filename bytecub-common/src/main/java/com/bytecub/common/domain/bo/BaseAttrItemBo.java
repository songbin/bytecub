package com.bytecub.common.domain.bo;

import lombok.Data;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 单个属性/事件的attr对象
  * @author bytecub@163.com  songbin
  * @version Id: BaseAttrBo.java, v 0.1 2020-12-21  Exp $$
  */
@Data
public class BaseAttrItemBo {
    /**标识符*/
    String               identifier;
    /**数据类型*/
    String               dataType;
    /**boolean类型为false时显示的文案*/
    String              bool0;
    /**boolean类型为true时显示的文案*/
    String              bool1;
    Long                 length;
    String               unit;
    /**如果数据类型是结构体的话*/
    List<BaseAttrItemBo> data;
}
