package com.bytecub.mdm.cache;

import com.bytecub.common.domain.dto.response.device.DevicePageResDto;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IDeviceCache.java, v 0.1 2021-01-28  Exp $$
  */
public interface IDeviceCache {
    /**根据设备编码读取设备缓存信息
     * @param deviceCode
     * @return
     * */
    DevicePageResDto deviceReader(String deviceCode);
    /**
     * 根据设备编码写入设备缓存信息
     * @param deviceCode
     * @param data
     * */
    void deviceWriter(String deviceCode, DevicePageResDto data);
    /**
     * 移除缓存
     * @param deviceCode
     * */
    void remove(String deviceCode);
}
