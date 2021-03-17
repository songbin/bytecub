package com.bytecub.manager.service.impl;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.dto.response.CaptchResDto;
import com.bytecub.manager.common.domain.security.SecurityUserBo;
import com.bytecub.manager.service.ITokenService;
import com.bytecub.mdm.dao.po.RolePermissionPo;
import com.bytecub.mdm.dao.po.UserPo;
import com.bytecub.mdm.service.IRolePermissionService;
import com.bytecub.mdm.service.IRoleService;
import com.bytecub.plugin.redis.CacheTemplate;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.aspectj.weaver.patterns.IToken;
import org.bouncycastle.cms.bc.BcCMSContentEncryptorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: TokenServiceImpl.java, v 0.1 2020-12-14  Exp $$  
 */
@Slf4j
@Service
public class TokenServiceImpl implements ITokenService {
    @Autowired
    private Producer producer;
    @Autowired
    CacheTemplate cacheTemplate;
    @Autowired
    IRolePermissionService rolePermissionService;

    @Override
    public String genToken(UserPo userPo) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        String key = BCConstants.REDIS_KEY.TOKEN + token;
        List<RolePermissionPo> listPermissions = rolePermissionService.queryByRoleCode(userPo.getRoleCode());
        SecurityUserBo securityUserBo = new SecurityUserBo();
        securityUserBo.setUserInfo(userPo);
        Set<String> permissions = new HashSet<>();
        for(RolePermissionPo item:listPermissions){
            permissions.add(item.getPermissionCode());
        }
        securityUserBo.setPermissions(permissions);
        cacheTemplate.set(key, securityUserBo, BCConstants.REDIS_DEF.TOKEN_EXPIRED);
        return token;
    }

    @Override
    public UserPo getDataFromToken(String token) {
        String key = BCConstants.REDIS_KEY.TOKEN + token;
        SecurityUserBo value = cacheTemplate.get(key, SecurityUserBo.class);
        return null == value ? null : value.getUserInfo();
    }

    @Override
    public SecurityUserBo queryByToken(String token) {
        String key = BCConstants.REDIS_KEY.TOKEN + token;
        SecurityUserBo value = cacheTemplate.get(key, SecurityUserBo.class);
        return value;
    }

    @Override
    public void removeToken(String token) {
        if (!StringUtils.isEmpty(token)) {
            String tokenKey = BCConstants.REDIS_KEY.TOKEN + token + token;
            cacheTemplate.remove(tokenKey);
        }
    }

    @Override
    public CaptchResDto getCaptchaCode() throws IOException {
        Map<String, Object> map = new HashMap<>(2);
        CaptchResDto captchResponse = new CaptchResDto();
        // 生成验证码
        String text = producer.createText();
        // 生成图片验证码
        ByteArrayOutputStream outputStream = null;
        BufferedImage image = producer.createImage(text);
        // 生成图片验证码
        outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", outputStream);
        // 生成一个token
        String token = UUID.randomUUID().toString().toUpperCase();
        captchResponse.setCaptchaToken(token);
        // 生成captcha的token
        // 对字节数组Base64编码
        captchResponse.setImg(Base64.encodeBase64String(outputStream.toByteArray()));

        String captchKey = BCConstants.REDIS_KEY.CAPTCH_KEY + token;
        /**
         * 缓存TOKEN key
         */
        cacheTemplate.set(captchKey, text.toLowerCase(), 5 * 60);
        return captchResponse;
    }

    @Override
    public boolean verifyCaptcha(String token, String text) {
        if (StringUtils.isEmpty(text)) {
            return false;
        }
        String captchKey = BCConstants.REDIS_KEY.CAPTCH_KEY + token;
        String code = cacheTemplate.get(captchKey, String.class);
        if(!StringUtils.isEmpty(code) && !StringUtils.isEmpty(text)){
            text = text.toUpperCase();
            code = code.toUpperCase();
            return text.equals(code);
        }else{
            return false;
        }

    }

}
