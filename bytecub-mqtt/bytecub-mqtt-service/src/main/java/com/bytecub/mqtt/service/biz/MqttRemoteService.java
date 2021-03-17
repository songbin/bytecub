package com.bytecub.mqtt.service.biz;

import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.state.ClientManager;
import com.bytecub.mqtt.service.state.SessionManger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * MQTT SERVER对外提供的服务都集中写在这里，目的是为了方便后期要是接入第三方mqtt server的时候好整改
  * @author bytecub@163.com  songbin
  * @version Id: MqttRemoteService.java, v 0.1 2021-01-27  Exp $$
  */
public class MqttRemoteService {
    /**
     * 检查设备是否在线
     * */
    public static boolean checkClientOnLine(String clientId){
        return ClientManager.checkClientValid(clientId);
    }

    /**
     * 所有在线客户端
     * */
    public static List<String> onlineDevices(){
        Map<String, ContextBo> map =  SessionManger.getContextMap();
        List<String> list = new ArrayList<>();
        map.forEach((key, value)->{
            list.add(key);
        });
        return list;
    }
}
