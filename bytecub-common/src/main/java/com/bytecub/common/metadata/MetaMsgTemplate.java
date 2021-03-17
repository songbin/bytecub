package com.bytecub.common.metadata;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: MetaMsgTemplate.java, v 0.1 2020-12-07  Exp $$
  */
@Data
public class MetaMsgTemplate {
    String code;
    String name;
    String desc;
    //BcMetaType type;

    public MetaMsgTemplate(String code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;

    }
}
