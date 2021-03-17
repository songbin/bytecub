package com.bytecub.gateway.mq.config;

import com.bytecub.common.constants.BCConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 设备上下线线程池
  * @author bytecub@163.com  songbin
  * @version Id: DeviceActiveTaskConfig.java, v 0.1 2021-01-05  Exp $$
  */
@Configuration
@EnableAsync
public class DeviceActiveTaskConfig {
    // 声明一个线程池(并指定线程池的名字)
    @Bean(BCConstants.TASK.DEVICE_ACTIVE_NAME)
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数：线程池创建时候初始化的线程数
        executor.setCorePoolSize(20);
        //最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(100);
        //允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        executor.setQueueCapacity(1000);
        // 线程池对拒绝任务的处理策略
        //丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        //线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix(BCConstants.TASK.DEVICE_ACTIVE_NAME);
        executor.initialize();
        return executor;
    }
}
