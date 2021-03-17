package com.bytecub.job.instance;

import com.bytecub.common.annotations.JobLockAnnotation;
import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.DevQueryReqDto;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.common.domain.gateway.mq.DeviceActiveMqBo;
import com.bytecub.mdm.cache.IDeviceOfflineCache;
import com.bytecub.mdm.service.IDeviceService;
import com.bytecub.mqtt.service.biz.MqttRemoteService;
import com.bytecub.mqtt.service.state.ClientManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  * @author bytecub@163.com songbin
 *  * @version Id: DevicceStatusJob.java, v 0.1 2021-01-25  Exp $$  
 */
@Slf4j
@Service
public class DeviceStatusJob {
    @Autowired IDeviceService deviceService;
    @Autowired
    IDeviceOfflineCache deviceOfflineCache;

    @Scheduled(cron = "${bc.job.cron.devicestatus}")
    @JobLockAnnotation(name = "DeviceStatusJob", duration = 60)
    public void fixedJob() {
        try {
            log.info("设备状态定时任务:" + System.currentTimeMillis()  + "线程:"+ Thread.currentThread().getName());
            deviceOfflineCache.removeExpire();
        } catch (Exception e) {
            log.warn("设备状态定时任务异常", e);
        }
    }

    private List<String> getDeviceList(){
        List<String> devList = new ArrayList<>();
        PageReqDto<DevQueryReqDto> pageReqDto = new PageReqDto<>();
        DevQueryReqDto devQueryReqDto = new DevQueryReqDto();
        devQueryReqDto.setActiveStatus(1);
        devQueryReqDto.setEnableStatus(1);
        pageReqDto.setParamData(devQueryReqDto);
        int startPageNo = 0;
        pageReqDto.setLimit(100);
        while(true){
            startPageNo = startPageNo + 1;
            pageReqDto.setPageNo(startPageNo);
            PageResDto<DevicePageResDto> result =  deviceService.queryByPage(pageReqDto);
            List<DevicePageResDto> list = result.getResultData();
            if(CollectionUtils.isEmpty(list)){
                break;
            }
            for(DevicePageResDto item:list){
                devList.add(item.getDeviceCode());
            }
        }
        return devList;
    }
}
