package com.bytecub.common.metadata;

import lombok.Getter;

/**
 * 产品功能的类型
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProductFuncTypeEnum.java, v 0.1 2020-12-11  Exp $$
  */
@Getter
public enum ProductFuncTypeEnum {
    PROP("PROP", "属性"),
    EVENT("EVENT", "事件"),
    SERVICE("SERVICE", "服务");
    String type;
    String msg;

    ProductFuncTypeEnum(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static ProductFuncTypeEnum explain(String type){
        for(ProductFuncTypeEnum item:ProductFuncTypeEnum.values()){
            if(item.type.equals(type)){
                return item;
            }
        }
        return ProductFuncTypeEnum.PROP;
    }
}
