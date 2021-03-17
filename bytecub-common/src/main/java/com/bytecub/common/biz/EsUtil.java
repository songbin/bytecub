package com.bytecub.common.biz;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.utils.DateUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: EsUtil.java, v 0.1 2020-12-12  Exp $$
  */
public class EsUtil {
    private static String env;
    public static void setEnv(String env){
        EsUtil.env = env;
    }
    /**
     * 记录设备属性/事件上报记录
     * 系统统一前缀 + product + funcType + property + 月份
     * */
    public static  String buildDevIndex(ProductFuncTypeEnum funcTypeEnum, String productCode, String identifier){
        return buildDevIndexByDate(new Date(), funcTypeEnum, productCode, identifier);
    }

    public static  String buildDevIndexByDate(Date date, ProductFuncTypeEnum funcTypeEnum, String productCode, String identifier){
        String month = DateUtil.formatMonth(date);
        String aliases = buildIndexAliases( productCode, funcTypeEnum, identifier);
        StringBuffer sb = new StringBuffer();
        sb.append(aliases)
                .append(BCConstants.ES.INDEX_SPLIT )
                .append(month)
                .append(BCConstants.ES.INDEX_SPLIT)
                .append(EsUtil.env);
        return sb.toString().toLowerCase();
    }

    /**
     * 记录设备属性设置下发指令, 路由是deviceCode
     * 系统统一前缀 + product + funcType + property  + set + 月份(yyyyMM)
     * */
    public static  String buildDevPropertyIndex(ProductFuncTypeEnum funcTypeEnum, String productCode, String identifier){
        return buildDevPropertyIndexByDate(new Date(), funcTypeEnum, productCode, identifier);
    }

    /**
     * 记录设备属性设置下发指令, 路由是deviceCode, 指定日期
     * 系统统一前缀 + product + funcType + property  + set + 月份(yyyyMM)
     * */
    public static  String buildDevPropertyIndexByDate(Date date, ProductFuncTypeEnum funcTypeEnum, String productCode, String identifier){
        String month = DateUtil.formatMonth(date);
        String aliases = buildPropSetIndexAliases( productCode, funcTypeEnum, identifier);
        StringBuffer sb = new StringBuffer();
        sb.append(aliases)
                .append(BCConstants.ES.INDEX_SPLIT )
                .append(month)
                .append(BCConstants.ES.INDEX_SPLIT)
                .append(EsUtil.env);
        return sb.toString().toLowerCase();
    }
    /**
     * 生成设备数据存储相关的模版
     * */
    public static String buildDeviceTemplateName(String productCode, ProductFuncTypeEnum funcTypeEnum, String identifier){
        return BCConstants.ES.INDEX_TEMPLATE_PREFIX + productCode + BCConstants.ES.INDEX_SPLIT + funcTypeEnum.getType().toLowerCase() + BCConstants.ES.INDEX_SPLIT  + identifier;
    }
    public static String buildSetPropertyTemplateName(String productCode, ProductFuncTypeEnum funcTypeEnum, String identifier){
        return BCConstants.ES.INDEX_TEMPLATE_PREFIX + BCConstants.ES.PROPERTY_SET_NAME + BCConstants.ES.INDEX_SPLIT + productCode + BCConstants.ES.INDEX_SPLIT + funcTypeEnum.getType().toLowerCase() + BCConstants.ES.INDEX_SPLIT  + identifier;
    }
    /**
     * 生成设备数据存储相关的模版对应的索引前缀
     * */
    public static String buildIndexPatterns(String productCode, ProductFuncTypeEnum funcTypeEnum, String identifier){
        String aliases =  buildIndexAliases(productCode, funcTypeEnum, identifier);
        StringBuffer sb = new StringBuffer(aliases)
                .append(BCConstants.ES.INDEX_SPLIT )
                .append("*");
        return sb.toString().toLowerCase();
     }
    /**
     * 业务数据存储索引创建别名
     * */
    public static String buildIndexAliases(String productCode, ProductFuncTypeEnum funcTypeEnum, String identifier){
        StringBuffer sb = new StringBuffer(BCConstants.ES.INDEX_DEVICE_PREFIX );
        sb.append(productCode)
                .append(BCConstants.ES.INDEX_SPLIT)
                .append(funcTypeEnum.getType())
                .append(BCConstants.ES.INDEX_SPLIT)
                .append(identifier);
        return sb.toString().toLowerCase();
    }

    /**
     * 属性下发别名
     * */
    public static String buildPropSetIndexAliases(String productCode, ProductFuncTypeEnum funcTypeEnum, String identifier){
        StringBuffer sb = new StringBuffer(BCConstants.ES.INDEX_DEVICE_PREFIX );
        sb.append(BCConstants.ES.PROPERTY_SET_NAME)
                .append(BCConstants.ES.INDEX_SPLIT)
                .append(productCode)
                .append(BCConstants.ES.INDEX_SPLIT)
                .append(funcTypeEnum.getType())
                .append(BCConstants.ES.INDEX_SPLIT)
                .append(identifier);
        return sb.toString().toLowerCase();
    }
    /**
     * 属性下发别名模版索引规则
     * */
    public static String buildPropSetIndexPatterns(String productCode, ProductFuncTypeEnum funcTypeEnum, String identifier){
        String aliases =  buildPropSetIndexAliases(productCode, funcTypeEnum, identifier);
        StringBuffer sb = new StringBuffer(aliases)
                .append(BCConstants.ES.INDEX_SPLIT )
                .append("*");
        return sb.toString().toLowerCase();
    }

}
