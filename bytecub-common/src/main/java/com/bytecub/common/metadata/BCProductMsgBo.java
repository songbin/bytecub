//package com.bytecub.common.metadata;
//
//import com.bytecub.plugin.es.config.enums.BcEsMetaType;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.Data;
//
//import java.util.Date;
//
///**
//  * ByteCub.cn.
//  * Copyright (c) 2020-2020 All Rights Reserved.
//  * 消息在es存储的统一格式
//  * @author bytecub@163.com  songbin
//  * @version Id: BCProductMsgBo.java, v 0.1 2020-12-09  Exp $$
//  */
//@Data
//public class BCProductMsgBo {
//    @MetaDesc(code = "deviceCode", name = "设备编码", desc = "设备唯一标识符", type = BcEsMetaType.STRING)
//    public String deviceCode;
//    @MetaDesc(code = "arrivedTime", name = "消息抵达时间", desc = "消息抵达服务器时间", type = BcEsMetaType.DATE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss.SSS",timezone ="GMT+8")
//    public Date   bcsArrivedTime;
//    @MetaDesc(code = "devTime", name = "设备消息时间", desc = "设备可能阻塞，比如网络恢复后一次性把断网期间的数据也上传了", type = BcEsMetaType.DATE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss.SSS",timezone ="GMT+8")
//    //2015-01-01T12:10:30Z 或者 2017-11-27T03:16:03.944Z 需要格式化成这个样子，才会默认成为日期类型
//    public Date   bcsDevTime;
//    public Object value;
//}
