package com.flowable.core.bean;

import java.util.Date;

/**
 * 实例数据父类
 */
public abstract class AbstractVariableInstance implements Cloneable {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 流程实例ID
	 */
	private String processInstanceId;

	/**
	 * 值
	 */
	private String value;

	/**
	 * 增加填写时间
	 */
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public abstract void setVariable(AbstractVariable bean);

	public abstract AbstractVariable getVariable();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
