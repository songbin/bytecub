package com.bytecub.common.enums;

import lombok.Getter;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: DeviceStateEnum.java, v 0.1 2021-02-01  Exp $$
  */
@Getter
public enum DeviceStateEnum {
    TOTAL(1, "全部设备"),
    ACTIVE(2, "在线设备");
    int type;
    String msg;

    DeviceStateEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static DeviceStateEnum explain(int type){
        for(DeviceStateEnum stateEnum: DeviceStateEnum.values()){
            if(stateEnum.type == type){
                return stateEnum;
            }
        }
        return null;
    }
}
