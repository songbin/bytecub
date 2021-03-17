package com.bytecub.mdm.dao.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_resource")
public class ResourcePo {
    @Id
    private Long id;

    private String resourceCode;

    private String resourceName;

    private Date createTime;

    private Date updateTime;

    private Integer resourceStatus;

    private Integer deviceType;

    private Integer delFlag;


}
