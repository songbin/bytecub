package com.bytecub.manager.service;

import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.domain.dto.request.PropCreateReqDto;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.mdm.dao.po.ProductFuncPo;

import java.util.List;

/**
 * 产品功能管理
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IAdminProductFuncService.java, v 0.1 2020-12-11  Exp $$
  */
public interface IAdminProductFuncService {
    /**发布产品下的的属性*/
    /**
     * 添加物模型
     * @param dto PropCreateReqDto
     * @return true or false
     * */
    Boolean addProp(PropCreateReqDto dto);
    /**
     * 更新物模型
     * @param dto PropCreateReqDto
     * @return true or false
     * */
    Boolean updateProp(PropCreateReqDto dto);
    /**
     * 根据标识符更新物模型
     * @param dto PropCreateReqDto
     * @return true or false
     * */
    Boolean updatePropByIdentifier(PropCreateReqDto dto);
    /**
     * 删除物模型
     * @param id Long
     * @return true or false
     * */
    Boolean delProp(Long id);
    /**
     * 根据ID查询物模型
     * @param Id Long
     * @return ProductFuncItemResDto
     * */
    ProductFuncItemResDto queryById(Long Id);
    /**
     * 根据产品编码和功能类型查询物模型列表
     * @param productCode String
     * @param typeEnum   ProductFuncTypeEnum
     * @return List
     * */
    List<ProductFuncItemResDto> listByProductAndType(String productCode, ProductFuncTypeEnum typeEnum);

    /**
     * 根据属性编码查询属性详情
     * @param productCode String
     * @param identifier   String
     * @param  funcType String
     * @return List
     * */
    ProductFuncItemResDto listByProdIdType(String productCode, String identifier, String funcType);
}
