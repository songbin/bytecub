package com.bytecub.protocol.service.impl;

import com.bytecub.common.annotations.BcProtocolAnnotation;
import com.bytecub.common.domain.dto.response.ProtocolItemResDto;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.protocol.base.IBaseProtocol;
import com.bytecub.protocol.service.IProtocolUtilService;
import com.bytecub.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProtocolUtilService.java, v 0.1 2020-12-29  Exp $$
  */
@Slf4j
@Service
public class ProtocolUtilServiceImpl implements IProtocolUtilService {
    @Autowired
    SpringContextUtil springContextUtil;
    /**key=>协议编码 */
    Map<String, IBaseProtocol> protocolInstanceMap = new HashMap<>();


    @Override
    public List<ProtocolItemResDto> listProtocol() {
        List<ProtocolItemResDto> result = new ArrayList<>();
        Map<String, IBaseProtocol> maps = SpringContextUtil.getBeanWithAnnotation(
                BcProtocolAnnotation.class);
        maps.forEach((key, protocol)->{
            BcProtocolAnnotation annotation = protocol.getClass().getAnnotation(BcProtocolAnnotation.class);
            ProtocolItemResDto itemResDto = new ProtocolItemResDto();
            itemResDto.setCode(annotation.protocolCode());
            itemResDto.setDesc(annotation.desc());
            itemResDto.setName(annotation.name());
            result.add(itemResDto);
        });
        return result;
    }

    @Override public IBaseProtocol queryProtocolInstanceByCode(String protocolCode) {
        if(!CollectionUtils.isEmpty(this.protocolInstanceMap)){
            return protocolInstanceMap.get(protocolCode);
        }
        Map<String, IBaseProtocol> maps = SpringContextUtil.getBeanWithAnnotation(BcProtocolAnnotation.class);
        IBaseProtocol instance = null;
        for(IBaseProtocol item : maps.values()){
            BcProtocolAnnotation annotation = item.getClass().getAnnotation(BcProtocolAnnotation.class);
            this.protocolInstanceMap.put(annotation.protocolCode(), item);
            if(annotation.protocolCode().equals(protocolCode)){
                instance = item;
            }
        }
        return instance;
    }
}
