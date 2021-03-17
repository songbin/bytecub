package com.bytecub.application.controller;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.DataResult;
import com.bytecub.mdm.cache.IJobLockCache;
import com.bytecub.plugin.es.SampleEsClient;
import com.bytecub.storage.IDataCenterService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: EchoController.java, v 0.1 2020-12-02  Exp $$
  */

@Slf4j
@RestController
@Api(value = "测试")
@RequestMapping("test")
public class TestController {
    @Autowired IDataCenterService dataCenterService;
    @Autowired SampleEsClient     esClient;
    @Autowired
    IJobLockCache jobLockCache;

    /**获取指定协议属性*/
    @RequestMapping(value = "passwd", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('system:user:add')")
    public DataResult password( String password){
        try{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String newPassword = encoder.encode(password);
            return DataResult.ok(newPassword);
        }catch (Exception e){
            log.warn("", e);
            return DataResult.fail("异常");
        }
    }

    /**获取指定协议属性*/
    @RequestMapping(value = "no", method = RequestMethod.POST)
    public DataResult nono( String passwd){
        try{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String newPassword = encoder.encode(passwd);
            return DataResult.ok(newPassword);
        }catch (Exception e){
            log.warn("", e);
            return DataResult.fail("异常");
        }
    }

    /**获取指定协议属性*/
    @RequestMapping(value = "lock", method = RequestMethod.POST)
    public DataResult lock( String jobName){
        try{
            Boolean ret = jobLockCache.lock(jobName, 60);
            String data = ret ? "成功执行": "其他服务在执行";
            return DataResult.ok(data);
        }catch (Exception e){
            log.warn("", e);
            return DataResult.fail("异常");
        }
    }

    public static void main(String[] args){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPassword = encoder.encode("E10ADC3949BA59ABBE56E057F20F883E");
        System.out.println(newPassword);

    }



}
