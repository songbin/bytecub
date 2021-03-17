package com.bytecub.gateway.mq.mqttclient;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songbin
 * @version Id: BCMqttCallback.java, v 0.1 2018/12/13 13:52  Exp $$
 */
public class BCPubMqttCallback implements MqttCallback {
    private static final Logger             logger = LoggerFactory.getLogger(BCPubMqttCallback.class);
    private              MqttClient         client;
    private              MqttConnectOptions options;
    private              String             urls;




    public BCPubMqttCallback(MqttClient client, MqttConnectOptions options, String urls) {
        this.client = client;
        this.options = options;
        this.urls = urls;
    }

    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        logger.info("mqtt 连接丢失", cause);
        int count = 1;
       // int sleepTime = 0;
        boolean willConnect = true;
        while (willConnect){
            try{
                Thread.sleep(100);
                logger.info("连接[{}]断开，尝试重连第{}次", this.client.getServerURI(), count++);
                this.client.connect(this.options);
                logger.info("重连成功");
                willConnect = false;
            }catch (Exception e){
                logger.warn("重连异常", e);
            }

        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //System.out.println("deliveryComplete---------" + token.isComplete());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        try{
            String msg = new String(message.getPayload());
            logger.warn("发布消息客户端{}接收消息主题 : {}   消息内容: {}", client.getServerURI(), topic, msg);

        }catch (Exception e){
            logger.warn("mqtt 订阅消息异常", e);
        }

    }

}
