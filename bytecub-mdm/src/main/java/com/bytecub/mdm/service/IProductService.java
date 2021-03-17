package com.bytecub.mdm.service;

import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.product.ProductQueryReqDto;
import com.bytecub.common.domain.dto.response.ProductDetailResDto;
import com.bytecub.common.domain.dto.response.ProductResDto;
import com.bytecub.mdm.dao.po.ProductPo;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IProductService.java, v 0.1 2020-12-11  Exp $$
  */
public interface IProductService {
    List<ProductPo> listAllValid();
    /**
     * 根据产品编码查询产品详情
     * @param code
     * @return
     * */
    ProductPo queryByCode(String code);
    /**
     * 根据名字模糊搜索产品
     * @param searchPage
     * @return
     * */
    PageResDto<ProductResDto> searchByName(PageReqDto searchPage);
    /**
     * 新增
     * @param record
     * */
    void saveItem(ProductPo record);
    /**
     * 产品详情
     * @param productCode
     * @return
     * */
    ProductDetailResDto detail(String productCode);
    /**
     * 根据ID删除
     * @param id
     * */
    void delete(Long id);
    /**
     * 根据ID更新
     * @param record
     * */
    void updateItem(ProductPo record);
    /**
     * 一把查询出所有
     * @param queryReqDto
     * @return
     * */
    List<ProductResDto> query(ProductQueryReqDto queryReqDto);

}
