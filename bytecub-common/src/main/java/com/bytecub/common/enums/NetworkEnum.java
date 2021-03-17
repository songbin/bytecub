package com.bytecub.common.enums;

import lombok.Getter;

/**传输协议类型枚举
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: NetworkEnum.java, v 0.1 2020-12-09  Exp $$
  */
@Getter
public enum NetworkEnum {
    MQTT("MQTT", "MQTT协议");
    String type;
    String msg;

    NetworkEnum(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static NetworkEnum explain(String type){
        for(NetworkEnum item:NetworkEnum.values()){
            if(item.type.equals(type)){
                return item;
            }
        }
        return null;
    }
}
