package com.flowable.oa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 流程全局实例（存储具体填写的值）
 * 
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:34:09
 * @author : lukewei
 * @description :
 */
@Entity
@Table(name = "T_BIZ_PROCESS_INSTANCE")
public class ProcessVariableInstance implements Serializable, Cloneable {

	private static final long serialVersionUID = 620831623030964444L;

	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;

	/**
	 * 增加填写时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	/**
	 * 参数
	 */
	@Column(name = "PROCESS_VARIABLE_ID")
	private String variableId;

	/**
	 * 流程id
	 */
	@Column(name = "BIZ_ID", nullable = false, length = 64)
	private String bizId;

	/**
	 * 任务ID
	 */
	@Column(name = "TASK_ID", nullable = false, length = 32)
	private String taskId;

	/**
	 * 流程实例ID
	 */
	@Column(length = 64, name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	/**
	 * 值
	 */
	@Column(nullable = false, length = 512, name = "VALUE")
	private String value;

	/**
	 * 参数名称
	 */
	@Column(name = "VARIABLE_NAME", nullable = false, length = 32)
	private String variableName;

	/**
	 * 参数别名
	 */
	@Column(name = "VARIABLE_ALIAS", nullable = false, length = 32)
	private String variableAlias;
	
	@Column(name = "VIEW_COMPONENT")
	private String viewComponent;

	public String getId() {
		return id;
	}

	public String getVariableId() {
		return variableId;
	}

	public void setVariableId(String variableId) {
		this.variableId = variableId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public String getValue() {
		return value;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableAlias() {
		return variableAlias;
	}

	public void setVariableAlias(String variableAlias) {
		this.variableAlias = variableAlias;
	}

	public String getViewComponent() {
		return viewComponent;
	}

	public void setViewComponent(String viewComponent) {
		this.viewComponent = viewComponent;
	}

	public ProcessVariableInstance clone() {
		ProcessVariableInstance instance = null;
		try {
			instance = (ProcessVariableInstance) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

}
