package com.flowable.oa.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 工单定时任务
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:32:45
 * @author : lukewei
 * @description :
 */
@Entity
@Table(name="T_TIMED_TASK")
public class BizTimedTask implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, length = 64)
	private String id;
	
	@Column(name="BIZ_ID",length = 64)
	private String bizId;
	
	@Column(name="TASK_NAME",length = 64)
	private String taskName;
	
	@Column(name="TASK_DEF_KEY",length = 64)
	private String taskDefKey;
	
	@Column(name="BUTTON_ID", length = 32)
	private String buttonId;
	
	@Column(nullable = true, length = 64, name = "TASK_ID")
	private String taskId;
	
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Column(name="END_TIME")
	private String endTime ;

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

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
