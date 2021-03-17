package com.bytecub.mdm.cache.impl;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.mdm.cache.IPropertyGetCache;
import com.bytecub.plugin.redis.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  * 设备属性读取相关缓存  * @author bytecub@163.com songbin
 *  * @version Id: PropertyGetCacheImpl.java, v 0.1 2021-01-26  Exp $$  
 */
@Service
@Slf4j
public class PropertyGetCacheImpl implements IPropertyGetCache {
    @Autowired
    CacheTemplate cacheTemplate;

    @Override
    public void propGetValueWrite(String messageId, Object value) {
        if (null == value) {
            return;
        }
        cacheTemplate.set(buildPropGetKey(messageId), value, BCConstants.REDIS_DEF.GENERAL_EXPIRED);
    }

    @Override
    public <T> T propGetReader(String messageId, Class<T> tClass) {
        return cacheTemplate.get(this.buildPropGetKey(messageId), tClass);
    }

    @Override
    public Map<String, Object> propGetReader(String messageId) {
        return cacheTemplate.get(this.buildPropGetKey(messageId), HashMap.class);
    }

    private final String buildPropGetKey(String messageId) {
        return BCConstants.REDIS_KEY.PROPERTY_GET + messageId;
    }
}
