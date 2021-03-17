package com.bytecub.mqtt.domain.bo;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Data;

/**
 * @author songbin
 * @version Id: SendMsgBo.java, v 0.1 2019/1/16   Exp $$
 */
@Data
public class SendMsgBo {
    /**
     * 主题
     */
    private String topic;

    /**
     * 接受客户端标识
     */
    private String recvClientId;

    /**
     * 发送方
     */
    private String sendClientId;

    private Integer msgId;


    private ByteBuf msgContent;

    /**
     * 保留标识
     */
    boolean  retain;

    /**
     * 重发标识
     */
    int  dupTimes;

    /**
     * 服务质量
     */
    MqttQoS qoS;


    public SendMsgBo(){

    }

    public SendMsgBo(SendMsgBo sendableMsg){
        this.topic = sendableMsg.topic;
        this.sendClientId = sendableMsg.sendClientId;
        this.msgId = sendableMsg.msgId;
        this.msgContent = sendableMsg.msgContent;
        this.retain = sendableMsg.retain;
        this.dupTimes = sendableMsg.dupTimes;
        this.qoS = sendableMsg.qoS;
    }



    public SendMsgBo(String topic, String recvClientId, String sendClientId, Integer msgId, ByteBuf msgContent,
                     boolean retain, int dupTimes, MqttQoS qoS) {
        super();
        this.topic = topic;
        this.recvClientId = recvClientId;
        this.sendClientId = sendClientId;
        this.msgId = msgId;
        this.msgContent = msgContent;
        this.retain = retain;
        this.dupTimes = dupTimes;
        this.qoS = qoS;
    }
}
