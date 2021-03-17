package com.bytecub.common.enums;

import lombok.Getter;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 设备回执消息状态(设备返回的状态归类)
  * @author bytecub@163.com  songbin
  * @version Id: DeviceReplyEnum.java, v 0.1 2021-01-23  Exp $$
  */
@Getter
public enum DeviceReplyEnum {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    UNKNOWN(202, "未知状态"),
    NORELY(203, "未收到回执");
    int code;
    String msg;

    DeviceReplyEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
