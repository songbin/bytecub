package com.bytecub.mdm.convert.po2dto;

import com.bytecub.common.domain.dto.response.ProductResDto;
import com.bytecub.mdm.dao.po.ProductPo;
import com.bytecub.utils.ObjectCopyUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ResourceDtoConvert.java, v 0.1 2020-12-10  Exp $$
  */
public class ProductPoConvert {
    public static ProductResDto item(ProductPo po){
        ProductResDto resourceRequestDto = new ProductResDto();
        ObjectCopyUtil.copyProperties(po, resourceRequestDto);
        return resourceRequestDto;
    }

    public static List<ProductResDto> list(List<ProductPo> list){
        List<ProductResDto> result = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return result;
        }
        for(ProductPo item:list){
            ProductResDto itemBo = item(item);
            result.add(itemBo);
        }
        return result;
    }
}
