package com.flowable.core.service;

import java.util.Map;

public interface CommandService {

	/**
	 * 任意节点跳转
	 * @param params
	 *  base.bizId 工单ID
	 *  base.taskDefKey 目标节点定义id
	 */
	void jumpCommand(Map<String, Object> params) ;
}
