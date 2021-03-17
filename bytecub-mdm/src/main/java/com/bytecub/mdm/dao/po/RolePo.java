package com.bytecub.mdm.dao.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_role")
public class RolePo {
    @Id
    private Long id;

    private String roleCode;

    private String roleName;

    private Date createTime;

    private Date updateTime;

    private Long opId;

}
