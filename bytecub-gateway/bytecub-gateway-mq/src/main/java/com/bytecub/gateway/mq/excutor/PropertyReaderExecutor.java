package com.bytecub.gateway.mq.excutor;

import com.bytecub.common.biz.TopicBiz;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.common.domain.message.DeviceDownMessage;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.domain.gateway.mq.PropertyReaderMessageBo;
import com.bytecub.gateway.mq.mqttclient.BcPubMqttClient;
import com.bytecub.mdm.dao.po.ProductPo;
import com.bytecub.mdm.service.IProductService;
import com.bytecub.protocol.base.IBaseProtocol;
import com.bytecub.protocol.service.IProtocolUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: MQTTConsumeMonitor.java, v 0.1 2020-12-09  Exp $$  
 */
@Service
@Slf4j
public class PropertyReaderExecutor {

    @Autowired
    BcPubMqttClient bcPubMqttClient;
    @Autowired
    IProductService productService;
    @Autowired
    IProtocolUtilService protocolUtilService;
    /***
     * 具体线程
     */
    @Async(BCConstants.TASK.PROPERTY_READER_NAME)
    public void execute(PropertyReaderMessageBo msg) {
        try {
            this.sendMsg(msg.getDevicePageResDto(), msg.getDeviceDownMessage());
        } catch (Exception e) {
            log.warn("内部队列消费异常", e);

        }

    }
    /**
     * 发送请求到设备端
     */
    private void sendMsg(DevicePageResDto devicePageResDto, DeviceDownMessage deviceDownMessage) {
        IBaseProtocol baseProtocol = this.queryProtocolByProductCode(devicePageResDto.getProductCode());
        String topic = TopicBiz.buildPropertyGet(devicePageResDto.getDeviceCode(), deviceDownMessage.getProductCode());
        byte[] msg = baseProtocol.encode(topic, devicePageResDto.getDeviceCode(), deviceDownMessage);
        bcPubMqttClient.publish(topic, msg);
    }

    /** 根据产品编码查询协议实现 */
    private IBaseProtocol queryProtocolByProductCode(String productCode) {
        try {
            ProductPo productPo = productService.queryByCode(productCode);
            return protocolUtilService.queryProtocolInstanceByCode(productPo.getProtocolCode());
        } catch (Exception e) {
            log.warn("", e);
            throw BCGException.genException(BCErrorEnum.INNER_EXCEPTION);
        }
    }
}
