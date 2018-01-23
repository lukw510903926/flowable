package com.flowable.core.bean.auth;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "T_SYS_USER")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"}) 
public class SystemUser implements Serializable {

	private static final long serialVersionUID = -7377982631848439265L;

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(unique = true, nullable = false, length = 64, name = "ID")
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

	@ManyToMany(mappedBy = "users")
	private Set<SystemRole> roles = new HashSet<SystemRole>();

	/**
	 * 1 有效 0 无效
	 */
	@Column(name = "status", length = 5)
	private Integer status = 1;
	

	public SystemUser() {
	}
	
	public SystemUser(String username, String name) {
		this.username = username;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonBackReference
	public Set<SystemRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<SystemRole> roles) {
		this.roles = roles;
	}
}
