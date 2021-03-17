package com.bytecub.job.aspectj;

import com.bytecub.common.annotations.JobLockAnnotation;
import com.bytecub.mdm.cache.IJobLockCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 *  * ByteCub.cn.
 *  * Copyright (c) 2020-2021 All Rights Reserved.
 *  * 
 *  * @author bytecub@163.com  songbin
 *  * @Date 2021/3/12  Exp $$
 *  
 */
@Aspect
@Component
@Slf4j
public class JobLockAspect {
    @Autowired
    IJobLockCache jobLockCache;

    // 配置织入点
    @Pointcut("@annotation(com.bytecub.common.annotations.JobLockAnnotation)")
    public void jobLockAspect() {
    }

    @Around("jobLockAspect()")
    public void doBefore(ProceedingJoinPoint proceeding) {
        //获取redis锁
        Boolean flag = this.getLock(proceeding,  System.currentTimeMillis());
        if (flag) {
            try {
                Long start = System.currentTimeMillis();
                proceeding.proceed();
                Long end = System.currentTimeMillis();
                JobLockAnnotation jobLockAnnotation = this.getAnnotationLog(proceeding);
                long duration = jobLockAnnotation.duration() *1000;
                long retain = end - start;
                if(retain < duration){
                    long sleep = duration - retain  + 500;
                    Thread.sleep(sleep);
                }

            } catch (Throwable throwable) {
                log.warn("分布式锁执行发生异常", throwable);
            } finally {
                // 删除锁
                this.delLock(proceeding);
            }
        } else {
            log.info("其他系统正在执行此项任务");
        }
    }
    /**
     * 是否存在注解，如果存在就获取
     */
    private JobLockAnnotation getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(JobLockAnnotation.class);
        }
        return null;
    }
    /**
     * 获取锁
     *
     * @param proceeding
     * @return
     */
    private boolean getLock(ProceedingJoinPoint proceeding, long currentTime) {
        //获取注解中的参数
        JobLockAnnotation jobLockAnnotation = this.getAnnotationLog(proceeding);
        Integer duration = jobLockAnnotation.duration();
        String jobName = jobLockAnnotation.name();
        Boolean lockRet = jobLockCache.lock(jobName, duration);

        if (lockRet) {
            return true;
        } else {
            // 如果当前时间与锁的时间差, 大于保护时间,则强制删除锁(防止锁死)
            log.warn("其他服务在执行任务{}, 当前线程:{}", jobName, Thread.currentThread().getName());
            return false;
        }
    }

    /**
     * 删除锁
     *
     * @param proceeding
     */
    private void delLock(ProceedingJoinPoint proceeding) {
        JobLockAnnotation jobLockAnnotation = this.getAnnotationLog(proceeding);
        Integer duration = jobLockAnnotation.duration();
        String jobName = jobLockAnnotation.name();
        jobLockCache.unlock(jobName);
    }





}
