package com.bytecub.mdm.dao.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_device")
public class DevicePo {
    @Id
    private Long id;

    private String deviceCode;

    private String deviceName;

    private String gwDevCode;

    private String productCode;

    private Integer delFlag;

    private Integer enableStatus;

    private Integer activeStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date lastOnlineTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    String deviceSecret;

    String firmwareVersion;

    String devHost;
    Integer devPort;

}
