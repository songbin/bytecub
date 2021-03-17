package com.bytecub.manager.service;

import com.bytecub.common.domain.dto.request.ProductAddReqDto;
import com.bytecub.common.domain.dto.request.product.ProductUpdateReqDto;
import com.bytecub.common.domain.dto.response.ProductDetailResDto;
import com.bytecub.common.domain.dto.response.ProtocolItemResDto;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IAdminProductService.java, v 0.1 2020-12-15  Exp $$
  */
public interface IAdminProductService {
    List<ProtocolItemResDto> listProtocol();
    void insertProduct(ProductAddReqDto reqDto);
    void updateProduct(ProductUpdateReqDto reqDto);
}
