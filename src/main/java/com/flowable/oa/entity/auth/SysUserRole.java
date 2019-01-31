package com.flowable.oa.entity.auth;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Descrption : 用户角色关联
 *
 * @author : lukew
 * @created : 2017/12/27 20:17
 * @eamil : 13507615840@163.com
 **/
@Entity
@Table(name = "T_SYS_USER_ROLE")
public class SysUserRole implements Serializable{


    private static final long serialVersionUID = 8168749240683768907L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, length = 64, name = "ID")
    private String id;

    @Column(name = "USER_ID",length = 32)
    private String userId;

    @Column(name = "ROLE_ID",length = 32)
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
