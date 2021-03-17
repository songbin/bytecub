package com.bytecub.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * JSON转换工具类[使用FastJSON实现]
 *
 * @author hanxiaoqiang
 */
public class JsonUtil {

    private static final SerializeConfig config;
    private static final SerializerFeature[] features = {
            /**
             *  输出空置字段
            */
            SerializerFeature.WriteMapNullValue,
            /**
             * list字段如果为null，输出为[]，而不是null
            */
            SerializerFeature.WriteNullListAsEmpty,
            /**
             * 数值字段如果为null，输出为0，而不是null
            */
            SerializerFeature.WriteNullNumberAsZero,
            /**
             * Boolean字段如果为null，输出为false，而不是null
            */
            SerializerFeature.WriteNullBooleanAsFalse,
            /**
             * 字符类型字段如果为null，输出为""，而不是null
            */
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteDateUseDateFormat
            //SerializerFeature.DisableCircularReferenceDetect
    };

    static {
        config = new SerializeConfig();
        /**
         * 使用和json-lib兼容的日期输出格式
         */
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        /**
         * 使用和json-lib兼容的日期输出格式
         */
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    /**
     * 将对象转换为Json字符串
     *
     * @param t 对象
     * @return String 转换后的Json字符串
     */
    public static <T> String toString(T t) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        return toString(t, dateFormat);
    }

    /**
     * 将对象转换为Json字符串
     *
     * @param t          对象
     * @param dateFormat 日期类型转换格式
     * @param <T>
     * @return String 转换后的Json字符串
     */
    public static <T> String toString(T t, String dateFormat) {
        JSON.DEFFAULT_DATE_FORMAT = dateFormat;
        return JSON.toJSONString(t, features);
    }

    /**
     * 解析Json字符串为对象
     *
     * @param json  Json字符串
     * @param clazz 目标对象类型
     * @return T 目标对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 解析Json字符串为对象列表
     *
     * @param json  Json字符串
     * @param clazz 目标对象类型
     * @return List<T> 目标对象列表
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * 解析Json字符串为MAP
     *
     * @param json
     * @return
     */
    public static Map<String, Object> json2Map(String json) {
        return json != null && !json.isEmpty() ? (Map) JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
        }, new Feature[0]) : null;
    }


    public static JSONObject parseObject2(String json) {
        JSONObject result = new JSONObject();
        return result;
    }

    /**
     * 判断字符串是否可以转化为json对象
     *
     * @param content 待判断内容
     * @return
     */
    public static boolean isJsonObject(String content) {
        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if (StringUtils.isBlank(content)) {
            return false;
        }
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
