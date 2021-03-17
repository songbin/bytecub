package com.bytecub.common.biz;

import com.bytecub.common.constants.BCConstants;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: MqttUtil.java, v 0.1 2021-01-05  Exp $$
  */
public class MqttUtil {
    /**从mqtt connect的userName中获取deviceCode*/
    public static  String fetchDeviceCode(String userName){
        String[] strings =  userName.split("\\" + BCConstants.AUTH.SIGN_SPLIT);
        return strings[0];
    }
}
