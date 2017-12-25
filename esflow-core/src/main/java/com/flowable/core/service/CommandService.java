package com.flowable.core.service;

public interface CommandService {

	/**
	 * 任意节点跳转
	 * @param bizId 工单ID
	 * @param taskId 任务id
	 * @param targetTaskDefKey 目标节点定义id
	 */
	void jumpCommand(String bizId,String taskId, String targetTaskDefKey) ;
}
