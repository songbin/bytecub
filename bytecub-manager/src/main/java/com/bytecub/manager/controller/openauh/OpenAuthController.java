package com.bytecub.manager.controller.openauh;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.DataResult;
import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.DevQueryReqDto;
import com.bytecub.common.domain.dto.request.openauth.OpenAuthRequestDto;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.common.domain.dto.response.openauth.OpenAuthResDto;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.manager.service.IAdminOpenAuthService;
import com.bytecub.mdm.dao.po.OpenAuthPo;
import com.bytecub.mdm.service.IOpenAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: DeviceController.java, v 0.1 2020-12-22  Exp $$
  */
@Slf4j @RestController
@RequestMapping(BCConstants.URL_PREFIX.MGR + "open/auth")
@Api(description = "第三方授权管理")
public class OpenAuthController {
    @Autowired IAdminOpenAuthService adminOpenAuthService;
    @Autowired IOpenAuthService openAuthService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('system:open:add')")
    @ApiOperation(value = "添加授权", httpMethod = "POST", response = DataResult.class, notes = "添加授权")
    public DataResult add(@Valid @RequestBody OpenAuthRequestDto reqDto) {
        adminOpenAuthService.create(reqDto);
        return DataResult.ok();
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ApiOperation(value = "授权列表", httpMethod = "POST", response = DataResult.class, notes = "授权列表")
    public DataResult<PageResDto<OpenAuthPo>> search(@RequestBody PageReqDto searchPage) {
        PageResDto<OpenAuthPo> resDto = openAuthService.searchByPage(searchPage);
        return DataResult.ok(resDto);
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @PreAuthorize("@ps.hasPermission('system:open:delete')")
    @ApiOperation(value = "删除授权", httpMethod = "GET", response = DataResult.class, notes = "删除授权")
    public DataResult  search(Long id) {
        openAuthService.delete(id);
        return DataResult.ok();
    }

    @RequestMapping(value = "secret", method = RequestMethod.GET)
    @PreAuthorize("@ps.hasPermission('system:open:delete')")
    @ApiOperation(value = "查看密钥", httpMethod = "GET", response = DataResult.class, notes = "查看密钥")
    public DataResult  secret(String  key) {
        OpenAuthPo po = openAuthService.selectByKey(key);
        if(null == po){
            throw BCGException.genException(BCErrorEnum.APP_NO_EXIST);
        }
        return DataResult.ok(po.getAppSecret());
    }


}
