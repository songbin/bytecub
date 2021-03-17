package com.bytecub.mdm.convert.po2dto;

import com.bytecub.common.domain.dto.request.ResourceRequestDto;
import com.bytecub.common.domain.dto.response.ResourceResDto;
import com.bytecub.mdm.dao.po.ResourcePo;
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
public class ResourcePoConvert {
    public static ResourceResDto item(ResourcePo po){
        ResourceResDto resourceRequestDto = new ResourceResDto();
        ObjectCopyUtil.copyProperties(po, resourceRequestDto);
        return resourceRequestDto;
    }

    public static List<ResourceResDto> list(List<ResourcePo> list){
        List<ResourceResDto> result = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return result;
        }
        for(ResourcePo item:list){
            ResourceResDto itemBo = item(item);
            result.add(itemBo);
        }
        return result;
    }
}
