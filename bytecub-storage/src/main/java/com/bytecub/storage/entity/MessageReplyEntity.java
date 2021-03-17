package com.bytecub.storage.entity;

import com.bytecub.common.constants.BCConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.   *  消息回执  * 
 *
 * @author bytecub@163.com songbin  * @version Id: MessageReplyEntity.java, v 0.1 2021-01-22  Exp $$  
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = BCConstants.ES.INDEX_PREFIX + "message_reply", replicas = 1, shards = 5, refreshInterval = "10s")
public class MessageReplyEntity {
    @Id
    private String id;
    /** 消息回执的messageId，和下行消息呼应 */
    @Field(type = FieldType.Keyword)
    String messageId;
    /** 设备处理消息的状态 */
    @Field(type = FieldType.Integer)
    Integer status;
    /** 抵达服务器时间 */
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSSZ",timezone="GMT+8")
    Date timestamp;
    /** 设备上报的时间 */
    @Field(type = FieldType.Date,  format = DateFormat.date_optional_time)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSSZ",timezone="GMT+8")
    Date devTimestamp;
    /** 回执消息内容 */
    @Field(type = FieldType.Keyword)
    String body;
    @Field(type = FieldType.Keyword)
    String productCode;
    @Field(type = FieldType.Keyword)
    String deviceCode;
}
