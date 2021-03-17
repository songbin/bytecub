package com.bytecub.mdm.service.impl;

import com.bytecub.common.biz.RedisKeyUtil;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.mdm.dao.dal.OpenAuthPoMapper;
import com.bytecub.mdm.dao.po.OpenAuthPo;
import com.bytecub.mdm.service.IOpenAuthService;
import com.bytecub.plugin.redis.CacheTemplate;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: OpenAuthServiceImpl.java, v 0.1 2021-01-08  Exp $$
  */
@Service
@Slf4j
public class OpenAuthServiceImpl implements IOpenAuthService {
    @Autowired OpenAuthPoMapper openAuthPoMapper;
    @Autowired CacheTemplate cacheTemplate;
    @Override public PageResDto<OpenAuthPo> searchByPage(PageReqDto searchPage) {
        OpenAuthPo query = new OpenAuthPo();
        query.setDelFlag(false);
        PageHelper.startPage(searchPage.getPageNo(), searchPage.getLimit());
        List<OpenAuthPo> list = openAuthPoMapper.select(query);
        for(OpenAuthPo item:list){
            item.setAppSecret("********");
        }
        PageInfo<OpenAuthPo> pageInfo = new PageInfo<>(list);
        PageResDto<OpenAuthPo> resultPage = PageResDto.genResult(searchPage.getPageNo(), searchPage.getLimit(), pageInfo.getTotal(), list, null);
        return resultPage;
    }

    @Override public OpenAuthPo selectByKey(String key) {
        String redisKey = RedisKeyUtil.buildAuthKey(key);
        OpenAuthPo po = cacheTemplate.get(redisKey, OpenAuthPo.class);
        if(null == po){
            OpenAuthPo query = new OpenAuthPo();
            query.setAppKey(key);
            po= openAuthPoMapper.selectOne(query);
            if(null != po){
                cacheTemplate.set(redisKey, po, BCConstants.REDIS_DEF.AUTH_INFO_EXPIRED);
            }
        }
        return po;
    }

    @Override public void insert(String key, String secret, String name, String desc) {
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(secret)){
            return;
        }
        OpenAuthPo record = new OpenAuthPo();
        record.setAppKey(key);
        record.setAppSecret(secret);
        record.setAppName(name);
        record.setAppDesc(desc);
        try{
            openAuthPoMapper.insertSelective(record);
        }catch (DataIntegrityViolationException e){
            throw BCGException.genException(BCErrorEnum.APP_UNIQUE_ERROR);
        }

    }

    @Override public void update(Long id, String key, String secret) {
        OpenAuthPo record = openAuthPoMapper.selectByPrimaryKey(id);
        if(null == record){
            return;
        }
        OpenAuthPo update = new OpenAuthPo();
        update.setAppKey(key);
        update.setAppSecret(secret);
        update.setId(id);
        openAuthPoMapper.updateByPrimaryKeySelective(update);
        String redisKey = RedisKeyUtil.buildAuthKey(key);
        cacheTemplate.remove(redisKey);
    }

    @Override public void delete(Long id) {
        OpenAuthPo record = openAuthPoMapper.selectByPrimaryKey(id);
        if(null == record){
            return;
        }
        OpenAuthPo update = new OpenAuthPo();
        update.setDelFlag(true);
        update.setAppKey(record.getAppKey() + System.currentTimeMillis());
        update.setId(id);
        openAuthPoMapper.updateByPrimaryKeySelective(update);
        String redisKey = RedisKeyUtil.buildAuthKey(record.getAppKey());
        cacheTemplate.remove(redisKey);
    }
}
