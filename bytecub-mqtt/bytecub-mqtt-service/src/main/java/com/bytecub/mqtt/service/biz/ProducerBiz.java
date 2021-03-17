package com.bytecub.mqtt.service.biz;

import java.net.InetSocketAddress;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytecub.common.biz.MqttUtil;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.gateway.mq.DeviceActiveMqBo;
import com.bytecub.gateway.mq.producer.DeviceActiveMqProducer;
import com.bytecub.mqtt.domain.bo.ContextBo;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProducerBiz.java, v 0.1 2021-01-05  Exp $$
  */
public class ProducerBiz {
    private static final Logger logger = LoggerFactory.getLogger(ProducerBiz.class);
    /**发送设备上线下消息*/
    public static void sendActiveMQ(ContextBo contextBo, Boolean active){
        try{
            if(active){
                sendActiveOnlineMQ(contextBo);
            }else {
                sendActiveOfflineMQ(contextBo.getClientId());
            }
        }catch (Exception e){
            logger.warn("发生设备上下线消息异常{}", contextBo, e);
        }
    }
    public static void sendActiveOnlineMQ(ContextBo contextBo){
        InetSocketAddress address = (InetSocketAddress)contextBo.getHandlerContext().channel().remoteAddress();
        String host = address.getAddress().getHostAddress();
        int port = address.getPort();
        if(contextBo.getClientId().startsWith(BCConstants.MQTT.SYS_CLIENT_TOPIC_PREFIX)){
            return;
        }
        DeviceActiveMqBo deviceActiveMqBo = new DeviceActiveMqBo();
        String deviceCode = MqttUtil.fetchDeviceCode(contextBo.getUserName());
        deviceActiveMqBo.setActive(true);
        deviceActiveMqBo.setDeviceCode(deviceCode);
        deviceActiveMqBo.setHost(host);
        deviceActiveMqBo.setPort(port);
        deviceActiveMqBo.setTimestamp(new Date());
        DeviceActiveMqProducer.send(deviceActiveMqBo);
    }
    public static void sendActiveOfflineMQ(String clientId){
        try{
            DeviceActiveMqBo deviceActiveMqBo = new DeviceActiveMqBo();
            deviceActiveMqBo.setActive(false);
            deviceActiveMqBo.setDeviceCode(clientId);
            DeviceActiveMqProducer.send(deviceActiveMqBo);
        }catch (Exception e){
            logger.warn("发生设备下线消息异常{}", clientId, e);
        }
    }
}
