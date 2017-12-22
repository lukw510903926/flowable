package com.flowable.core.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 流程全局实例（存储具体填写的值）
 */
@Entity
@Table(name = "ACT_BIZ_TASK_INSTANCE")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"}) 
public class TaskVariableInstance extends AbstractVariableInstance implements java.io.Serializable {

	private static final long serialVersionUID = 5283203408485574414L;

	private String taskId;

	private TaskVariable variable;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TASK_VARIABLE_ID")
	// @Transient
	@Override
	public TaskVariable getVariable() {
		return variable;
	}

	@Override
	public void setVariable(AbstractVariable bean) {
		if (bean != null && bean instanceof TaskVariable) {
			variable = (TaskVariable) bean;
		}
	}

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	public String getId() {
		return super.getId();
	}

	@Column(nullable = false, length = 64, name = "PROCESS_INSTANCE_ID")
	public String getProcessInstanceId() {
		return super.getProcessInstanceId();
	}

	@Column(nullable = false, length = 512, name = "VALUE")
	public String getValue() {
		return super.getValue();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return super.getCreateTime();
	}

	@Column(nullable = false, length = 64, name = "TASK_ID")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public void setCreateTime(Date createTime) {
		super.setCreateTime(createTime);
	}

	@Override
	public void setId(String id) {
		super.setId(id);
	}

	@Override
	public void setProcessInstanceId(String processInstanceId) {
		super.setProcessInstanceId(processInstanceId);
	}

	@Override
	public void setValue(String value) {
		super.setValue(value);
	}

}
