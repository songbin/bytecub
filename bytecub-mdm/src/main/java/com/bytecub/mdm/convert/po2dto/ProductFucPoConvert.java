package com.bytecub.mdm.convert.po2dto;

import com.bytecub.common.domain.bo.PropAttrBo;
import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.metadata.BcMetaUnit;
import com.bytecub.mdm.dao.po.ProductFuncPo;
import com.bytecub.utils.JSONProvider;
import com.bytecub.utils.ObjectCopyUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProductFucPoConvert.java, v 0.1 2020-12-18  Exp $$
  */
public class ProductFucPoConvert {
    public static ProductFuncItemResDto item(ProductFuncPo po){
        if(null == po){
            return null;
        }
        ProductFuncItemResDto productFuncItemResDto = new ProductFuncItemResDto();
        ObjectCopyUtil.copyProperties(po, productFuncItemResDto);
        productFuncItemResDto.setProductCode(po.getProductCode());
        productFuncItemResDto.setAttr(po.getAttr());
        productFuncItemResDto.setDataType(po.getDataType());
        productFuncItemResDto.setIdentifier(po.getIdentifier());
        productFuncItemResDto.setPropDesc(po.getFuncDesc());
        productFuncItemResDto.setUnit(po.getUnit());
        productFuncItemResDto.setWrType(po.getWrType());
        productFuncItemResDto.setPropName(po.getFuncName());
        productFuncItemResDto.setCreateTime(po.getCreateTime());
        if(StringUtils.isEmpty(po.getAttr())){
            return productFuncItemResDto;
        }
        PropAttrBo propAttrBo = JSONProvider.parseObject(po.getAttr(), PropAttrBo.class);
        productFuncItemResDto.setRangeMax("" + propAttrBo.getMax());
        productFuncItemResDto.setRangeMin("" + propAttrBo.getMin());
        productFuncItemResDto.setBool0(propAttrBo.getBool0());
        productFuncItemResDto.setBool1(propAttrBo.getBool1());
        productFuncItemResDto.setLength("" + propAttrBo.getLength());
        productFuncItemResDto.setEventType(po.getEventType());
        productFuncItemResDto.setUnit(propAttrBo.getUnit());
        Map map = (Map)JSONProvider.parseObject(po.getAttr(), Map.class);
        productFuncItemResDto.setAttrMap(map);

        return productFuncItemResDto;
    }

    public static List<ProductFuncItemResDto> list(List<ProductFuncPo> list){
        List<ProductFuncItemResDto> result = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return result;
        }
        for(ProductFuncPo item:list){
            ProductFuncItemResDto itemBo = item(item);
            result.add(itemBo);
        }
        return result;
    }
}
