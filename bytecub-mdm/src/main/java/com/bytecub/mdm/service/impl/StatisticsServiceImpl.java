package com.bytecub.mdm.service.impl;


import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.mdm.dao.po.ProductPo;
import com.bytecub.mdm.service.IProductFuncService;
import com.bytecub.mdm.service.IProductService;
import com.bytecub.mdm.service.IStatisticsService;
import com.bytecub.storage.IDataCenterService;
import com.bytecub.storage.IMessageReplyService;
import com.bytecub.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: StatisticsServiceImpl.java, v 0.1 2021-01-27  Exp $$
  */
@Slf4j
@Service
public class StatisticsServiceImpl implements IStatisticsService {
    @Autowired
    IProductService productService;
    @Autowired
    IProductFuncService productFuncService;
    @Autowired
    IDataCenterService dataCenterService;
    @Autowired IMessageReplyService messageReplyService;

    @Override
    public Long countMsgTotalByDay(Date date) {
        Long total = 0L;
        Date start = DateUtil.getDayStart(date);
        Date end = DateUtil.getDayEnd(date);
        List<ProductPo> products = productService.listAllValid();
        for(ProductPo po:products){
            for(ProductFuncTypeEnum funcTypeEnum:ProductFuncTypeEnum.values()){
                List<ProductFuncItemResDto> models = productFuncService.ListFuncByProductCode(po.getProductCode(), null, funcTypeEnum);
                for(ProductFuncItemResDto dto:models){
                    if(ProductFuncTypeEnum.PROP.equals(funcTypeEnum)){
                        long setCount = dataCenterService.countSetRange(dto.getProductCode(), funcTypeEnum, dto.getIdentifier(), start, end);
                        total += setCount;
                    }
                    long upCount = dataCenterService.countUpRange(dto.getProductCode(), funcTypeEnum, dto.getIdentifier(), start, end);
                    total += upCount;
                }
            }
         }
        long replyCount =  messageReplyService.count(start, end);
        total += replyCount;
        return total;
    }
}
