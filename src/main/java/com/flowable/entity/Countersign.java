package com.flowable.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flowable.util.Constants;

/**
 * 会签任务
 * @author 26223
 *
 */
@Entity
@Table(name="T_COUNTER_SIGN")
public class Countersign implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;
	
	@Column(name="TASK_ID", length = 32, nullable = false)
	private String taskId;

	@Column(name="BIZ_ID", length = 32, nullable = false)
	private String bizId;
	
	@Column(name="PROCESSINSTANCE_ID", length = 10, nullable = false)
	private String processInstanceId;
	
	@Column(name="PROCESSDEFINITION_ID", length = 128, nullable = false)
	private String processDefinitionId;
	
	@Column(name="TASK_ASSIGNEE", length = 32, nullable = false)
	private String taskAssignee;
	
	@Column(name="RESULT_TYPE", length = 10, nullable = false)
	private Integer resultType = Constants.MEET_YES;
	
	@Column(name="IS_COMPLETE", length = 10, nullable = false)
	private Integer isComplete = 0;//当前会签是否结束 0 没有,1 结束
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime = new Date();

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

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getTaskAssignee() {
		return taskAssignee;
	}

	public void setTaskAssignee(String taskAssignee) {
		this.taskAssignee = taskAssignee;
	}

	public Integer getResultType() {
		return resultType;
	}

	public void setResultType(Integer resultType) {
		this.resultType = resultType;
	}

	public Integer getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}
}
