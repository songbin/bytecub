package com.bytecub.utils;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ArrayUtil.java, v 0.1 2020-12-03  Exp $$
  */
public class ArrayUtil {

    public static String[] copyString(String[] source){
        if(isEmpty(source)){
            return null;
        }
        int len = source.length;
        String[] arr = new String[len];
        for(int i=0; i < len; i ++){
            arr[i] = source[i];
        }
        return arr;
    }


    public static String concat(String[] source, String split){
        if(isEmpty(source)){
            return null;
        }
        String result = "";
        for(int i=0; i < source.length; i ++){
            result = result.concat(source[i]);
             if(i != source.length - 1){
                 result = result.concat(split);
             }
        }
        return result;
    }

    public static boolean isEmpty(String[] source){
        if(null == source){
            return true;
        }
        if(0 == source.length){
            return true;
        }
        return false;
    }
}
