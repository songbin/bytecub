package com.bytecub.common.metadata;

import lombok.Getter;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: EventTypeEnum.java, v 0.1 2020-12-18  Exp $$
  */
@Getter
public enum EventTypeEnum {
    INFO("INFO", "通知"),
    WARN("WARN", "警告"),
    FAULT("FAULT", "故障");
    String code;
    String msg;

    EventTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static EventTypeEnum explain(String code){
        for(EventTypeEnum item:EventTypeEnum.values()){
            if(item.code.equals(code)){
                return item;
            }
        }
        return null;
    }
}
