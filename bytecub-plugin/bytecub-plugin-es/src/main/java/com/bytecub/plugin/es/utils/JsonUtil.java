package com.bytecub.plugin.es.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songbin
 * @version 2016年6月4日 上午11:40:35  
 */
public class JsonUtil {
    private static String getClassName() {
        return "JSONProvider";
    }
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**json string 转 对象
     * */
    public static <T> Object parseObject( String text, Class<T> clazz) {
        try {
            if (StringUtils.isBlank(text))
                return null;
            return JSON.parseObject(text, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**对象 转json string
     * */
    public static String toJSONString( Object object) {
        try {
            if (null == object)
                return null;
            return JSON.toJSONString(object);
        } catch (Exception e) {
            return null;
        }
    }


    /**json inputstream 转 对象
     * */
    public static <T> Object parseJsonObject(JSONObject jsonObject, Class<T> clazz) {
        try {
            return jsonObject.toJavaObject(clazz);
        } catch (Exception e) {
            logger.warn("inputsteam", e);
            return null;
        }
    }

    /**json string 转 对象
    * */
    public static <T> Object parseArrayObject( String text, Class<T> clazz) {

        try {
            if (StringUtils.isBlank(text))
                return null;
            return JSON.parseArray(text, clazz);
        } catch (Exception e) {
            logger.warn("exception", e);
            return null;
        }
    }
}
