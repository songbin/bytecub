package com.bytecub.mdm.cache;

import com.bytecub.common.domain.gateway.mq.DeviceActiveMqBo;
import com.bytecub.common.enums.BatchOpEnum;

import java.util.List;
import java.util.Map;

/**
 *  * ByteCub.cn.
 *  * Copyright (c) 2020-2021 All Rights Reserved.
 *  * 
 *  * @author bytecub@163.com  songbin
 *  * @Date 2021/3/12  Exp $$
 *  
 */
public interface IDeviceOfflineCache {
    void cacheWriter(DeviceActiveMqBo deviceActiveMqBo);
    DeviceActiveMqBo cacheReader(String deviceCode);
    List<DeviceActiveMqBo> cacheBatchReader(List<String> deviceCodes);
    Map<String, DeviceActiveMqBo> cacheMapReader(List<String> deviceCodes);
    /**在线设备总数*/
    Long activeCount();
    /**
     * 批量更改在线状态，用于网管设备上报设备状态
     * @param deviceCodes
     * @param batchOpEnum
     * @return
     * */
    void cacheBatchWriter(List<String> deviceCodes, BatchOpEnum batchOpEnum);
    /**
     * 移除过期下线的设备
     * */
    void removeExpire();


}
