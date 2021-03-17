package com.bytecub.gateway.mqtt.config;

import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.protocol.base.IBaseProtocol;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * MQTT客户端
 *
 * @author songbin
 * @version Id: BCMqttClient.java, v 0.1 2018/12/13 13:32  Exp $$
 */
public class BCMqttClient {
    private static final Logger             logger  = LoggerFactory.getLogger(BCMqttClient.class);
    private              MqttClient         client;
    private              MqttConnectOptions options = new MqttConnectOptions();
    private              String             urls;
    /**MQTT服务器ID*/
    private String serverId;

    /**
     * 用于标记是否连接成功
     */
    private boolean isConnected = false;


    public void connect(String urls, String clientID,
                        String username, String password,
                        int timeout, int keepalive,
                        IBaseProtocol baseProtocol,
                        String productCode) {
        MqttClient client;
        try {
            client = new MqttClient(urls, clientID, new MemoryPersistence());
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){
                options.setUserName(username);
                options.setPassword(password.toCharArray());
            }
            options.setConnectionTimeout(timeout);
            options.setKeepAliveInterval(keepalive);
//            String[] url = new String[1];
//            url[0] = urls;
         //   options.setServerURIs(url);
            this.client = client;
            try {
                client.setCallback(new BCMqttCallback(client, options, urls, baseProtocol, productCode));
                client.connect(options);
                this.urls = urls;
            } catch (Exception e) {
                logger.warn("连接MQTT服务器[{} username:{}  password:{}]异常:{}",
                        urls, username, password, e);
                this.isConnected = false;
                this.tryConnect();

            }
        } catch (Exception e) {
            logger.warn("连接MQTT服务器[{} username:{}  password:{}]异常:{}",
                    urls, username, password, e);

        }
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
                logger.info("连接[{}]断开，尝试重连第{}次", this.client.getServerURI(), count++);
                this.client.connect(this.options);
                this.isConnected = true;
            }catch (Exception e){
                logger.warn("首次重连异常");
            }

        }
    }

    /**
     * 发布，默认qos为0，非持久化
     *
     * @param topic
     * @param pushMessage
     */
    public void publish(String topic, String pushMessage) {
        publish(0, false, topic, pushMessage);
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
        MqttTopic mTopic = this.client.getTopic(topic);
        if (null == mTopic) {
            logger.error("topic not exist:{}", topic);
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            logger.warn("向主题[{}]发布消息异常{}", topic, e);
            throw BCGException.genException(BCErrorEnum.INVALID_MQTT_USER);
        } catch (MqttException e) {
            logger.warn("向主题[{}]发布消息异常{}", topic, e);
            throw BCGException.genException(BCErrorEnum.INVALID_MQTT_USER);
        }
    }

    /**
     * 订阅某个主题，qos默认为0
     *
     * @param topic
     */
    public void subscribe(String topic) {
        subscribe(topic, 0);
    }

    /**
     * 订阅某个主题
     *
     * @param topic
     * @param qos
     */
    public void subscribe(String topic, int qos) {
        try {
            if(StringUtils.isEmpty(topic)){
                logger.info("空主题，不订阅");
                return;
            }
            this.client.subscribe(topic, qos);
        } catch (MqttException e) {
            logger.warn("订阅主题异常", e);
        }
    }


    public String getUrls(){
        return this.urls;
    }


    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
