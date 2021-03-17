package com.bytecub.storage;

import java.util.Date;
import java.util.List;

import com.bytecub.storage.entity.MessageReplyEntity;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IMessageReplyService.java, v 0.1 2021-01-22  Exp $$
  */
public interface IMessageReplyService {
    /**
     * 新增数据
     * @param entity
     * @return
     * */
    void create(MessageReplyEntity entity);
    /**
     * 根据deviceCode和messageId查询列表
     * @param  deviceCode String
     * @param ids List
     * @return
     * */
    List<MessageReplyEntity> searchHistory(String deviceCode, List<String> ids);
    /**
     * 根据messageId查询回执
     * @param  messageId
     * @param deviceCode
     * @return
     * */
    MessageReplyEntity queryByMessageId(String deviceCode, String messageId);
    /**
     * 指定范围消息总数
     * @param start
     * @param end
     * @return
     * */
    long count(Date start, Date end);
}
