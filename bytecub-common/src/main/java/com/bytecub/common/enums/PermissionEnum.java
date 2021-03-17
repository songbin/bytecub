package com.bytecub.common.enums;

import lombok.Getter;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: PermissionEnum.java, v 0.1 2021-02-23  Exp $$
  */
@Getter
public enum PermissionEnum {
    PRODUCT_CREATE("sys:product:create", "创建产品", "产品"),
    PRODUCT_UPDATE("sys:product:update", "编辑产品", "产品"),
    PRODUCT_DELETE("sys:product:delete", "删除产品", "产品"),
    MODEL_RELEASE("sys:model:release", "模型发布", "物模型"),
    MODEL_CREATE("sys:model:add", "模型添加", "物模型"),
    MODEL_UPDATE("sys:model:update", "模型编辑", "物模型"),
    MODEL_DELETE("sys:model:delete", "模型删除", "物模型"),
    DEVICE_ADD_BATCH("system:device:batchadd", "批量新增设备", "设备"),
    DEVICE_IMPORT("system:device:import", "导入设备", "设备"),
    DEVICE_ADD("system:device:add", "新增设备", "设备"),
    DEVICE_UPDATE("system:device:update", "编辑设备", "设备"),
    DEVICE_GW_MAP("system:device:gw:map", "关联网关", "设备"),
    DEVICE_STATUS("system:device:status", "状态变更", "设备"),

    OPEN_ADD("system:open:add", "新增应用", "开放平台"),
    OPEN_UPDATE("system:open:update", "编辑应用", "开放平台"),
    OPEN_DELETE("system:open:delete", "删除应用", "开放平台"),
    OPEN_SECRET("system:open:secret", "查看密钥", "开放平台");
    String code;
    String msg;
    String group;

    PermissionEnum(String code, String msg, String group) {
        this.code = code;
        this.msg = msg;
        this.group = group;
    }
}
