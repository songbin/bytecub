package com.bytecub.common.domain.bo;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: TopicDescBo.java, v 0.1 2020-12-22  Exp $$
  */
@Data
public class TopicDescBo {
    String name;
    String topic;
    String desc;

    public static TopicDescBo build(String name, String topic, String desc){
        TopicDescBo item = new TopicDescBo();
        item.setDesc(desc);
        item.setName(name);
        item.setTopic(topic);
        return item;
    }
}
