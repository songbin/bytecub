package com.bytecub.mdm.service;

import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.domain.dto.request.prop.TemplateReqDto;
import com.bytecub.common.domain.dto.response.prop.TemplateResDto;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.mdm.dao.po.ProductFuncPo;
import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IProductFuncService.java, v 0.1 2020-12-11  Exp $$
  */
public interface IProductFuncService {
    /**获取指定产品下指定功能类型物模型
     * @param productCode
     * @param funcStatus 如果是null的话 则查询所有状态
     * @param typeEnum
     * @return
     * */
    List<ProductFuncItemResDto> ListFuncByProductCode(String productCode, Integer funcStatus, ProductFuncTypeEnum typeEnum);
    /**
     * 根据标识符查询物模型
     * @param productCode String
     * @param typeEnum ProductFuncTypeEnum
     * @param  identifier String
     * @return   ProductFuncItemResDto
     * */
    ProductFuncItemResDto queryFunc(String productCode, ProductFuncTypeEnum typeEnum, String identifier);
    void insert(ProductFuncPo record) ;
    void update(ProductFuncPo record);
    /**
     * 根据标识符和模型功能类型更新具体某条物模型
     * @param  record
     * @return
     * */
    Integer updateByTypeIdentifier(ProductFuncPo record);
    ProductFuncPo queryById(Long id);
    /**
     * 发布属性,会生成对应的模版
     * @param   propId   Long
     * @return
     * */
    void releaseProp(Long propId);
    /**组装模版数据
     * @param reqDto TemplateReqDto
     * @return TemplateResDto
     * */
    TemplateResDto template(TemplateReqDto reqDto);

    /**
     * 根据属性编码查询属性详情
     * @param productCode String
     * @param identifier   String
     * @param  funcType String
     * @return List
     * */
    ProductFuncItemResDto listByProdIdType(String productCode, String identifier, String funcType);


}
