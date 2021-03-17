package com.bytecub.common.domain.dto.response;

import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProtocolItemResDto.java, v 0.1 2020-12-15  Exp $$
  */
@Data
public class DataTypeItemResDto {
    String code;
    String name;
    String desc;

    public DataTypeItemResDto(String code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }
}
