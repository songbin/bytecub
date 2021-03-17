package com.bytecub.mdm.dao.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_open_auth")
public class OpenAuthPo {
    @Id
    private Long id;

    private String appKey;

    private String appSecret;

    private Boolean enableStatus;

    private Boolean delFlag;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    private String appName;

    private String appDesc;


}
