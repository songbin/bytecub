package com.bytecub.manager.controller.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytecub.common.domain.dto.request.product.ReleasePropReqDto;
import com.bytecub.common.domain.dto.request.prop.TemplateReqDto;
import com.bytecub.common.domain.dto.response.prop.TemplateResDto;
import com.bytecub.common.exception.BCGException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.DataResult;
import com.bytecub.common.domain.bo.TopicDescBo;
import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.ProductAddReqDto;
import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.domain.dto.request.PropCreateReqDto;
import com.bytecub.common.domain.dto.request.product.ProductUpdateReqDto;
import com.bytecub.common.domain.dto.response.DataTypeItemResDto;
import com.bytecub.common.domain.dto.response.ProductDetailResDto;
import com.bytecub.common.domain.dto.response.ProductResDto;
import com.bytecub.common.domain.dto.response.ProtocolItemResDto;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.metadata.BcMetaType;
import com.bytecub.common.metadata.BcMetaUnit;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.manager.service.IAdminProductFuncService;
import com.bytecub.manager.service.IAdminProductService;
import com.bytecub.mdm.service.IProductFuncService;
import com.bytecub.mdm.service.IProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 产品属性相关管理
  * @author bytecub@163.com  songbin
  * @version Id: ResourceController.java, v 0.1 2020-12-10  Exp $$
  */
@Slf4j
@RestController
@RequestMapping(BCConstants.URL_PREFIX.MGR + "product/prop")
@Api(tags = "产品属性管理")
public class PropController {
    @Autowired
    IAdminProductFuncService adminProductFuncService;
    @Autowired
    IProductService productService;
    @Autowired IAdminProductService adminProductService;
    @Autowired IProductFuncService productFuncService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ApiOperation(value = "产品属性列表", httpMethod = "GET", response = DataResult.class, notes = "产品属性列表")
    public DataResult props(String code, String type){
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(type)){
            return DataResult.fail(BCErrorEnum.INVALID_PARAM);
        }
        ProductFuncTypeEnum funcType = ProductFuncTypeEnum.explain(type);
        if(null == funcType) funcType = ProductFuncTypeEnum.PROP;
        List<ProductFuncItemResDto> result = adminProductFuncService.listByProductAndType(code, funcType);
        return DataResult.ok(result);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('sys:model:add')")
    @ApiOperation(value = "添加属性", httpMethod = "POST", response = DataResult.class, notes = "添加属性")
    public DataResult  propAdd(@RequestBody   PropCreateReqDto dto){
        adminProductFuncService.addProp(dto);
        return DataResult.ok( );
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('sys:model:update')")
    @ApiOperation(value = "编辑属性", httpMethod = "POST", response = DataResult.class, notes = "编辑属性")
    public DataResult  propUpdate(@Valid @RequestBody   PropCreateReqDto dto){
        dto.setIdentifier(dto.getIdentifier().toLowerCase());
        adminProductFuncService.updateProp(dto);
        return DataResult.ok( );
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @ApiOperation(value = "属性详情", httpMethod = "GET", response = DataResult.class, notes = "属性详情")
    public DataResult<ProductFuncItemResDto>  propDetail(Long id){
        ProductFuncItemResDto result = adminProductFuncService.queryById(id);
        return null == result ? DataResult.fail("没有找到元素") : DataResult.ok(result);
    }

    @RequestMapping(value = "detail/identifier", method = RequestMethod.GET)
    @ApiOperation(value = "根据属性编码查询属性详情", httpMethod = "GET", response = DataResult.class, notes = "根据属性编码查询属性详情")
    public DataResult<ProductFuncItemResDto>  propDetail(String productCode, String identifier, String funcType){
        if(StringUtils.isEmpty(productCode) || StringUtils.isEmpty(identifier)){
            throw BCGException.genException(BCErrorEnum.INVALID_PARAM);
        }
        ProductFuncItemResDto result = adminProductFuncService.listByProdIdType(productCode, identifier, funcType);
        return DataResult.ok(result);
    }

    @RequestMapping(value = "del", method = RequestMethod.GET)
    @PreAuthorize("@ps.hasPermission('sys:model:delete')")
    @ApiOperation(value = "删除属性", httpMethod = "GET", response = DataResult.class, notes = "删除属性")
    public DataResult  propDel(Long id){
        adminProductFuncService.delProp(id);
        return DataResult.ok( );
    }
    @RequestMapping(value = "release", method = RequestMethod.GET)
    @PreAuthorize("@ps.hasPermission('sys:model:release')")
    @ApiOperation(value = "属性发布", httpMethod = "GET", response = DataResult.class, notes = "属性发布")
    public DataResult  propRelease(Long id){
        productFuncService.releaseProp(id);
        return DataResult.ok( );
    }

    @RequestMapping(value = "template", method = RequestMethod.POST)
    @ApiOperation(value = "物模型数据模版", httpMethod = "POST", response = DataResult.class, notes = "物模型数据模版")
    public DataResult  template(@Valid @RequestBody TemplateReqDto reqDto){
        if(StringUtils.isEmpty(reqDto.getIdentifier()) && StringUtils.isEmpty(reqDto.getProductCode())){
            log.warn("产品编码和标识符不能同时为空");
            throw BCGException.genException(BCErrorEnum.INVALID_PARAM, "产品编码和标识符不能同时为空");
        }
        TemplateResDto resDto = productFuncService.template(reqDto);
        return   DataResult.ok(resDto);
    }



}
