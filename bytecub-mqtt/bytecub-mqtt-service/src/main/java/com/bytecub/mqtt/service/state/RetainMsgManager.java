package com.bytecub.mqtt.service.state;


import com.bytecub.mqtt.domain.bo.RetainMsgBo;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 保留消息管理
 * 1个Topic只有唯一的retain消息，Broker会保存每个Topic的最后一条retain消息。
 * 每个Client订阅Topic后会立即读取到retain消息，不必要等待发送。
 * 订阅Topic时可以使用通配符，就会收到匹配的每个Topic的retain消息。
 *
 * 发布消息时把retain设置为true，即为保留信息。
 * 如果需要删除retain消息，可以发布一个空的retain消息，因为每个新的retain消息都会覆盖最后一个retain消息。
 * Created by songbin on 2020-11-24.
 */
public class RetainMsgManager {
    private static final Logger                   logger    = LoggerFactory.getLogger(RetainMsgManager.class);
    /**存储每个topic的保留消息*/
    private static       Map<String, RetainMsgBo> retainMap = new HashMap();

    public static void pushRetain( MqttPublishMessage msg){
        if(null == msg || !msg.fixedHeader().isRetain()){
            return;
        }
        byte[] messageBytes = new byte[msg.payload().readableBytes()];
        msg.payload().getBytes(msg.payload().readerIndex(), messageBytes);
        if (messageBytes.length == 0) {
            retainMap.remove(msg.variableHeader().topicName());
        } else {
            RetainMsgBo retainMsg = new RetainMsgBo().setTopic(msg.variableHeader().topicName()).setMqttQoS(msg.fixedHeader().qosLevel().value())
                    .setMessageBytes(messageBytes);
            retainMap.put(msg.variableHeader().topicName(), retainMsg);
        }
        return;
    }

    public static RetainMsgBo latestRetain(String topic){
        RetainMsgBo msg = retainMap.get(topic);
        return msg;
    }


}
