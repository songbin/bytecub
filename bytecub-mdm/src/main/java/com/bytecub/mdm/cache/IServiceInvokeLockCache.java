package com.bytecub.mdm.cache;

/**
 *  * ByteCub.cn.
 *  * Copyright (c) 2020-2021 All Rights Reserved.
 *  * 
 *  * @author bytecub@163.com  songbin
 *  * @Date 2021/3/15  Exp $$
 *  
 */
public interface IServiceInvokeLockCache {
    Boolean lock(String messageId);
}
