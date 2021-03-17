package com.bytecub.common.domain.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ResourceRequestDto.java, v 0.1 2020-12-10  Exp $$
  */
@Data
public class ResourceRequestDto {
    @Length(min = 1, max = 55, message = "编码长度[1,55]")
    private String resourceCode;
    @Length(min = 1, max = 40, message = "编码长度[1,40]")
    private String resourceName;
}
