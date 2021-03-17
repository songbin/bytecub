package com.bytecub.gateway.mq.redis.publish;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.gateway.mq.ServiceInvokeMessageBo;
import com.bytecub.plugin.redis.CacheTemplate;
import com.bytecub.utils.SpringContextUtil;

/**
 *  * ByteCub.cn.
 *  * Copyright (c) 2020-2021 All Rights Reserved.
 *  * 
 *  * @author bytecub@163.com  songbin
 *  * @Date 2021/3/13  Exp $$
 *  
 */
public class ServiceInvokePublisher {
    private static CacheTemplate cacheTemplate = SpringContextUtil.getBean(CacheTemplate.class);

    static public void send(ServiceInvokeMessageBo bo){
        cacheTemplate.publish(BCConstants.REDIS_CHANNEL.SERVICE_INVOKE, bo);
    }
}
