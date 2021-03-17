package com.bytecub.application.config;

import com.bytecub.common.annotations.NoTokenAllowedAnnotation;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.mdm.dao.po.UserPo;
import com.bytecub.plugin.redis.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private CacheTemplate cacheTemplate;

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
        String token = httpServletRequest.getHeader(BCConstants.GLOBAL.TOKEN);
        // 跨域的预请求OPTIONS 不做限制
        if (RequestMethod.OPTIONS.name().equals(httpServletRequest.getMethod())) {
            return true;
        }
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
//        if(StringUtils.isEmpty(token)){
//            return true;
//        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        Class classs = handlerMethod.getBeanType();

//        if(!method.isAnnotationPresent(NoCheckVersionAnnotation.class)){
//            checkAppVersion(os,appVersion);
//        }

        String userToken = BCConstants.REDIS_KEY.TOKEN + token;
        UserPo tokenInfo = cacheTemplate.get(userToken, UserPo.class);
        if(tokenInfo == null){//token为空
            if(!classs.isAnnotationPresent(NoTokenAllowedAnnotation.class)){//class上加了标签,则不判断token是否存在
                //log.debug("class 上没有加标签，需要看method是否有标签");
                //能进来说明没加标签
                if(!method.isAnnotationPresent(NoTokenAllowedAnnotation.class)){//method上加了标签,则不判断token是否存在
                    //log.debug("method 上没有加标签，需要抛出错误");
                    //能进来说明没加标签

                    log.error("访问路径 ：{} token : {}已经失效", httpServletRequest.getRequestURI(),token);
                    throw new BCGException(BCErrorEnum.INVALID_TOKEN.getCode(),BCErrorEnum.INVALID_TOKEN.getMsg());
                }else{
                    //log.debug("method 上有加标签，");
                }
            }else{
                //log.debug("class 上有加标签，");
            }
            return true;//token == null 将不进行
        }
        //默认都进行初始密码判断，如果不需要判断的加上OrignalPwdCheckAnnotation注解
//        if(!classs.isAnnotationPresent(NoPwdCheckAnnotation.class)){
//            if(!method.isAnnotationPresent(NoPwdCheckAnnotation.class)){
//                if(checkOriginalPass(tokenInfo.getUserPass())){
//                    throw new BCException(BCErrorEnum.REMIND_CHANGE_USERPWD.getCode(),BCErrorEnum.REMIND_CHANGE_USERPWD.getMsg());
//                }
//            }
//        }

        UserAPPTokenInfoThreadLocal.setCurrentToken(tokenInfo);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

        UserAPPTokenInfoThreadLocal.cleanCurrentToken();
    }


//    private boolean checkOriginalPass(String userPass){
//        if(userPass.equals(IShareConstants.UserToken.ORIGNAL_USER_PASS)){
//            return true;
//        }else{
//            return false;
//        }
//    }
}
