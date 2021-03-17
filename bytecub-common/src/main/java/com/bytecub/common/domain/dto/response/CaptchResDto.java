package com.bytecub.common.domain.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: CaptchResDto.java, v 0.1 2020-12-17  Exp $$
  */
@Data
public class CaptchResDto {
    /**
     * 图像验证码BASE64
     */
    @ApiModelProperty(value = "图片")
    private String img;
    /**
     * 图像验证码KEY
     */
    @ApiModelProperty(value = "图像验证码KEY")
    private String captchaToken;
}
