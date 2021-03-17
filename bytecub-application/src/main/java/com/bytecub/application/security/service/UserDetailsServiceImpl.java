package com.bytecub.application.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bytecub.manager.common.domain.security.SecurityUserBo;
import com.bytecub.mdm.dao.po.RolePermissionPo;
import com.bytecub.mdm.dao.po.UserPo;
import com.bytecub.mdm.service.IRolePermissionService;
import com.bytecub.mdm.service.IUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户验证处理
 *
 * @author songbin
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private IUserService  userService;

    @Autowired
    private IRolePermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserPo user = userService.queryUserByName(username);
        if (null == user)
        {
            log.info("登录用户：{} 不存在.", username);
            throw BCGException.genException(BCErrorEnum.LOGIN_VALID_USERNAMEPASSWD);
        }
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(UserPo user)
    {
        SecurityUserBo securityUserBo = new SecurityUserBo();
        List<RolePermissionPo> listPermissions = permissionService.queryByRoleCode(user.getRoleCode());
        securityUserBo.setUserInfo(user);
        Set<String> permissions = new HashSet<>();
        for(RolePermissionPo item:listPermissions){
            permissions.add(item.getPermissionCode());
        }
        securityUserBo.setPermissions(permissions);
        return securityUserBo;
    }
}
