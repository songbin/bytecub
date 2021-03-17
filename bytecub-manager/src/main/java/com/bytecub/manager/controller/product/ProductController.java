package com.bytecub.manager.controller.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytecub.common.domain.dto.request.product.ProductQueryReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.DataResult;
import com.bytecub.common.domain.bo.TopicDescBo;
import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.ProductAddReqDto;
import com.bytecub.common.domain.dto.request.product.ProductUpdateReqDto;
import com.bytecub.common.domain.dto.response.DataTypeItemResDto;
import com.bytecub.common.domain.dto.response.ProductDetailResDto;
import com.bytecub.common.domain.dto.response.ProductResDto;
import com.bytecub.common.domain.dto.response.ProtocolItemResDto;
import com.bytecub.common.metadata.BcMetaType;
import com.bytecub.common.metadata.BcMetaUnit;
import com.bytecub.manager.service.IAdminProductFuncService;
import com.bytecub.manager.service.IAdminProductService;
import com.bytecub.mdm.service.IProductFuncService;
import com.bytecub.mdm.service.IProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ResourceController.java, v 0.1 2020-12-10  Exp $$
  */
@Slf4j
@RestController
@RequestMapping(BCConstants.URL_PREFIX.MGR + "product")
@Api(tags = "产品管理")
public class ProductController {
    @Autowired
    IAdminProductFuncService adminProductFuncService;
    @Autowired
    IProductService productService;
    @Autowired IAdminProductService adminProductService;
    @Autowired IProductFuncService productFuncService;


    @RequestMapping(value = "search", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询产品列表", httpMethod = "POST", response = DataResult.class, notes = "分页查询产品列表")
    public DataResult<PageResDto<ProductResDto>> search(@RequestBody PageReqDto<String> searchPage){
        PageResDto<ProductResDto> page = productService.searchByName(searchPage);
        return DataResult.ok(page);
    }
    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ApiOperation(value = "结构体一次列出所有产品", httpMethod = "POST", response = DataResult.class, notes = "结构体一次列出所有产品")
    public DataResult<List<ProductResDto>> query(@RequestBody ProductQueryReqDto query){
        List<ProductResDto> result = productService.query(query);
        return DataResult.ok(result);
    }
    @RequestMapping(value = "protocols", method = RequestMethod.GET)
    @ApiOperation(value = "获取支持协议列表", httpMethod = "GET", response = DataResult.class, notes = "获取支持协议列表")
    public DataResult<List<ProtocolItemResDto>> protocols(){
        return DataResult.ok(adminProductService.listProtocol());
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('sys:product:create')")
    @ApiOperation(value = "创建产品", httpMethod = "POST", response = DataResult.class, notes = "创建产品")
    public DataResult create(@RequestBody  ProductAddReqDto reqDto){
        adminProductService.insertProduct(reqDto);
        return DataResult.ok();
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('sys:product:update')")
    @ApiOperation(value = "更新产品", httpMethod = "POST", response = DataResult.class, notes = "更新产品")
    public DataResult update(@RequestBody ProductUpdateReqDto reqDto){
        adminProductService.updateProduct(reqDto);
        return DataResult.ok();
    }
    @RequestMapping(value = "del", method = RequestMethod.GET)
    @PreAuthorize("@ps.hasPermission('sys:product:delete')")
    @ApiOperation(value = "删除产品", httpMethod = "GET", response = DataResult.class, notes = "删除产品")
    public DataResult del(Long id){
        productService.delete(id);
        return DataResult.ok();
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @ApiOperation(value = "产品详情", httpMethod = "GET", response = DataResult.class, notes = "产品详情")
    public DataResult<ProductDetailResDto> detail(String code){
        ProductDetailResDto resDto = productService.detail(code);
        return DataResult.ok(resDto);
    }


    @RequestMapping(value = "datatypes", method = RequestMethod.GET)
    @ApiOperation(value = "获取数据类型", httpMethod = "GET", response = DataResult.class, notes = "获取数据类型")
    public DataResult<List<DataTypeItemResDto>> dataTypeList(){
        List<DataTypeItemResDto> result = new ArrayList<>();
        for(BcMetaType item: BcMetaType.values()){
            DataTypeItemResDto dto = new DataTypeItemResDto(item.getCode(), item.getMsg(), item.getMsg());
            result.add(dto);
        }
        return DataResult.ok(result);
    }

    @RequestMapping(value = "units", method = RequestMethod.GET)
    @ApiOperation(value = "获取计量单位列表", httpMethod = "GET", response = DataResult.class, notes = "获取计量单位列表")
    public DataResult<List<DataTypeItemResDto>> units(){
        List<DataTypeItemResDto> result = new ArrayList<>();
        for(BcMetaUnit item: BcMetaUnit.values()){
            DataTypeItemResDto dto = new DataTypeItemResDto(item.getCode(), item.getName(), item.getCode() +"|"+item.getMsg());
            result.add(dto);
        }
        return DataResult.ok(result);
    }


    @RequestMapping(value = "topics", method = RequestMethod.GET)
    @ApiOperation(value = "topic列表", httpMethod = "GET", response = DataResult.class, notes = "删除属性")
    public DataResult  topics(String productCode){
        List<TopicDescBo> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        TopicDescBo item = TopicDescBo.build("属性上报", "bcup/"  + "设备编码/prop", null);
        list.add(item);
        TopicDescBo item1 = TopicDescBo.build("事件上报", "bcup/"  + "设备编码/event", null);
        list.add(item1);
        TopicDescBo item2 = TopicDescBo.build("下行数据返回结果上报", "bcup/"  + "设备编码/" + BCConstants.TOPIC.MSG_REPLY , "设备上报下行数据返回结果(服务&属性设置)");
        list.add(item2);

        TopicDescBo item3 = TopicDescBo.build("调用设备服务", "bcdown/"  + "设备编码/service/invoke", null);
        list.add(item3);
        TopicDescBo item4 = TopicDescBo.build("设备属性设置", "bcdown/" + "设备编码/property/set", "设备属性设置");
        list.add(item4);
        return DataResult.ok( list);
    }


}
