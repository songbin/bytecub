package com.bytecub.openapi.controller;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.DataResult;
import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.DevQueryReqDto;
import com.bytecub.common.domain.dto.request.prop.TemplateReqDto;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.common.domain.dto.response.prop.TemplateResDto;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.mdm.service.IDeviceService;
import com.bytecub.mdm.service.IProductFuncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: OpenDeviceController.java, v 0.1 2021-01-08  Exp $$
  */
@Slf4j
@RestController
@RequestMapping(BCConstants.URL_PREFIX.OPEN_API + "device")
@Api(description = "开放平台设备管理")
public class OpenDeviceController {
    @Autowired IDeviceService deviceService;

    @Autowired IProductFuncService productFuncService;

    @RequestMapping(value = "search", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询设备列表", httpMethod = "POST", response = DataResult.class, notes = "分页查询设备列表")
    public DataResult<PageResDto<DevicePageResDto>> search(@RequestBody PageReqDto<DevQueryReqDto> searchPage) {
        PageResDto<DevicePageResDto> resDto = deviceService.queryByPage(searchPage);
        return DataResult.ok(resDto);
    }

    @RequestMapping(value = "template", method = RequestMethod.POST)
    @ApiOperation(value = "物模型数据模版", httpMethod = "POST", response = DataResult.class, notes = "物模型数据模版")
    public DataResult  template(@Valid @RequestBody TemplateReqDto reqDto){
        if(StringUtils.isEmpty(reqDto.getIdentifier()) && StringUtils.isEmpty(reqDto.getProductCode())){
            log.warn("产品编码和标识符不能同时为空");
            throw BCGException.genException(BCErrorEnum.INVALID_PARAM, "产品编码和标识符不能同时为空");
        }
        TemplateResDto resDto = productFuncService.template(reqDto);
        return   DataResult.ok(resDto);
    }
}
