package com.flowable.service.flow;

import java.util.Map;

import com.flowable.entity.BizInfo;

public interface CommandService {

	/**
	 * 任意节点跳转
	 * 
	 * @param params base.bizId 工单ID base.taskDefKey 目标节点定义id
	 */
	BizInfo jumpCommand(Map<String, Object> params);
}