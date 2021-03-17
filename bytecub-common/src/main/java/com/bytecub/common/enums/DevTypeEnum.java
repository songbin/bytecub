package com.bytecub.common.enums;

import lombok.Getter;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: DevTypeEnum.java, v 0.1 2020-12-09  Exp $$
  */
@Getter
public enum DevTypeEnum {

    GATEWAY("GATEWAY", "网关设备"),
    SUB("SUB", "网关子设备"),
    DIRECT("DIRECT", "直连设备");
    String type;
    String msg;

    DevTypeEnum(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static DevTypeEnum explain(String type){
        for(DevTypeEnum item:DevTypeEnum.values()){
            if(item.type.equals(type)){
                return item;
            }
        }
        return null;
    }
}
