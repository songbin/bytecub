package com.bytecub.manager.controller.user;

import com.bytecub.common.annotations.NoTokenAllowedAnnotation;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.DataResult;
import com.bytecub.common.domain.dto.request.LoginReqDto;
import com.bytecub.common.domain.dto.response.CaptchResDto;
import com.bytecub.common.domain.dto.response.LoginResDto;
import com.bytecub.common.domain.dto.response.user.UserInfoResDto;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.manager.service.ITokenService;
import com.bytecub.mdm.dao.po.UserPo;
import com.bytecub.mdm.service.IRolePermissionService;
import com.bytecub.mdm.service.IRoleService;
import com.bytecub.mdm.service.IUserService;
import com.bytecub.utils.Md5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: LoginController.java, v 0.1 2020-12-14  Exp $$  
 */
@Slf4j
@RestController
@RequestMapping(BCConstants.URL_PREFIX.MGR + "user")
@Api(description = "用户管理相关")
public class LoginController {
    @Autowired
    IUserService userService;
    @Autowired
    ITokenService tokenService;
    @Resource
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录", httpMethod = "POST", response = DataResult.class, notes = "用户登录")
    @NoTokenAllowedAnnotation
    public DataResult<LoginResDto> login(@RequestBody LoginReqDto loginDto) {
        Boolean ret = tokenService.verifyCaptcha(loginDto.getCaptureNo(), loginDto.getCapture());
        if (!ret) {
            return DataResult.fail(BCErrorEnum.INVALID_CODE);
        }
        UserPo query = new UserPo();
        query.setUserName(loginDto.getUserName());
        String password = Md5Utils.md5(loginDto.getPassword());
        query.setPasswd(password);
        List<UserPo> list = userService.queryUserByUnion(query);
        if (CollectionUtils.isEmpty(list)) {
            return DataResult.fail(BCErrorEnum.LOGIN_VALID_USERNAMEPASSWD);
        }
        UserPo userPo = list.get(0);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userPo.getUserName(), userPo.getPasswd()));
        LoginResDto result = new LoginResDto();
        result.setRoleType(userPo.getRoleType());
        result.setUserName(userPo.getUserName());
        result.setUserNick(userPo.getUserNick());
        String token = tokenService.genToken(userPo);
        result.setToken(token);
        return DataResult.ok(result);
    }

    /**
     * 管理后台用户退出
     *
     * @return 详情
     */
    @PostMapping("/logout")
    @ApiOperation(value = "logout", httpMethod = "POST", response = DataResult.class, notes = "管理后台用户退出")
    public DataResult logout() {
        ServletRequestAttributes requestAttributes =
            (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(BCConstants.GLOBAL.TOKEN);
        tokenService.removeToken(token);
        return DataResult.ok();
    }

    /**
     * 获取图像验证码
     *
     * @return
     * @throws IOException
     */
    @NoTokenAllowedAnnotation
    @RequestMapping("/getCaptchCode")
    @ApiOperation(value = "获取图像验证码", httpMethod = "GET", response = CaptchResDto.class, notes = "获取图像验证码")
    public DataResult<CaptchResDto> getCode() throws IOException {
        CaptchResDto dto = tokenService.getCaptchaCode();
        return DataResult.ok(dto);
    }

    /**
     * 获取图像验证码
     *
     * @return
     * @throws IOException
     */
    @NoTokenAllowedAnnotation
    @RequestMapping("/info")
    @ApiOperation(value = "用户信息", httpMethod = "GET", response = UserInfoResDto.class, notes = "用户信息")
    public DataResult<UserInfoResDto> UserInfo(String token) throws IOException {
        UserPo po = tokenService.getDataFromToken(token);
        if (null == po) {
            throw BCGException.genException(BCErrorEnum.INVALID_TOKEN);
        }
        UserInfoResDto dto = new UserInfoResDto();
        dto.setIntroduction("");
        dto.setName(po.getUserName());
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        dto.setRoles(roles);
        return DataResult.ok(dto);
    }
}
