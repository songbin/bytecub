package com.bytecub.mdm.dao.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_user")
public class UserPo {
    @Id
    private Long id;

    private String userName;

    private String userNick;

    private String passwd;

    private Boolean delFlag;

    private Integer roleType;

    private Integer userStatus;

    private Date createTime;

    private Date updateTime;

    private String roleCode;

}
