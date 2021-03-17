package com.bytecub.common.biz;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.metadata.ProductFuncTypeEnum;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: TopicBiz.java, v 0.1 2020-12-09  Exp $$
  */
public class TopicBiz {
    /**从topic中解析物模型类型
     * 系统前缀/deviceCode/suffix
     * */
    public static String parseDeviceCode(String topic) {
        try {
            String[] splits = topic.split("/");
            return splits[2];
        } catch (Exception e) {
            throw BCGException.genException(BCErrorEnum.MQTT_TOPIC_INVALID, topic);
        }
    }
    /**从topic中解析物模型类型
     * 系统前缀/funcType/identifier/deviceCode
     * */
    public static String parseFuncType(String topic) {
        try {
            String[] splits = topic.split("/");
            return splits[3].toUpperCase();
        } catch (Exception e) {
            throw BCGException.genException(BCErrorEnum.MQTT_TOPIC_INVALID, topic);
        }
    }
    /**从topic中解析物模型类型
     * 系统前缀/funcType/identifier/deviceCode
     * */
    public static ProductFuncTypeEnum parseFuncTypeEnum(String topic) {
        try {
            String[] splits = topic.split("/");
            return ProductFuncTypeEnum.explain(splits[3].toUpperCase());
        } catch (Exception e) {
            throw BCGException.genException(BCErrorEnum.MQTT_TOPIC_INVALID, topic);
        }
    }
//    /**从topic中获取产品编码*/
//    @Deprecated
//    public static String parseProductCode(String topic) {
//        try {
//            String[] splits = topic.split("/");
//            return splits[1];
//        } catch (Exception e) {
//            throw BCGException.genException(BCErrorEnum.MQTT_TOPIC_INVALID, topic);
//        }
//    }
    /**
     * 根据协议编码组装订阅主题，用于内部MQTT客户端订阅
     * */
    public static String buildTopicByProduct(){
        return BCConstants.MQTT.GLOBAL_UP_PREFIX  + "/#";
    }

    public static String buildServiceInvoke(String deviceCode, String productCode){
        StringBuffer sb = new StringBuffer(BCConstants.MQTT.GLOBAL_DOWN_PREFIX)
                .append(productCode)
                .append("/")
                .append(deviceCode)
                .append("/service/invoke");
        return sb.toString();
    }

    public static String buildPropertySet( String deviceCode, String productCode){
        StringBuffer sb = new StringBuffer(BCConstants.MQTT.GLOBAL_DOWN_PREFIX)
                .append(productCode)
                .append("/")
                .append(deviceCode)
                .append("/property/set");
        return sb.toString();
    }

    public static String buildPropertyGet( String deviceCode, String productCode){
        StringBuffer sb = new StringBuffer(BCConstants.MQTT.GLOBAL_DOWN_PREFIX)
                .append(productCode)
                .append("/")
                .append(deviceCode)
                .append("/property/get");
        return sb.toString();
    }
}
