package com.bytecub.gateway.mqtt.config;

import java.util.ArrayList;
import java.util.List;

/**
 * mqtt路由，用于记录mqtt client信息
 * @author songbin
 * @version Id: BCMqttRouter.java, v 0.1 2019/1/26   Exp $$
 */
public class BCMqttRouter {
    /**mosquitto服务器列表*/
    private static List<BCMqttClient> mosquittoList = new ArrayList<>();


    public static void addMosquittoServer(BCMqttClient client) {
        BCMqttRouter.mosquittoList.add(client);
    }

    public static List<BCMqttClient> getMosquittoServer() {
        return BCMqttRouter.mosquittoList;
    }

    public static BCMqttClient findServerById(Integer serverId){
        for(BCMqttClient client:mosquittoList){
            if(client.getServerId().equals(serverId)){
                return client;
            }
        }
        return null;
    }

}
