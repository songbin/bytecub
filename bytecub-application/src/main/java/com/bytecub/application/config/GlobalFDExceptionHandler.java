package com.bytecub.application.config;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.util.IOUtils;
import com.bytecub.common.domain.DataResult;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.utils.JSONProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * @author songbin
 * @version Id: GlobalExceptionHandler.java, v 0.1 2018/9/3 17:23  Exp $$
 */
@ControllerAdvice
@Slf4j
public class GlobalFDExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        DataResult responseDto;
        if(ex instanceof BCGException){
            BCGException yie = (BCGException)ex;
            Integer code = yie.getErrorCode();
            if(!code.equals(BCErrorEnum.INVALID_TOKEN.getCode())
            && !code.equals(BCErrorEnum.INVALID_ROLE.getCode())){
                log.warn("异常", ex);
            }else{
                log.warn("{}-{}", ((BCGException) ex).getMsg(), ((BCGException) ex).getExtraMsg());
            }
            responseDto = new DataResult(yie.getErrorCode(), yie.getMsg(), null);
        }else if(ex instanceof AccessDeniedException){
            responseDto = new DataResult(BCErrorEnum.INVALID_ROLE.getCode(), BCErrorEnum.INVALID_ROLE.getMsg(), null);
        }else if(ex instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException me = (MethodArgumentNotValidException)ex;
            BindingResult bindingResult = me.getBindingResult();
            String msg = "参数校验失败:";
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                //一次只显示一个异常参数，免得别人搞破坏,对接的人慢慢调去吧
                msg += fieldError.getDefaultMessage() + " ";
                break;
            }
            log.warn("{}", msg);
            responseDto = new DataResult(BCErrorEnum.INVALID_PARAM.getCode(), msg, null);

        }else if(ex instanceof ConstraintViolationException){
            ConstraintViolationException me = (ConstraintViolationException)ex;
            String bindingResult = me.getMessage();
//            String msg = "参数校验失败:";
//            for (FieldError fieldError : bindingResult.getFieldErrors()) {
//                //一次只显示一个异常参数，免得别人搞破坏,对接的人慢慢调去吧
//                msg += fieldError.getDefaultMessage() + " ";
//                break;
//            }
            log.warn("{}", bindingResult);
            responseDto = new DataResult(BCErrorEnum.INVALID_PARAM.getCode(), bindingResult, null);
        }else if(ex instanceof BindException){
            log.warn("异常", ex);
            BindException bindException = (BindException)ex;
            responseDto = new DataResult(BCErrorEnum.INVALID_PARAM.getCode(),bindException.getFieldError().getDefaultMessage(),null);
        } else if (ex instanceof MissingServletRequestParameterException) {
            log.warn("异常", ex);
            responseDto = new DataResult(BCErrorEnum.LACK_PARAM_EXCEPTION.getCode() ,BCErrorEnum.LACK_PARAM_EXCEPTION.getMsg(), null);
        } else  {
            log.warn("异常", ex);
            responseDto = new DataResult(BCErrorEnum.INNER_EXCEPTION.getCode(), BCErrorEnum.INNER_EXCEPTION.getMsg(), null);
        }

        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(JSONProvider.toJSONString(responseDto));
        } catch(Exception e){
            log.info("拦截器异常", e);
        } finally {
            IOUtils.close(out);
        }

        return null;
    }
}
