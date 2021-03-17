package com.bytecub.application.config;

import com.bytecub.common.annotations.NoTokenAllowedAnnotation;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.mdm.dao.po.OpenAuthPo;
import com.bytecub.mdm.dao.po.UserPo;
import com.bytecub.mdm.service.IDeviceService;
import com.bytecub.mdm.service.IOpenAuthService;
import com.bytecub.plugin.redis.CacheTemplate;
import com.bytecub.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
public class OpenApiInterceptor implements HandlerInterceptor {

    @Autowired
    private IOpenAuthService openAuthService;

    /**
     *  Token数据
     * @param httpServletRequest 请求
     * @param httpServletResponse 返回
     * @param object 其他数据
     * @return 验证消息
     * @throws Exception 异常信息
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String key = httpServletRequest.getHeader("key");
        String nonce = httpServletRequest.getHeader("nonce");
        String timestamp = httpServletRequest.getHeader("timestamp");
        String sign = httpServletRequest.getHeader("sign");
        // 跨域的预请求OPTIONS 不做限制
        if (RequestMethod.OPTIONS.name().equals(httpServletRequest.getMethod())) {
            return true;
        }
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        Boolean ret = this.verify(key,nonce, timestamp, sign);
        if(!ret){
            throw new BCGException(BCErrorEnum.INVALID_SIGN);
        }
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

        UserAPPTokenInfoThreadLocal.cleanCurrentToken();
    }

    private Boolean verify(String key, String nonce, String timestamp, String sign){
        OpenAuthPo po = openAuthService.selectByKey(key);
        if(null == po){
            throw BCGException.genException(BCErrorEnum.APP_NO_EXIST);
        }
        String uncode = key + "|" + nonce + "|" + timestamp + "|" + po.getAppSecret();
        String encode = Md5Utils.md5(uncode);
        if(encode.equals(sign)){
            return true;
        }
        return false;
    }

}
