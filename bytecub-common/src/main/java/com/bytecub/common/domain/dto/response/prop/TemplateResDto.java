package com.bytecub.common.domain.dto.response.prop;

import lombok.Data;

import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 模版数据返回值
  * @author bytecub@163.com  songbin
  * @version Id: TemplateResDto.java, v 0.1 2021-01-19  Exp $$
  */
@Data
public class TemplateResDto {
    /**请求数据示例*/
    Map<String,Object> demoData;
    /**请求数据模版*/
    Map<String, Object> template;
}
