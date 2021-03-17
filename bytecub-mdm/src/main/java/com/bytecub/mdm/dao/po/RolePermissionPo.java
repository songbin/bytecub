package com.bytecub.mdm.dao.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author dell
 */
@Data
@Table(name="t_role_permission")
public class RolePermissionPo {
    @Id
    private Long id;

    private Long userId;

    private String roleCode;

    private String permissionCode;

    private Date createTime;

    private Date updateTime;

    private Long opId;


}
