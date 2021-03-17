package com.bytecub.gateway.mq.redis.publish;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.gateway.mq.PropertySetMessageBo;
import com.bytecub.plugin.redis.CacheTemplate;
import com.bytecub.utils.IdGenerate;
import com.bytecub.utils.SpringContextUtil;

/**
 *  * ByteCub.cn.
 *  * Copyright (c) 2020-2021 All Rights Reserved.
 *  * 
 *  * @author bytecub@163.com  songbin
 *  * @Date 2021/3/13  Exp $$
 *  
 */
public class PropertySetPublisher {
    private static CacheTemplate cacheTemplate = SpringContextUtil.getBean(CacheTemplate.class);

    static public void send(PropertySetMessageBo bo){
        bo.setMessageId(IdGenerate.genId());
        cacheTemplate.publish(BCConstants.REDIS_CHANNEL.PROP_SET, bo);
    }
}
