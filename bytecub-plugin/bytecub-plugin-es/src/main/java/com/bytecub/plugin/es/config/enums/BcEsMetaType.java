package com.bytecub.plugin.es.config.enums;

import lombok.Getter;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: BcMetaType.java, v 0.1 2020-12-07  Exp $$
  */
@Getter
public enum BcEsMetaType {
    KEYWORD("keyword", ""),
    TEXT("text", ""),
    STRING("keyword", ""),
    LONG("long",""),
    DOUBLE("double", ""),
    FLOAT("float", ""),
    DATE("date", ""),
    BOOLEAN("boolean",""),
    INTEGER("integer",""),
    GEOPOINT("geo_point ", ""),
    OBJECT("object", ""),
    UNKNOWN("unknown ", "");
    String type;
    String msg;

    BcEsMetaType(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
