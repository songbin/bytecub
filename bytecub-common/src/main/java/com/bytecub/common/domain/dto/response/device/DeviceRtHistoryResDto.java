package com.bytecub.common.domain.dto.response.device;

import java.util.Date;

import com.bytecub.common.domain.storage.EsMessage;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 设备运行历史数据
  * @author bytecub@163.com  songbin
  * @version Id: DeviceRtItemResDto.java, v 0.1 2020-12-27  Exp $$
  */
@Data
public class DeviceRtHistoryResDto extends EsMessage {
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8" )
    Date   replyTime;
    Integer status;
    String replyMessage;
}
