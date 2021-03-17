package com.bytecub.mdm.cache.impl;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.common.domain.gateway.mq.DeviceActiveMqBo;
import com.bytecub.mdm.cache.IDeviceCache;
import com.bytecub.mdm.cache.IDeviceOfflineCache;
import com.bytecub.plugin.redis.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.bytecub.common.constants.BCConstants.REDIS_DEF.DEVICE_INFO_EXPIRED;
import static com.bytecub.common.constants.BCConstants.REDIS_KEY.DEV_INFO;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: DeviceCacheImpl.java, v 0.1 2021-01-28  Exp $$  
 */
@Slf4j
@Service
public class DeviceCacheImpl implements IDeviceCache {
    @Autowired
    CacheTemplate cacheTemplate;
    @Autowired
    IDeviceOfflineCache deviceOfflineCache;
    @Override
    public DevicePageResDto deviceReader(String deviceCode) {
        String key = this.buildDevKey(deviceCode);

        DevicePageResDto devicePageResDto = cacheTemplate.get(key, DevicePageResDto.class);
        if(null == devicePageResDto){
            return null;
        }
        DeviceActiveMqBo activeMqBo = deviceOfflineCache.cacheReader(deviceCode);
        if(null != activeMqBo){
            devicePageResDto.setDevPort(activeMqBo.getPort());
            devicePageResDto.setDevHost(activeMqBo.getHost());
            devicePageResDto.setActiveStatus(activeMqBo.getActive() ? 1 : 0);
            devicePageResDto.setLastOnlineTime(activeMqBo.getTimestamp());
        }
        return devicePageResDto;
    }

    @Override
    public void deviceWriter(String deviceCode, DevicePageResDto data) {
        String key = this.buildDevKey(deviceCode);
        cacheTemplate.set(key, data, DEVICE_INFO_EXPIRED);
    }

    @Override
    public void remove(String deviceCode) {
        String key = this.buildDevKey(deviceCode);
        cacheTemplate.remove(key);
    }

    private final String buildDevKey(String deviceCode){
        return  DEV_INFO + deviceCode;
    }
}
