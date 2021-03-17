package com.bytecub.utils;

import org.springframework.beans.BeanUtils;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ObjectCopyUtil.java, v 0.1 2020-12-05  Exp $$
  */
public class ObjectCopyUtil {

    /**
     * 对象属性拷贝 <br>
     * 将源对象的属性拷贝到目标对象
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
            BeanUtils.copyProperties(source, target);
    }
}
