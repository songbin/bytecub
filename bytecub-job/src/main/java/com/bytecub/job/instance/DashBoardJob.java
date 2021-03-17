package com.bytecub.job.instance;

import com.bytecub.common.annotations.JobLockAnnotation;
import com.bytecub.mdm.service.IDashBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**首页大屏数据 定时任务
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  * @author bytecub@163.com songbin
 *  * @version Id: DevicceStatusJob.java, v 0.1 2021-01-25  Exp $$  
 */
@Slf4j
@Service
public class DashBoardJob {
    @Autowired IDashBoardService dashBoardService;

    @Scheduled(cron = "0 0 1 * * ? ")
    @JobLockAnnotation(name = "DashBoardJob", duration = 60)
    public void fixedJob() {
        try {
           this.cache();
        } catch (Exception e) {
            log.warn("设备状态定时任务异常", e);
            dashBoardService.index();
        }
    }

    private void cache(){
        Long start = System.currentTimeMillis();
        log.info("大屏统计任务开始:" +  start);
        dashBoardService.index();
        long dura = (System.currentTimeMillis() - start)/100;
        log.info("同屏统计结束，耗时:{}秒 ", dura);
    }
}
