//package com.bytecub.common.metadata;
//
//import com.bytecub.plugin.es.config.enums.BcEsMetaType;
//import lombok.Data;
//
///**
//  * ByteCub.cn.
//  * Copyright (c) 2020-2020 All Rights Reserved.
//  * 
//  * @author bytecub@163.com  songbin
//  * @version Id: BaseRequestMsg.java, v 0.1 2020-12-09  Exp $$
//  */
//@Data
//public class BaseRequestMsg {
//    @MetaDesc(code = "deviceCode", name = "设备编码", desc = "设备唯一标识符", type = BcEsMetaType.STRING)
//    public String deviceCode;
//    @MetaDesc(code = "arrivedTime", name = "消息抵达时间", desc = "消息抵达服务器时间", type = BcEsMetaType.DATE)
//    public Long   arrivedTime;
//    @MetaDesc(code = "devTime", name = "设备消息时间", desc = "设备可能阻塞，比如网络恢复后一次性把断网期间的数据也上传了", type = BcEsMetaType.DATE)
//    public Long   devTime;
//    public Object value;
//}
