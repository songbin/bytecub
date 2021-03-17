package com.bytecub.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: RandomUtil.java, v 0.1 2020-12-23  Exp $$
  */
public class RandomUtil {

    public static String randomString(int length){
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }
}
