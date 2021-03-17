package com.bytecub.common.metadata;

import lombok.Getter;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: BcMetaType.java, v 0.1 2020-12-11  Exp $$
  */
@Getter
public enum BcMetaUnit {
    MM("mm",  "毫米",  "毫米"),
    CM("cm",  "厘米",  "厘米"),
    DM("dm",  "分米",  "分米"),
    KM("km",  "千米",  "千米"),
    M("m",  "米",  "米"),
    CM_SQUARE("c㎡",  "平方厘米",  "平方厘米"),
    MM_SQUARE("m㎡",  "平方毫米",  "平方毫米"),
    KM_SQUARE("k㎡",  "平方千米",  "平方千米"),
    M_SQUARE("㎡",  "平方米",  "平方米"),
    NM("nm",  "纳米",  "纳米"),
    M_MICRO_MICRO("μm",  "微米",  "微米"),
    L("L",  "升",  "升"),
    MM_CUB("mm³",  "立方毫米",  "立方毫米"),
    CM_CUB("cm³",  "立方厘米",  "立方厘米"),
    KM_CUB("km³",  "立方千米",  "立方千米"),
    M_CUB("m³",  "立方米",  "立方米"),
    GQ("h㎡",  "公顷",  "公顷"),
    KPA("kPa",  "千帕",  "千帕"),
    PA("Pa",  "帕斯卡",  "帕斯卡"),
    MG("mg",  "毫克",  "毫克"),
    G("g",  "克",  "克"),
    KG("kg",  "千克",  "千克"),
    N("N",  "牛",  "牛"),
    ML("mL",  "毫升",  "毫升"),
    HSD("℉",  "华氏度",  "华氏度"),
    K("K",  "开尔文",  "开尔文"),
    T("t",  "吨",  "吨"),
    SSD("°C",  "摄氏度",  "摄氏度"),
    MPA("mPa",  "毫帕",  "毫帕"),
    HPA("hPa",  "百帕",  "百帕"),
    J("J",  "焦耳",  "焦耳"),
    CAL("cal",  "卡路里",  "卡路里"),
    KWH("kW·h",  "千瓦时",  "千瓦时"),
    WH("Wh",  "瓦时",  "瓦时"),
    EV("eV",  "电子伏",  "电子伏"),
    KJ("kJ",  "千焦",  "千焦"),
    DU("°",  "度",  "度"),
    RAD("rad",  "弧度",  "弧度"),
    HZ("Hz",  "赫兹",  "赫兹"),
    ΜW("μW",  "微瓦",  "微瓦"),
    MW("mW",  "毫瓦",  "毫瓦"),
    KW("kW",  "千瓦特",  "千瓦特"),
    W("W",  "瓦特",  "瓦特"),
    KN("kn",  "节",  "节"),
    KMH("km/h",  "千米每小时",  "千米每小时"),
    MS("m/s",  "米每秒",  "米每秒"),
    SECOND("″",  "秒",  "秒"),
    MINUTE("′",  "分",  "分"),
    MIN("min",  "分钟",  "分钟"),
    H("h",  "小时",  "小时"),
    DAY("day",  "日",  "日"),
    WEEK("week",  "周",  "周"),
    MONTH("month",  "月",  "月"),
    YEAR("year",  "年",  "年"),
    KA("kA",  "千安",  "千安"),
    A("A",  "安培",  "安培"),
    MV("mV",  "毫伏",  "毫伏"),
    V("V",  "伏特",  "伏特"),
    MSS("ms",  "毫秒",  "毫秒"),
    S("s",  "秒",  "秒"),
    NF("nF",  "纳法",  "纳法"),
    PF("pF",  "皮法",  "皮法"),
    ΜF("μF",  "微法",  "微法"),
    F("F",  "法拉",  "法拉"),
    Ω("Ω",  "欧姆",  "欧姆"),
    ΜA("μA",  "微安",  "微安"),
    MA("mA",  "毫安",  "毫安"),
    GL("g/L",  "克每升",  "克每升"),
    MGL("mg/L",  "毫克每升",  "毫克每升"),
    ΜGM_CUB("μg/m³",  "微克每立方米",  "微克每立方米"),
    MGM_CUB("mg/m³",  "毫克每立方米",  "毫克每立方米"),
    GM_CUB("g/m³",  "克每立方米",  "克每立方米"),
    KGM_CUB("kg/m³",  "千克每立方米",  "千克每立方米"),
    GRAV("grav",  "重力加速度",  "重力加速度"),
    DB("dB",  "分贝",  "分贝"),
    PERCENT("%",  "百分比",  "百分比"),
    LM("lm",  "流明",  "流明"),
    BIT("bit",  "比特",  "比特"),
    GML("g/mL",  "克每毫升",  "克每毫升"),
    B("B",  "字节",  "字节"),
    ΜG_DAY("μg(d㎡·d)",  "微克每平方分米每天",  "微克每平方分米每天"),
    PPM("ppm",  "百万分率",  "百万分率"),
    PIXEL("pixel",  "像素",  "像素"),
    LUX("Lux",  "照度",  "照度"),
    KVAR("KVar",  "千乏",  "千乏"),
    ΜGL("μg/L",  "微克每升",  "微克每升"),
    KCAL("kcal",  "千卡路里",  "千卡路里"),
    GB("GB",  "吉字节",  "吉字节"),
    MB("MB",  "兆字节",  "兆字节"),
    KB("KB",  "千字节",  "千字节"),
    TH("t/h",  "吨每小时",  "吨每小时"),
    KCLH("KCL/h",  "千卡每小时",  "千卡每小时"),
    LS("L/s",  "升每秒",  "升每秒"),
    MPAS("Mpa",  "兆帕",  "兆帕"),
    CUB_HR("m³/h",  "立方米每小时",  "立方米每小时"),
    KVARHS("kvar/h",  "千乏时",  "千乏时"),
    UWCM2S("uw/cm2",  "微瓦每平方厘米",  "微瓦每平方厘米"),
    ZHI("只",  "只",  "只"),
    RH("%RH",  "相对湿度",  "相对湿度"),
    SUB_S("m³/s",  "立方米每秒",  "立方米每秒"),
    KGS("kg/s",  "公斤每秒",  "公斤每秒"),
    RMIN("r/min",  "转每分钟",  "转每分钟"),
    COUNT("count",  "次",  "次"),
    GEAR("gear",  "档",  "档"),
    STEPCOUNT("stepCount",  "步",  "步"),
    NM3H("Nm3/h",  "标准立方米每小时",  "标准立方米每小时"),
    KV("kV",  "千伏",  "千伏"),
    KVA("KVA",  "千伏安",  "千伏安"),
    KVARS("kvar",  "千乏",  "千乏"),
    TURNMS("turn/m",  "转每分钟",  "转每分钟"),
    VM("V/m",  "伏特每米",  "伏特每米"),
    MLMIN("ml/min",  "滴速",  "滴速"),
    MMHG("mm/Hg",  "血压",  "血压"),
    MMOLL("mmol/L",  "血糖",  "血糖"),
    MMS("mm/s",  "毫米每秒",  "毫米每秒"),
    NC("N/C",  "牛顿每库仑",  "牛顿每库仑"),
    PPB("ppb",  "微克每升",  "微克每升"),
    USCM("uS/cm",  "微西每厘米",  "微西每厘米"),
    PPT("ppt",  "纳克每升",  "纳克每升"),
    ;
    String       code;
    String       name;
    String       msg;

    BcMetaUnit(String code, String name, String msg) {
        this.code = code;
        this.name = name;
        this.msg = msg;
    }

    public static BcMetaUnit explain(String code){
        for(BcMetaUnit item: BcMetaUnit.values()){
            if(item.code.equals(code)){
                return item;
            }
        }
        return null;
    }
}
