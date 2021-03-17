package com.bytecub.mdm.cache;

import com.bytecub.common.constants.BCConstants;

/**
 *  * ByteCub.cn.
 *  * Copyright (c) 2020-2021 All Rights Reserved.
 *  * 
 *  * @author bytecub@163.com  songbin
 *  * @Date 2021/3/15  Exp $$
 *  
 */
public interface IPropSetLockCache {
    Boolean lock(String messageId);
}
