package com.bytecub.mdm.cache.impl;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.mdm.cache.IJobLockCache;
import com.bytecub.plugin.redis.CacheTemplate;
import com.bytecub.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 *  * ByteCub.cn.
 *  * Copyright (c) 2020-2021 All Rights Reserved.
 *  * 
 *  * @author bytecub@163.com  songbin
 *  * @Date 2021/3/12  Exp $$
 *  
 */
@Slf4j
@Service
public class JobLockCacheImpl implements IJobLockCache {
    @Autowired
    CacheTemplate cacheTemplate;

    @Override
    public Boolean lock(String jobName, Integer seconds) {
        String key = this.buildKey(jobName);
        Date date = new Date();
        String data = DateUtil.getStringByFormat(date, DateUtil.newFormat);
        return cacheTemplate.setnx(key, data, seconds);
    }

    @Override
    public void unlock(String jobName) {
        String key = this.buildKey(jobName);
        cacheTemplate.remove(key);
    }

    @Override
    public Date getLock(String jobName) {
        try{
            String key = this.buildKey(jobName);
            String data = cacheTemplate.get(key);
            if(StringUtils.isEmpty(data)){
                return null;
            }
            return DateUtil.parseDateNewFormat(data);
        }catch (Exception e){
            log.warn("获取时间异常", e);
            return null;
        }

    }

    private final String buildKey(String jobName){
        return BCConstants.REDIS_KEY.JOB_LOCK + jobName;
    }
}
