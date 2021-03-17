package com.bytecub.gateway.mqtt.network.impl;

import com.bytecub.common.biz.TopicBiz;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.bo.ProtocolBo;
import com.bytecub.common.enums.NetworkEnum;
import com.bytecub.gateway.mqtt.network.IMQTTMonitorTask;
import com.bytecub.gateway.mqtt.network.IMQTTRecvDevMsgService;
import com.bytecub.mdm.dao.po.ProductPo;
import com.bytecub.mdm.dao.po.ProtocolPo;
import com.bytecub.mdm.service.IProductService;
import com.bytecub.mdm.service.IProtocolService;
import com.bytecub.protocol.base.IBaseProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: RecvDevMsgServiceImpl.java, v 0.1 2020-12-03  Exp $$
  */
@Slf4j
@Service
public class MQTTRecvDevMsgServiceImpl implements IMQTTRecvDevMsgService {
    @Autowired
    IProductService productService;
    @Autowired
    IProtocolService protocolService;
    @Autowired
    IMQTTMonitorTask           monitorTask;
    @Value("${bytecub.mqtt.client.url}")
    String mqttUrl;
    @Override
    public void startListen(){
        try {
            String topic = BCConstants.MQTT.GLOBAL_UP_PREFIX + "#";
            monitorTask.execute(mqttUrl, topic,null,null);
            log.info("内部MQTT监听启动完成");
        } catch (Exception e) {
            log.warn("MQTT GATEWAY 启动异常", e);
        }

    }
    private void subscribe(){
        this.scanProtocol();
        List<ProductPo> products =  productService.listAllValid();
        for(ProductPo item:products){
            if(!item.getTransportList().contains(NetworkEnum.MQTT.getType())){
                continue;
            }
            String topic = TopicBiz.buildTopicByProduct();
            ProtocolPo query = new ProtocolPo();
            query.setProtocolCode(item.getProtocolCode());
            query.setDelFlag(0);
            query.setProtocolStatus(1);
            List<ProtocolBo> list = protocolService.listByCondition(query);
            if(CollectionUtils.isEmpty(list)){
                continue;
            }
            monitorTask.execute(mqttUrl, topic, list.get(0).getProtocolHandle(), item.getProductCode());
        }
    }
    private void scanProtocol(){
      //  protocolMap = SpringContextUtil.getBeanWithAnnotation(BcProtocolAnnotation.class);
        log.debug("test");
    }

}
