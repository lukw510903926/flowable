package com.flowable.oa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.util.Date;

/**
 * 流程日志
 */
@Entity
@Table(name = "T_BIZ_LOG")
public class BizLog implements java.io.Serializable {

	private static final long serialVersionUID = -67861329386846521L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;

	@Column(name = "BIZ_ID")
	private String bizId;

	@Column(length = 512, name = "TASK_NAME")
	private String taskName;

	@Column(nullable = false, length = 64, name = "TASK_ID")
	private String taskID;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@Column(nullable = false, length = 256, name = "HANDLE_USER")
	private String handleUser;
	
	@Column(length = 64, name = "USER_PHONE")
	private String userPhone;
	
	@Column(length = 64, name = "USER_DEPT")
	private String userDept;

	@Column(length = 1000, name = "HANDLE_DESCRIPTION")
	private String handleDescription;

	@Column(nullable = false, length = 512, name = "HANDLE_RESULT")
	private String handleResult;

	@Column(nullable = false, length = 512, name = "HANDLE_NAME")
	private String handleName;

	public BizLog() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getHandleUser() {
		return handleUser;
	}

	public void setHandleUser(String handleUser) {
		this.handleUser = handleUser;
	}

	public String getHandleDescription() {
		return handleDescription;
	}

	public void setHandleDescription(String handleDescription) {
		this.handleDescription = handleDescription;
	}

	public String getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}

	public String getHandleName() {
		return handleName;
	}

	public void setHandleName(String handleName) {
		this.handleName = handleName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserDept() {
		return userDept;
	}

	public void setUserDept(String userDept) {
		this.userDept = userDept;
	}

}
