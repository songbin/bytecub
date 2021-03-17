package com.bytecub.mdm.cache;

import io.swagger.models.auth.In;

import java.util.Date;

/**
 *  * ByteCub.cn.
 *  * Copyright (c) 2020-2021 All Rights Reserved.
 *  * 
 *  * @author bytecub@163.com  songbin
 *  * @Date 2021/3/12  Exp $$
 *  
 */
public interface IJobLockCache {
    Boolean lock(String jobName, Integer seconds);
    void unlock(String jobName);
    Date getLock(String jobName);
}
