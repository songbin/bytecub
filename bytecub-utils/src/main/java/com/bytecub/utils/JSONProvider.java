package com.bytecub.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author songbin
 * @version 2016年6月4日 上午11:40:35
 */
public class JSONProvider {
    private static String getClassName() {
        return "JSONProvider";
    }
    private static final Logger logger = LoggerFactory.getLogger(JSONProvider.class);

    /**json string 转 对象
     * */
    public static <T> T parseObject( String text, Class<T> clazz) {
        try {
            if (StringUtils.isBlank(text)){
                return null;
            }

            return JSON.parseObject(text, clazz);
        } catch (Exception e) {
            return null;
        }
    }
    /**json string 转 对象
     * 返回值如果是空的话，就返回一个默认new的对象
     * */
    public static <T> T parseObjectDefValue( String text, Class<T> clazz) {
        try {
            if (StringUtils.isBlank(text)){
                return clazz.newInstance();
            }

            T ret = JSON.parseObject(text, clazz);
            if(null == ret){
                return clazz.newInstance();
            }
            return ret;
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    public static JSONObject fromString( String text) {
        try {
            if (StringUtils.isBlank(text))
                return null;
            return JSON.parseObject(text);
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
    public static <T> T parseJsonObject(JSONObject jsonObject, Class<T> clazz) {
        try {
            return jsonObject.toJavaObject(clazz);
        } catch (Exception e) {
            logger.warn("inputsteam", e);
            return null;
        }
    }

    /**json inputstream 转 对象
     * */
    public static <T> T map2Object(Map map, Class<T> clazz) {
        try {
            String jsonStr = JSONObject.toJSONString(map);
            return JSONObject.parseObject(jsonStr, clazz);
        } catch (Exception e) {
            logger.warn("inputsteam", e);
            return null;
        }
    }

    /**json string 转 对象
    * */
    public static <T> List<T> parseArrayObject(String text, Class<T> clazz) {

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
