package com.bytecub.mdm.service.impl;


import com.bytecub.common.biz.RedisKeyUtil;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.product.ProductQueryReqDto;
import com.bytecub.common.domain.dto.response.ProductDetailResDto;
import com.bytecub.common.domain.dto.response.ProductResDto;
import com.bytecub.mdm.convert.po2dto.ProductPoConvert;
import com.bytecub.mdm.dao.dal.ProductPoMapper;
import com.bytecub.mdm.dao.po.ProductPo;
import com.bytecub.mdm.service.IProductService;
import com.bytecub.plugin.redis.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProductServiceImpl.java, v 0.1 2020-12-11  Exp $$
  */
@Service
@Slf4j
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductPoMapper productPoMapper;
    @Autowired CacheTemplate cacheTemplate;

    @Override public List<ProductPo> listAllValid() {
        return productPoMapper.listAll(1,0);
    }

    @Override public ProductPo queryByCode(String code) {
        String key = RedisKeyUtil.buildProductInfoKey(code);
        ProductPo record = cacheTemplate.get(key, ProductPo.class);
        if(null != record){
            return record;
        }
        ProductPo query = new ProductPo();
        query.setProductStatus(1);
        query.setDelFlag(0);
        query.setProductCode(code);
        List<ProductPo> result = productPoMapper.selectByUnion(query);

        if(!CollectionUtils.isEmpty(result)){
            cacheTemplate.set(key, result.get(0), BCConstants.REDIS_DEF.PRODUCT_INFO_EXPIRED);
        }
        return CollectionUtils.isEmpty(result) ? null : result.get(0);
    }

    @Override
    public PageResDto<ProductResDto> searchByName(PageReqDto searchPage) {
        String name = (String)(searchPage.getParamData());
        name = StringUtils.isEmpty(name) ? null : name;
        int startId = (searchPage.getPageNo() - 1) * searchPage.getLimit();
        List<ProductPo>  list = productPoMapper.searchByName(name, startId, searchPage.getLimit());
        long count = productPoMapper.countByName(name);
        List<ProductResDto> resultList = ProductPoConvert.list(list);
        PageResDto<ProductResDto> resultPage = PageResDto.genResult(searchPage.getPageNo(), searchPage.getLimit(), count, resultList, null);
        return resultPage;
    }

    @Override
    public void saveItem(ProductPo record) {
        productPoMapper.insertSelective(record);
    }

    @Override
    public ProductDetailResDto detail(String productCode) {
        return productPoMapper.selectByCode(productCode);
    }

    @Override public void updateItem(ProductPo record) {
        ProductPo query = productPoMapper.selectByPrimaryKey(record.getId());
        if(null == query){
            return ;
        }
        String key = RedisKeyUtil.buildProductInfoKey(query.getProductCode());
        cacheTemplate.remove(key);
        productPoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<ProductResDto> query(ProductQueryReqDto queryReqDto) {
        ProductPo query = new ProductPo();
        query.setProductCode(query.getProductCode());
        if(!StringUtils.isEmpty(queryReqDto.getProductName())){
            query.setProductName(queryReqDto.getProductName());
        }
        query.setNodeType(queryReqDto.getNodeType());
        List<ProductPo> list = productPoMapper.select(query);
        return ProductPoConvert.list(list);
    }

    @Override public void delete(Long id) {
        ProductPo record = productPoMapper.selectByPrimaryKey(id);
        if(null == record){
            return;
        }
        String key = RedisKeyUtil.buildProductInfoKey(record.getProductCode());
        cacheTemplate.remove(key);
        ProductPo del = new ProductPo();
        del.setId(id);
        del.setDelFlag(1);
        del.setProductCode(record.getProductCode() + System.currentTimeMillis());
        productPoMapper.updateByPrimaryKeySelective(del);
    }
}
