package com.bytecub.gateway.mq.redis.consume;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.gateway.mq.PropertyReaderMessageBo;
import com.bytecub.common.domain.gateway.mq.PropertySetMessageBo;
import com.bytecub.common.domain.gateway.mq.ServiceInvokeMessageBo;
import com.bytecub.gateway.mq.producer.PropertyReaderMessageProducer;
import com.bytecub.gateway.mq.producer.PropertySetMessageProducer;
import com.bytecub.gateway.mq.producer.ServiceInvokeMessageProducer;
import com.bytecub.utils.JSONProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 *  * ByteCub.cn.
 *  * Copyright (c) 2020-2021 All Rights Reserved.
 *  * 
 *  * @author bytecub@163.com  songbin
 *  * @Date 2021/3/13  Exp $$
 *  
 */
@Component
@Slf4j
public class ChannelConsume implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] bytes) {
       try{
           this.process(message, bytes);
       }catch (Exception e){
           log.warn("redis订阅者处理消息异常, 系统即将终止", e);
           System.exit(-1);
       }
    }

    private void process(Message message, byte[] bytes) throws Exception{
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        String byteBody = new String(bytes, BCConstants.GLOBAL.CHARSET_UFT8);
        log.info("channel:{} 收到消息:{}  bytes:{}", channel, body, byteBody);
        switch (channel){
            case BCConstants.REDIS_CHANNEL.PROP_SET:
                PropertySetMessageBo propertySetMessageBo = JSONProvider.parseObject(body, PropertySetMessageBo.class);
                PropertySetMessageProducer.send(propertySetMessageBo);
                break;
            case BCConstants.REDIS_CHANNEL.SERVICE_INVOKE:
                ServiceInvokeMessageBo invokeMessageBo = JSONProvider.parseObject(body, ServiceInvokeMessageBo.class);
                ServiceInvokeMessageProducer.send(invokeMessageBo);
                break;
            case BCConstants.REDIS_CHANNEL.PROP_READER:
                PropertyReaderMessageBo readerMessageBo = JSONProvider.parseObject(body, PropertyReaderMessageBo.class);
                PropertyReaderMessageProducer.send(readerMessageBo);
                break;
            default:
                log.warn("redis收到非法的消息,channel:{} body:{}", channel, body);
                return;
        }
    }
}
