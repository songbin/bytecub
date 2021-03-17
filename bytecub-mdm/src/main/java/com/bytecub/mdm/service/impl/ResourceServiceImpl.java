package com.bytecub.mdm.service.impl;

import com.bytecub.common.domain.dto.request.ResourceRequestDto;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.mdm.convert.dto2po.ResourceReqDtoConvert;
import com.bytecub.mdm.dao.dal.ResourcePoMapper;
import com.bytecub.mdm.dao.po.ResourcePo;
import com.bytecub.mdm.service.IResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ResourceServiceImpl.java, v 0.1 2020-12-10  Exp $$
  */
@Service
@Slf4j
public class ResourceServiceImpl implements IResourceService {
    @Autowired
    ResourcePoMapper resourcePoMapper;
    @Override public void createResource(ResourceRequestDto dto) {
        try{
            ResourcePo resourcePo = ResourceReqDtoConvert.item(dto);
            resourcePoMapper.insertSelective(resourcePo);
        }catch (Exception e){
            log.warn("创建资源失败", e);
            throw BCGException.genException(BCErrorEnum.RESOURCE_CREATE_FAIL);
        }
    }
}
