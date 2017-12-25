package com.flowable.core.util.flowable;

public class TaskInfo {

	/**
	 * 任务id
	 */
	private String taskId;
	
	/**
	 * 任务定义key
	 */
	private String taskDefinitionKey;
	
	/**
	 * 任务名称
	 */
	private String taskName;
	
	/**
	 * 待办人/组
	 */
	private String assignee;
	
	public TaskInfo() {
	}
	
	public TaskInfo(String taskId, String taskDefinitionKey, String taskName, String assignee) {
		super();
		this.taskId = taskId;
		this.taskDefinitionKey = taskDefinitionKey;
		this.taskName = taskName;
		this.assignee = assignee;
	}

	public TaskInfo(String taskId, String taskDefinitionKey, String taskName) {
		this.taskId = taskId;
		this.taskDefinitionKey = taskDefinitionKey;
		this.taskName = taskName;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	
}
