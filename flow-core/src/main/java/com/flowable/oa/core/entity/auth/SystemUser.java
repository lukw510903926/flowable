package com.flowable.oa.core.entity.auth;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "T_SYS_USER")
public class SystemUser implements Serializable {

    private static final long serialVersionUID = -7377982631848439265L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "ID")
    private Integer id;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "LAST_LOGIN_TIME")
    private Date lastLoginTime;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Transient
    private Set<SystemRole> roles = new HashSet<>();

    /**
     * 1 有效 0 无效
     */
    @Column(name = "status")
    private Integer status;
}
