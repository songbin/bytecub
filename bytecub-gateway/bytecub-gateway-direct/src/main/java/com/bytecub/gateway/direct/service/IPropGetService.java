package com.bytecub.gateway.direct.service;

import com.bytecub.common.domain.gateway.direct.response.PropertyGetResponse;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 读取设备属性
  * @author bytecub@163.com  songbin
  * @version Id: IPropGetService.java, v 0.1 2021-01-26  Exp $$
  */
public interface IPropGetService {
    /**
     * 服务端实时服务设备属性
     * @param deviceCode
     * @param identifier
     * @param timeout 超时时间，单位秒
     * @return 如果value是null的话 说明是超时为收到设备上报数据
     * */
    PropertyGetResponse fetchProperty(String deviceCode, String identifier, Long timeout);
}
