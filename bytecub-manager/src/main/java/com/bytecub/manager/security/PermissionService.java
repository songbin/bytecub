package com.bytecub.manager.security;

import java.util.Set;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.manager.common.domain.security.SecurityUserBo;
import com.bytecub.manager.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: ResourceController.java, v 0.1 2020-12-10  Exp $$  
 */
@Service("ps")
public class PermissionService
{
    @Autowired
    private ITokenService tokenService;

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermission(String permission)
    {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest servletRequest = attributes.getRequest();
        String token = servletRequest.getHeader(BCConstants.GLOBAL.TOKEN);
        SecurityUserBo securityUserBo = tokenService.queryByToken(token);
        if(null == securityUserBo ){
            return false;
        }
        if(CollectionUtils.isEmpty(securityUserBo.getPermissions())){
            return false;
        }
        return securityUserBo.getPermissions().contains(permission);
    }


}
