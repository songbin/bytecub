package com.bytecub.mdm.cache;

import com.bytecub.mdm.dao.po.ProductFuncPo;

import java.util.List;
import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IProductFuncCache.java, v 0.1 2021-01-26  Exp $$
  */
public interface IProductFuncCache {
    /**
     * 从缓存删除
     * @param productCode
     * @param funcType
     * @return
     * */
    void remove(String productCode, String funcType);
    /**
     * 从缓存删除
     * @param productCode
     * @param funcType
     * @param identifier
     * @return
     * */
    void removeField(String productCode, String funcType, String identifier);
    /**
     * 从缓存删除
     * @param productCode
     * @param funcType
     * @return
     * */
    void removeKey(String productCode, String funcType);
    /**
     * 将一个产品下指定类型物模型存储到redis中
     * @param productCode
     * @param list
     * @param funcType
     * @return
     * */
    void setProperties(String productCode, String funcType, List<ProductFuncPo> list);
    /**
     * 设置一个单独的
     * @param productCode
     * @param po
     * @param funcType
     * @return
     * */
    void setProperty(String productCode, String funcType, ProductFuncPo po);
    /**
     * 获取单个物模型标识符值
     * @param productCode
     * @param funcType
     * @param identifier
     * @param funcStatus
     * @return
     * */
    ProductFuncPo getProperty(String productCode, String funcType, String identifier, Integer funcStatus);
    /**
     * 获取产品指定功能下物模型
     * @param productCode
     * @param funcType
     * @param funcStatus
     * @return key=>identifier
     * */
    Map<String, ProductFuncPo> getProperties(String productCode, String funcType, Integer funcStatus);
    /**
     * 获取产品指定功能下物模型
     * @param productCode
     * @param funcType
     * @param funcStatus 发布状态 null则查询所有
     * @return
     * */
    List<ProductFuncPo> getListProperties(String productCode, String funcType, Integer funcStatus);
}
