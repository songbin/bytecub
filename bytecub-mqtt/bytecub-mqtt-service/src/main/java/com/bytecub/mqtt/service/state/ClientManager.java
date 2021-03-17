package com.bytecub.mqtt.service.state;


import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.domain.constants.MqttConstants;
import com.bytecub.mqtt.service.network.SendManager;
import com.bytecub.utils.ArrayUtil;
import com.bytecub.utils.CombinationUtil;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * client和topic的映射关系管理
 *
 * @author songbin
 * @version Id: ClientManager.java, v 0.1 2019/1/17   Exp $$
 */
public class ClientManager {
    private static final Logger                              logger   = LoggerFactory.getLogger(ClientManager.class);
    /**
     * key topic value上下文关系 包括clientId
     * value不适用map<ContextBo, String>的原因是，订阅时可能Qos改变，所以每次订阅都需要重新设置
     * key=>topic
     * value=>clientId，client网络上下文
     */
    private static       Map<String, Map<String, ContextBo>> topicMap = new ConcurrentHashMap<>();
    /**
     * 存储客户端最近一次ping请求的时间
     * key => clientID
     * value => 最近一次ping的时间，时间戳 精确到毫秒
     * */
    private static Map<String, Long> clientStatusMap = new ConcurrentHashMap<>();

    /**client和topic关联
     * key=>clientId
     * value=><topic, true(没啥意义就是为了组成map)>
     * */
    private static Map<String, Map<String, Boolean>> clientTopicMap = new ConcurrentHashMap<>();
    /**
     * 处理携带通配符的topic
     * */
    public static void addComplexClient(String topic, ContextBo contextBo){
        try{

        }catch (Exception e){
            logger.warn("异常", e);
        }
    }
    /**
     * 将client的上下文相关信息添加到映射关系表中
     * 针对的是简单的没有通配符的topic
     */
    public static void addClient(String topic, ContextBo contextBo) {
        try {
            /**处理topic对应的client*/
            Map<String, ContextBo> clientMap = topicMap.get(topic);
            if (CollectionUtils.isEmpty(clientMap)) {
                clientMap = new HashMap<>();
            }
            clientMap.put(contextBo.getClientId(), contextBo);
            topicMap.put(topic, clientMap);


            /**开始处理client对应所有topic**/
            Map<String, Boolean> topics = null;
            if(clientTopicMap.containsKey(contextBo.getClientId())){
                topics = clientTopicMap.get(contextBo.getClientId());
                if(!topics.containsKey(topic)){
                    topics.put(topic, true);
                }
            }else{
                topics = new HashMap<>();
                topics.put(topic, true);
                clientTopicMap.put(contextBo.getClientId(), topics);
            }

        } catch (Exception e) {
            logger.warn("异常", e);
        }
    }
    /**
     * 清理对应client下的所有数据
     * */
    public static void removeClient(String clientId){
        try{
            /**移除client对应的topic*/
            Map<String, Boolean> topics = clientTopicMap.get(clientId);
            if(null != topics){
                /**从topic中移除client*/
                for(String key : topics.keySet()){
                    Map<String, ContextBo> clientMap = topicMap.get(key);
                    if(CollectionUtils.isEmpty(clientMap)){
                        continue;
                    }
                    clientMap.remove(clientId);
                }
                clientTopicMap.remove(clientId);
            }
            clientStatusMap.remove(clientId);

        }catch (Exception e){
            logger.warn("移除client[{}]异常", e);
        }
    }
    /**
     * 客户端取消订阅
     * 删除指定topic下的指定client，取消订阅的时候用
     * */
    public static void unsubscribe(String topic, ContextBo contextBo) {
        try {
            Map<String, ContextBo> clientMap = topicMap.get(topic);
            if (CollectionUtils.isEmpty(clientMap)) {
                return;
            }
            ContextBo item = clientMap.get(contextBo.getClientId());
            if(null == item){
                return;
            }
            clientMap.remove(contextBo.getClientId());
        } catch (Exception e) {
            logger.warn("异常", e);
        }
    }

    /**
     * 将消息发送到指定topic下的所有client上去
     */
    public static void pubTopic(MqttPublishMessage msg) {
        String topic = msg.variableHeader().topicName();
        List<String> topicList = searchTopic(topic);
        for(String itemTopic:topicList){
            Map<String, ContextBo> clientMap = topicMap.get(itemTopic);
            if(CollectionUtils.isEmpty(clientMap)){
                continue;
            }
            for(ContextBo contextBo:clientMap.values()){
                if(!checkClientValid(contextBo.getClientId())){
                    continue;
                }
                SendManager.pubMsg(msg, contextBo);
            }
        }

    }
    /**
     * 根据指定topic搜索所有订阅的topic
     * 指定的topic没有通配符，但是订阅的时候可能会存在通配符，所以有个查找的过程
     * */
    private static List<String> searchTopic(String topic){
        try{
            List<String> topicList = new ArrayList<>();
            /**先把自己加进去*/
            topicList.add(topic);
            /**先处理#通配符*/
            String[] filterDup = topic.split("/");
            int[] src = new int[filterDup.length];
            String itemTopic = "";
            for(int i =0 ; i < filterDup.length; i++){
                String item = itemTopic.concat("#");
                topicList.add(item);
                itemTopic = itemTopic.concat(filterDup[i]).concat("/");
                src[i] = i;
                continue;
            }

            /**处理+通配符*/
            Map<List<Integer>, Boolean> map = CombinationUtil.alg(src);
            for(List<Integer> key:map.keySet()){
                String[] arr = ArrayUtil.copyString(filterDup);
                for(Integer index:key){
                    arr[index] = "+";
                }
                String newTopic = ArrayUtil.concat(arr, "/");
                topicList.add(newTopic);
            }

            return topicList;

        }catch (Exception e){
            logger.warn("", e);
            return null;
        }
    }



    public static void main(String[] args) {
        String topic = "/abc/123/test/topic/ddd/233";
        List<String> list = searchTopic(topic);
        System.out.println("原始数据:"+ topic + "找到的组合数量:" + list.size());
        System.out.println(list);
    }
    /**
     * 更新客户端在线时间，给客户端发送消息时用这个看客户端最近是否在线
     * */
    public static void updateClientOnLine(String clientId){
        clientStatusMap.put(clientId, System.currentTimeMillis());
    }

    /**
     * Ping时检查客户端是否在线
     * */
    public static Boolean checkClientValid(String clientId){
        long currTime = System.currentTimeMillis();
        Long timestamp = clientStatusMap.get(clientId);
        if(null == timestamp){
            /**newConnects的时候就会初始化一个ping时间进来*/
            return false;
        }
        if(currTime - timestamp > MqttConstants.DEVICE_PING_EXPIRED){
           clientStatusMap.remove(clientId);
           SessionManger.removeContextByClientId(clientId);
           return false;
        }
        return true;
    }


}
