package com.bytecub.common.enums;

import lombok.Getter;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: BCErrorEnum.java, v 0.1 2020-12-04  Exp $$
  */
@Getter
public enum BCErrorEnum {
    NO_READY_YET(-1, "系统尚未准备好！"),
    SUCCESS(200, "成功"),
    FAIL(4000, "失败"),
    DEFAULT_PASS(3999, "当前账号是默认密码，请修改密码后重新登陆"),
    INVALID_PARAM(4001, "参数非法"),
    INNER_EXCEPTION(4002, "内部异常"),
    INVALID_TOKEN(4003, "您的登录用户身份已过期！"),
    INVALID_USER(4004, "用户信息不存在"),
    INVALID_ROLE(4005, "用户没有操作该菜单的权限"),
    LOGIN_FAILER(4006, "登陆失败请联系管理员"),
    LACK_PARAM_EXCEPTION(4007, "数据传递缺少参数，请稍后再试"),
    LOGIN_VALID_USERNAMEPASSWD(4008, "用户名或者密码错误"),
    INVALID_CODE(4009, "验证码非法"),
    ES_SEARCH_EXCEPTION(4010, "ES查询异常"),
    ES_CREATE_EXCEPTION(4011, "ES创建资源异常"),
    IDENTIFIER_UNIQUE_ERROR(4012, "标识符已经存在"),
    INVALID_PROTOCOL(4013, "协议非法"),
    CODE_UNIQUE_ERROR(4014, "编码已经存在"),
    APP_UNIQUE_ERROR(4015, "APP已经存在"),
    INVALID_SIGN(4016, "签名非法！"),
    APP_NO_EXIST(4017, "应用非法！"),
    PARSE_MSG_EXCEPTION(4018, "解析协议异常！"),
    DATA_EMPTY(4019, "空数据！"),
    RESOURCE_NOT_EXISTS(4020, "资源不存在！"),
    TIMEOUT(4021, "响应超时！"),
    INVALID_USER_APP(40031, "用户信息不存在"),
    INVALID_MQTT_USER(100020, "内部mqtt服务用户异常"),
    DECODE_PROTOCOL_EXCEPTION(100021, "解析协议异常"),
    MQTT_TOPIC_INVALID(100022, "MQTT订阅topic格式非法"),
    RESOURCE_CREATE_FAIL(100022, "创建资源失败");

    int code;
    String msg;

    BCErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
