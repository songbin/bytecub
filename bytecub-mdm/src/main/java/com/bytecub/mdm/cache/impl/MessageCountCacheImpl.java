package com.bytecub.mdm.cache.impl;

import com.bytecub.common.domain.dto.response.dashboard.DashLineResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.mdm.cache.IMessageCountCache;
import com.bytecub.plugin.redis.CacheTemplate;
import com.bytecub.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import static com.bytecub.common.constants.BCConstants.REDIS_KEY.MESSAGE_DASH_LINE;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: MessageCountCacheImpl.java, v 0.1 2021-01-27  Exp $$  
 */
@Service
@Slf4j
public class MessageCountCacheImpl implements IMessageCountCache {
    @Autowired
    CacheTemplate cacheTemplate;

    @Override
    public Integer todayTotal() {
        String key = this.buildTodayKey();
        String value = cacheTemplate.get(key, String.class);
        return StringUtils.isEmpty(value) ? null : Integer.valueOf(value);
    }

    @Override
    public void  todayTotalIncr() {
        String key = this.buildTodayKey();
        try{
            Long ret = cacheTemplate.incr(key);
            if(ret < 5){
                cacheTemplate.expire(key, 60*60*25);
            }
        }catch (Exception e){
            log.warn("消息[{}]自增长异常", key, e);
        }

    }

    @Override
    public DashLineResDto dashLineRead() {
        String key = buildDashLineKey();
        return cacheTemplate.get(key, DashLineResDto.class);
    }

    @Override
    public void dashLineWrite(DashLineResDto resDto) {
        String key = buildDashLineKey();
        cacheTemplate.set(key, resDto);
    }

    private final String buildDashLineKey(){
        String today = DateUtil.getTodayString();
        return MESSAGE_DASH_LINE + today;
    }
    private String buildTodayKey() {
        String today = DateUtil.getTodayString();
        return BCConstants.REDIS_KEY.MESSAGE_TODAY + today;
    }
}
