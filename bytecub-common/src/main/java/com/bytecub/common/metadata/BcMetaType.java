package com.bytecub.common.metadata;

import com.bytecub.plugin.es.config.enums.BcEsMetaType;
import lombok.Getter;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: BcMetaType.java, v 0.1 2020-12-11  Exp $$
  */
@Getter
public enum BcMetaType {
    STRING("text", BcEsMetaType.KEYWORD, "字符串|text", "text", "基本类型"),
    INTEGER("int", BcEsMetaType.INTEGER,"整型|int" , "num", "基本类型"),
    BOOLEAN("boolean", BcEsMetaType.BOOLEAN, "布尔类型|boolean", "boolean", "基本类型"),
    DATE("date", BcEsMetaType.DATE, "日期类型|date", "date", "基本类型"),
    FLOAT("float", BcEsMetaType.FLOAT, "单精度型浮点|float", "num", "基本类型"),
    DOUBLE("double", BcEsMetaType.DOUBLE, "双精度型浮点|double", "num", "基本类型"),
    LONG("long", BcEsMetaType.LONG, "长整型|long", "num", "基本类型"),
    STRUCT("struct", BcEsMetaType.OBJECT, "结构体|struct", "struct", "复合类型");
    String       code;
    BcEsMetaType esMetaType;
    String       msg;
    String type;
    String group;

    BcMetaType(String code, BcEsMetaType esMetaType, String msg, String type, String group) {
        this.code = code;
        this.esMetaType = esMetaType;
        this.msg = msg;
        this.type = type;
        this.group = group;
    }

    public static BcMetaType explain(String code){
        for(BcMetaType item:BcMetaType.values()){
            if(item.code.equals(code)){
                return item;
            }
        }
        return null;
    }
}
