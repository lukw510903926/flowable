package com.flowable.oa.core.entity.auth;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Descrption : 用户角色关联
 *
 * @author : lukew
 * @created : 2017/12/27 20:17
 * @eamil : 13507615840@163.com
 **/
@Data
@Entity
@Table(name = "T_SYS_USER_ROLE")
public class SysUserRole implements Serializable {


    private static final long serialVersionUID = 8168749240683768907L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, length = 64, name = "ID")
    private Integer id;

    @Column(name = "USER_ID", length = 32)
    private Integer userId;

    @Column(name = "ROLE_ID", length = 32)
    private Integer roleId;

    @Transient
    private String roleIds;
}
