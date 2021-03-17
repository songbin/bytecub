package com.bytecub.manager.service.impl;

import com.bytecub.common.domain.dto.request.ProductAddReqDto;
import com.bytecub.common.domain.dto.request.product.ProductUpdateReqDto;
import com.bytecub.common.domain.dto.response.ProductDetailResDto;
import com.bytecub.common.domain.dto.response.ProtocolItemResDto;
import com.bytecub.manager.service.IAdminProductService;
import com.bytecub.mdm.dao.po.ProductPo;
import com.bytecub.mdm.service.IProductService;
import com.bytecub.protocol.service.IProtocolUtilService;
import com.bytecub.utils.JSONProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: AdminProductServiceImpl.java, v 0.1 2020-12-15  Exp $$
  */
@Service
@Slf4j
@DependsOn("springContextUtil")
public class AdminProductServiceImpl implements IAdminProductService {
    @Autowired IProductService      productService;
    @Autowired IProtocolUtilService protocolUtilService;

    @Override public List<ProtocolItemResDto> listProtocol() {
        return protocolUtilService.listProtocol();
    }

    @Override
    @Transactional
    public void insertProduct(ProductAddReqDto reqDto) {
        String productCode = RandomStringUtils.randomAlphanumeric(16).toLowerCase();
        ProductPo record = new ProductPo();
        record.setProductCode(productCode);
        record.setNodeType(reqDto.getNodeType());
        record.setProductName(reqDto.getProductName());
        record.setProductDesc(reqDto.getProductDesc());
        record.setProtocolCode(reqDto.getProtocolCode());
        String transportList = JSONProvider.toJSONString(reqDto.getTransportList());
        record.setTransportList(transportList);
        try{
            productService.saveItem(record);
        }catch (DuplicateKeyException dke){
            record.setProductCode(RandomStringUtils.randomAlphanumeric(11));
            productService.saveItem(record);
        }
    }

    @Override
    public void updateProduct(ProductUpdateReqDto reqDto) {
        ProductPo record = new ProductPo();
        record.setNodeType(reqDto.getNodeType());
        record.setProductName(reqDto.getProductName());
        record.setProductDesc(reqDto.getProductDesc());
        record.setProtocolCode(reqDto.getProtocolCode());
        String transportList = JSONProvider.toJSONString(reqDto.getTransportList());
        record.setTransportList(transportList);
        record.setId(reqDto.getId());
        productService.updateItem(record);

    }
}
