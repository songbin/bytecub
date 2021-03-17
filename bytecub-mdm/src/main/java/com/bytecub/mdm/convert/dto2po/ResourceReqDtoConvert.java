package com.bytecub.mdm.convert.dto2po;

import com.bytecub.common.domain.dto.request.ResourceRequestDto;
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
public class ResourceReqDtoConvert {
    public static ResourcePo item(ResourceRequestDto dto){
        ResourcePo resourcePo = new ResourcePo();
        ObjectCopyUtil.copyProperties(dto, resourcePo);
        return resourcePo;
    }

    public static List<ResourcePo> list(List<ResourceRequestDto> list){
        List<ResourcePo> result = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return result;
        }
        for(ResourceRequestDto item:list){
            ResourcePo itemBo = item(item);
            result.add(itemBo);
        }
        return result;
    }
}
