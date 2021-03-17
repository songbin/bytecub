package com.bytecub.mdm.cache.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.mdm.cache.IProductFuncCache;
import com.bytecub.mdm.dao.po.ProductFuncPo;
import com.bytecub.plugin.redis.CacheTemplate;
import com.bytecub.utils.JSONProvider;

import lombok.extern.slf4j.Slf4j;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: ProductFuncCacheImpl.java, v 0.1 2021-01-26  Exp $$  
 */
@Slf4j
@Service
public class ProductFuncCacheImpl implements IProductFuncCache {
    @Autowired
    CacheTemplate cacheTemplate;

    @Override
    public void remove(String productCode, String funcType) {
        String key = this.buildModelKey(productCode, funcType);
        cacheTemplate.remove(key);
    }

    @Override
    public void removeField(String productCode, String funcType, String identifier) {
        String key = this.buildModelKey(productCode, funcType);
        cacheTemplate.removeHashMap(key, identifier);
    }

    @Override
    public void removeKey(String productCode, String funcType) {
        String key = this.buildModelKey(productCode, funcType);
        cacheTemplate.remove(key);
    }

    @Override
    public void setProperties(String productCode, String funcType, List<ProductFuncPo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        String key = this.buildModelKey(productCode, funcType);
        for (ProductFuncPo item : list) {
            if (null == item) {
                continue;
            }
            if (1 == item.getDelFlag()) {
                continue;
            }
            String value = JSONProvider.toJSONString(item);
            cacheTemplate.addHashMapTime(key, item.getIdentifier(), value, BCConstants.REDIS_DEF.GENERAL_EXPIRED);
        }
    }

    @Override
    public void setProperty(String productCode, String funcType, ProductFuncPo po) {
        if (null == po) {
            return;
        }
        if (1 == po.getDelFlag()) {
            return;
        }
        String key = this.buildModelKey(productCode, funcType);
        cacheTemplate.addHashMapTime(key, po.getIdentifier(), JSONProvider.toJSONString(po),
            BCConstants.REDIS_DEF.GENERAL_EXPIRED);
    }

    @Override
    public ProductFuncPo getProperty(String productCode, String funcType, String identifier, Integer funcStatus) {
        String key = this.buildModelKey(productCode, funcType);
        String value = cacheTemplate.getHashMap(key, identifier);
        ProductFuncPo po = JSONProvider.parseObject(value, ProductFuncPo.class);
        if (null == po) {
            return null;
        }
        if (null != funcStatus) {
            return funcStatus.equals(po.getFuncStatus()) ? po : null;
        }
        return po;
    }

    @Override
    public Map<String, ProductFuncPo> getProperties(String productCode, String funcType, Integer funcStatus) {
        Map<String, ProductFuncPo> resultMap = new HashMap<>();
        String key = this.buildModelKey(productCode, funcType);
        Map<String, String> map = cacheTemplate.getHashMapAll(key);
        if (CollectionUtils.isEmpty(map)) {
            return resultMap;
        }
        map.forEach((filed, value) -> {
            ProductFuncPo po = JSONProvider.parseObject(value, ProductFuncPo.class);
            if (null != po) {
                if (null != funcStatus) {
                    if (funcStatus.equals(po.getFuncStatus())) {
                        resultMap.put(filed, po);
                    }
                } else {
                    resultMap.put(filed, po);
                }
            }
        });
        return resultMap;
    }

    @Override
    public List<ProductFuncPo> getListProperties(String productCode, String funcType, Integer funcStatus) {
        List<ProductFuncPo> resultList = new ArrayList<>();
        String key = this.buildModelKey(productCode, funcType);
        Map<String, String> map = cacheTemplate.getHashMapAll(key);
        if (CollectionUtils.isEmpty(map)) {
            return resultList;
        }
        map.forEach((filed, value) -> {
            ProductFuncPo po = JSONProvider.parseObject(value, ProductFuncPo.class);
            if (null != po) {
                if (null != funcStatus) {
                    if (funcStatus.equals(po.getFuncStatus())) {
                        resultList.add(po);
                    }
                } else {
                    resultList.add(po);
                }
            }
        });
        return resultList;
    }

    private final String buildModelKey(String productCode, String funcType) {
        return BCConstants.REDIS_KEY.PRODUCT_FUNC + productCode + ":" + funcType;
    }

}
