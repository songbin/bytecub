package com.bytecub.mdm.cache.impl;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.mdm.cache.IPropSetLockCache;
import com.bytecub.mdm.cache.IServiceInvokeLockCache;
import com.bytecub.plugin.redis.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  * ByteCub.cn.
 *  * Copyright (c) 2020-2021 All Rights Reserved.
 *  * 
 *  * @author bytecub@163.com  songbin
 *  * @Date 2021/3/15  Exp $$
 *  
 */
@Service
@Slf4j
public class ServiceInvokeLockCacheImpl implements IServiceInvokeLockCache {
    @Autowired
    CacheTemplate cacheTemplate;

    @Override
    public Boolean lock(String messageId) {
        String key = this.buildKey(messageId);
        Long ret = cacheTemplate.incr(key);
        cacheTemplate.expire(key, 3600*10);
        return ret.longValue() > 1 ? false : true;
    }

    private final String buildKey(String messageId){
        return BCConstants.REDIS_KEY.SERVICE_INVOKE_LOCK + messageId;
    }
}
