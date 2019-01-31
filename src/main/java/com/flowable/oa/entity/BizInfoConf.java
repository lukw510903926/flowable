package com.flowable.oa.entity;

import java.io.Serializable;
import java.util.Date;
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

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:31:53
 * @author : lukewei
 * @description :
 */
@Entity
@Table(name = "T_BIZ_INFO_CONF")
public class BizInfoConf implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;
	
	@Column(name = "BIZ_ID")
	private String bizId;

	@Column(nullable = true, length = 64, name = "TASK_ID")
	private String taskId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime = new Date();

	/**
	 * 当前任务处理人
	 */
	@Column(nullable = true, length = 256, name = "TASK_ASSIGNEE")
	private String taskAssignee;
	
	/**
	 * 角色
	 */
	@Transient
	private Set<String> roles;
	
	@Transient
	private String loginUser;

	public BizInfoConf() {
		super();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskAssignee() {
		return taskAssignee;
	}

	public void setTaskAssignee(String taskAssignee) {
		this.taskAssignee = taskAssignee;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public BizInfoConf clone() {

		BizInfoConf bizInfoConf = null;
		try {
			bizInfoConf = (BizInfoConf) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bizInfoConf;
	}
}
