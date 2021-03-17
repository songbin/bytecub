package com.bytecub.common.biz;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.metadata.ProductFuncTypeEnum;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: RedisKeyUtil.java, v 0.1 2020-12-25  Exp $$
  */
public class RedisKeyUtil {
    /**
     * 构造设备实时状态缓存key
     *
     * */
    public static String buildRtCacheKey( String deviceCode, ProductFuncTypeEnum funcType){
        return BCConstants.REDIS_KEY.DEV_RT_DATA + deviceCode + ":" + funcType.getType();
    }
    public static String buildDeviceInfoKey( String deviceCode){
        return BCConstants.REDIS_KEY.DEV_INFO + deviceCode;
    }

    public static String buildDeviceOfflineKey( String deviceCode){
        return BCConstants.REDIS_KEY.DEVICE_OFFLINE + deviceCode;
    }

    public static String buildProductInfoKey( String productCode){
        return BCConstants.REDIS_KEY.PRODUCT_INFO + productCode;
    }

    public static String buildAuthKey( String key){
        return BCConstants.REDIS_KEY.OPEN_AUTH_INFO + key;
    }
}
