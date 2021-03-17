package com.bytecub.mdm.dao.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_protocol")
public class ProtocolPo {
    @Id
    private Long id;

    private String protocolCode;

    private String protocolName;

    private String protocolFileUrl;

    private Integer protocolType;

    private String jarSign;

    private Date createTime;

    private Date updateTime;

    private Integer protocolStatus;

    private Integer delFlag;



}
