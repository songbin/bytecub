package com.bytecub.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(1)
public class SpringContextUtil implements ApplicationContextAware {
    private static final Logger             logger = LoggerFactory.getLogger(SpringContextUtil.class);
    private static       ApplicationContext applicationContext;

    /**
     * 获取上下文
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 设置上下文
     *
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 通过名字获取上下文中的bean
     *
     * @param name beanName
     * @return object
     */
    public static Object getBean(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过类型获取上下文中的bean
     *
     * @param requiredType className
     * @return object
     */
    public static <T> T getBean(Class<T> requiredType) {
        try {
            return applicationContext.getBean(requiredType);
        } catch (Exception e) {
            return null;
        }

    }

    public  static <T> Map<String, T> getBeanWithAnnotation(Class<? extends Annotation> annotation) {
        Map<String, T> map = new HashMap<>();
        try {
            Map<String, Object> inner = applicationContext.getBeansWithAnnotation(annotation);
            if(CollectionUtils.isEmpty(inner)){
                return map;
            }
            inner.forEach((key, value)->{
                map.put(key, (T)value);
            });
            return map;
        } catch (Exception e) {
            logger.warn("", e);
            return map;
        }

    }

    /**
     * 获取application 里的属性值
     *
     * @param key key
     * @return str
     */
    public static String getAttributeValue(String key) {
        try {
            return applicationContext.getEnvironment().getProperty(key);
        } catch (Exception e) {
            logger.warn("", e);
            return null;
        }

    }


}
