package com.flowable.entity.auth;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "T_SYS_ROLE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"}) 
public class SystemRole implements Serializable {

	private static final long serialVersionUID = -8144101409950412071L;

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@Column(name = "NAME_EN", length = 64)
	private String nameEn;

	@Column(name = "NAME_CN", length = 64)
	private String nameCn;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_SYS_USER_ROLE", joinColumns = { @JoinColumn(name = "ROLE_ID") },
		inverseJoinColumns = {@JoinColumn(name = "USER_ID") })
	private Set<SystemUser> users = new HashSet<SystemUser>();

	public SystemRole() {
	}

	public SystemRole(String nameEn, String nameCn) {
		this.nameEn = nameEn;
		this.nameCn = nameCn;
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

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	@JsonManagedReference
	public Set<SystemUser> getUsers() {
		return users;
	}

	public void setUsers(Set<SystemUser> users) {
		this.users = users;
	}
	
}
