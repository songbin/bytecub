package com.bytecub.common.domain.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class ResourceResDto {
    private Long id;
    private String resourceCode;
    private String resourceName;
    private Date createTime;
    private Date updateTime;
    private Integer resourceStatus;
    private Integer deviceType;

}