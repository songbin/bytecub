package com.bytecub.gateway.mq.mqttclient;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: BcPubMqttClient.java, v 0.1 2021-01-06  Exp $$
  */
@Service
@Slf4j
public class BcPubMqttClient {
    private MqttClient mqttClient;
    @Value("${bytecub.mqtt.client.url}")
    String mqttUrl;
    @Value("${bytecub.mqtt.client.timeout:30}")
    int    timeout;
    @Value("${bytecub.mqtt.client.keepalive:30}")
    int    keepAlive;
    private MqttConnectOptions options = new MqttConnectOptions();
    /**
     * 用于标记是否连接成功
     */
    private boolean isConnected = false;
    public void init() {
        try {
            this.mqttClient = new MqttClient(mqttUrl, this.genClientId(), new MemoryPersistence());
            this.options.setCleanSession(true);
            this.options.setAutomaticReconnect(true);
            this.options.setConnectionTimeout(timeout);
            this.options.setKeepAliveInterval(keepAlive);
            try {
                this.mqttClient.connect(options);
                this.mqttClient.setCallback(new BCPubMqttCallback(this.mqttClient, options, mqttUrl));
                this.isConnected = true;
            } catch (Exception e) {
                log.warn("连接MQTT服务器", e);
                this.isConnected = false;
                this.tryConnect();

            }
        } catch (Exception e) {
            log.warn("连接MQTT服务器", e);
            System.exit(-1);
        }
    }

    private final String genClientId() {
        String clientId = BCConstants.MQTT.SYS_CLIENT_TOPIC_PREFIX + UUID
                .randomUUID().toString().replaceAll("-", "");
        return clientId;
    }

    /**
     * 用于处理第一次连接失败的情况
     * */
    private void tryConnect() {
        int count  = 1;
        while (!this.isConnected){
            try{
                int sleepTime = (1000*count)%(1000*60*10);
                Thread.sleep(sleepTime);
                log.info("连接[{}]断开，尝试重连第{}次", this.mqttUrl, count++);
                this.mqttClient.connect(this.options);
                this.isConnected = true;
            }catch (Exception e){
                log.warn("首次重连异常");
            }

        }
    }

    /**
     * 发布，默认qos为1，非持久化
     *
     * @param topic
     * @param pushMessage
     */
    public void publish(String topic, byte[] pushMessage) {
        publish(MqttQoS.AT_LEAST_ONCE.value(), false, topic, pushMessage);
    }

    /**
     * 发布
     *
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) {
        this.publish(qos, retained, topic, pushMessage.getBytes());
    }

    public void publish(int qos, boolean retained, String topic, byte[] pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage);
        MqttTopic mTopic = this.mqttClient.getTopic(topic);
        if (null == mTopic) {
            log.error("topic not exist:{}", topic);
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            log.warn("向主题[{}]发布消息异常{}", topic, e);
            throw BCGException.genException(BCErrorEnum.INVALID_MQTT_USER);
        } catch (MqttException e) {
            log.warn("向主题[{}]发布消息异常{}", topic, e);
            throw BCGException.genException(BCErrorEnum.INVALID_MQTT_USER);
        }
    }
}
