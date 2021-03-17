package com.bytecub.mqtt.service.state;


import com.bytecub.mqtt.domain.bo.WillMsgBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by songbin on 2020-11-27.
 */
public class WillMsgManager {
    private static final Logger                 logger  = LoggerFactory.getLogger(WillMsgManager.class);
    /**存储每个Client的遗嘱消息*/
    private static       Map<String, WillMsgBo> willMap = new HashMap();


    public static void pushWill(WillMsgBo msg){
        if(null == msg){
            return;
        }
        willMap.put(msg.getClientId(), msg);
    }

    public static void pubWill(String clientId){
        try{
            WillMsgBo willMsgBo = willMap.get(clientId);
            if(null == willMsgBo){
                return;
            }
           // SendManager.sendMessage();
            ClientManager.pubTopic(willMsgBo.getMsg());
        }catch (Exception e){
            logger.warn("发送客户端[{}]的遗嘱消息异常", e);
        }
    }
}
