package com.bytecub.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  * ByteCub.cn.
 *  * Copyright (c) 2020-2021 All Rights Reserved.
 *  * 自定义一个简单的分布式任务锁
 *  * @author bytecub@163.com  songbin
 *  * @Date 2021/3/12  Exp $$
 *  
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JobLockAnnotation {
    /**任务名*/
    String name() default "";
    /**任务间隔时间，在这个时间内分布式应用不能重复执行
     * 单位为秒
     * */
    int duration();

}
