package com.bytecub.manager.service.impl;

import com.bytecub.common.biz.EsUtil;
import com.bytecub.common.domain.bo.PropAttrBo;
import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.domain.dto.request.PropCreateReqDto;
import com.bytecub.common.metadata.BcMetaType;
import com.bytecub.common.metadata.BcMetaUnit;
import com.bytecub.common.metadata.EventTypeEnum;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.manager.service.IAdminProductFuncService;
import com.bytecub.mdm.cache.IProductFuncCache;
import com.bytecub.mdm.convert.po2dto.ProductFucPoConvert;
import com.bytecub.mdm.dao.po.ProductFuncPo;
import com.bytecub.mdm.service.IProductFuncService;
import com.bytecub.storage.IDataCenterService;
import com.bytecub.utils.JSONProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: AdminProductFuncServiceImpl.java, v 0.1 2020-12-11  Exp $$
  */
@Slf4j
@Service
public class AdminProductFuncServiceImpl implements IAdminProductFuncService {
    @Autowired IProductFuncService productFuncService;
    @Autowired IDataCenterService dataCenterService;
    @Autowired IProductFuncCache  productFuncCache;


    @Override
    public List<ProductFuncItemResDto> listByProductAndType(String productCode,
                                                              ProductFuncTypeEnum typeEnum) {
        List<ProductFuncItemResDto>  list = productFuncService.ListFuncByProductCode(productCode,null, typeEnum);
        return list;
    }

    @Override public ProductFuncItemResDto listByProdIdType(String productCode,
                                                                      String identifier, String funcType) {
        return productFuncService.listByProdIdType(productCode, identifier, funcType);
    }

    @Override public ProductFuncItemResDto queryById(Long id) {
        ProductFuncPo productFuncPo = productFuncService.queryById(id);
        if(null == productFuncPo){
            return null;
        }
        ProductFuncItemResDto dto = ProductFucPoConvert.item(productFuncPo);
        return dto;
    }

    @Override public Boolean addProp(PropCreateReqDto dto) {
        ProductFuncPo record = this.dto2Po(dto);
        productFuncService.insert(record);
        productFuncCache.remove(record.getProductCode(), record.getFuncType());
        return true;
    }

    @Override public Boolean updateProp(PropCreateReqDto dto) {
        ProductFuncPo record = new ProductFuncPo();
        record.setFuncName(dto.getPropName());
        record.setUnit(dto.getUnit());
        record.setWrType(dto.getWrType());
        record.setFuncDesc(dto.getPropDesc());
        record.setInputParam(dto.getInputParam());
        record.setOutputParam(dto.getOutputParam());
        record.setAttr(dto.getAttr());
        record.setId(dto.getId());
        productFuncService.update(record);
        return true;
    }
    @Override public Boolean updatePropByIdentifier(PropCreateReqDto dto) {
        ProductFuncPo record = new ProductFuncPo();
        record.setFuncName(dto.getPropName());
        record.setUnit(dto.getUnit());
        record.setWrType(dto.getWrType());
        record.setFuncDesc(dto.getPropDesc());
        record.setAttr(dto.getAttr());
        record.setIdentifier(dto.getIdentifier());
        record.setInputParam(dto.getInputParam());
        record.setOutputParam(dto.getOutputParam());
        record.setFuncType(dto.getFuncType());
        EventTypeEnum typeEnum = EventTypeEnum.explain(dto.getEventType());
        typeEnum = null == typeEnum ? EventTypeEnum.INFO : typeEnum;
        record.setEventType(typeEnum.getCode());
        productFuncService.updateByTypeIdentifier(record);
        return true;
    }

    @Override public Boolean delProp(Long id) {

        ProductFuncPo record = productFuncService.queryById(id);
        ProductFuncPo update = new ProductFuncPo();
        if(null == update) {
            return true;
        }
        update.setDelFlag(1);
        update.setId(id);
        update.setIdentifier(record.getIdentifier() + "_" + System.currentTimeMillis());
        productFuncService.update(update);
        return true;
    }
    private ProductFuncPo dto2Po(PropCreateReqDto dto){
        ProductFuncPo record = new ProductFuncPo();
        ProductFuncTypeEnum productFuncTypeEnum = ProductFuncTypeEnum.explain(dto.getFuncType());
        if(null == productFuncTypeEnum) productFuncTypeEnum = productFuncTypeEnum.PROP;
        record.setFuncType(productFuncTypeEnum.getType());
        EventTypeEnum eventTypeEnum = EventTypeEnum.explain(dto.getEventType());
        if(null == eventTypeEnum) eventTypeEnum = EventTypeEnum.INFO;
        record.setEventType(eventTypeEnum.getCode());
        record.setFuncName(dto.getPropName());
        record.setIdentifier(dto.getIdentifier());
        record.setDataType(dto.getDataType());
        if(productFuncTypeEnum.equals(ProductFuncTypeEnum.SERVICE)){
            String input = dto.getInputParam();
            if(StringUtils.isEmpty(input)){
                record.setDataType("");
            }else{
                PropAttrBo propAttrBo = JSONProvider.parseObject(input, PropAttrBo.class);
                record.setDataType(propAttrBo.getDataType());
            }

        }

        record.setUnit(dto.getUnit());
        record.setWrType(dto.getWrType());
        record.setProductCode(dto.getProductCode());
        record.setFuncDesc(dto.getPropDesc());
        String attr = dto.getAttr();
        record.setAttr(attr);
        if(!StringUtils.isEmpty(attr)){
            PropAttrBo attrBo = JSONProvider.parseObject(dto.getAttr(), PropAttrBo.class);
            record.setDataType(attrBo.getDataType());
        }

        record.setAsync(dto.getAsync());
        record.setInputParam(dto.getInputParam());
        record.setOutputParam(dto.getOutputParam());
        return record;
    }
}
