package com.bytecub.mqtt.service.network;

/**
 * @author songbin
 * @version Id: NettyServer.java, v 0.1 2019/1/15    Exp $$
 */
public interface MQTTServer {
    void start() throws Exception;
    void stop();
}
