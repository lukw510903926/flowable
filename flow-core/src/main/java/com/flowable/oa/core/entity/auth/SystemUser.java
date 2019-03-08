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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@Entity
@Table(name = "T_SYS_USER")
public class SystemUser implements Serializable {

	private static final long serialVersionUID = -7377982631848439265L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, length = 64, name = "ID")
	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;

	@Column(name = "USERNAME", length = 64)
	private String username;

	@Column(name = "NAME", length = 64)
	private String name;

	@Column(name = "PASSWORD", length = 64)
	private String password;

	@Column(name = "EMAIL", length = 64)
	private String email;

	@Transient
	private Set<SystemRole> roles = new HashSet<>();

	/**
	 * 1 有效 0 无效
	 */
	@Column(name = "status", length = 5)
	private Integer status = 1;
}
