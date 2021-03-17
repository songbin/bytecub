package com.bytecub.gateway.mqtt.config;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.gateway.mq.DeviceUpMessageBo;
import com.bytecub.gateway.mq.producer.DeviceReplyMessageProducer;
import com.bytecub.gateway.mq.producer.DeviceUpMessageProducer;
import com.bytecub.protocol.base.IBaseProtocol;
import com.bytecub.storage.IDataCenterService;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songbin
 * @version Id: BCMqttCallback.java, v 0.1 2018/12/13 13:52  Exp $$
 */
public class BCMqttCallback implements MqttCallback {
    private static final Logger             logger = LoggerFactory.getLogger(BCMqttCallback.class);
    private              MqttClient         client;
    private              MqttConnectOptions options;
    private              String             urls;
    private IBaseProtocol baseProtocol = null;
    private String             productCode;
    private IDataCenterService      dataCenterService = null;
  //  private DeviceUpMessageProducer gwQueueProducer   = null;



    public BCMqttCallback(MqttClient client, MqttConnectOptions options, String urls, IBaseProtocol baseProtocol,String productCode) {
        this.client = client;
        this.options = options;
        this.urls = urls;

        this.baseProtocol = baseProtocol;
        this.productCode = productCode;


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
                String topic = BCConstants.MQTT.GLOBAL_UP_PREFIX + "#";
                this.client.subscribe(topic);
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
            logger.info("{}接收消息主题 : {}   消息内容: {}", client.getServerURI(), topic, msg);
            DeviceUpMessageBo bo = new DeviceUpMessageBo();
            bo.setTopic(topic);
            bo.setSourceMsg(message.getPayload());
            bo.setPacketId((long)message.getId());

            if(topic.endsWith(BCConstants.TOPIC.MSG_REPLY) || topic.endsWith(BCConstants.TOPIC.PROP_GET_REPLY)){
                DeviceReplyMessageProducer.send(bo);
            }else{
                DeviceUpMessageProducer.send(bo);
            }

        }catch (Exception e){
            logger.warn("mqtt 订阅消息异常", e);
        }

    }

}
