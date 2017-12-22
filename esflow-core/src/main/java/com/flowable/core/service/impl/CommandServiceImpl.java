package com.flowable.core.service.impl;

import org.flowable.engine.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;

import com.flowable.core.service.CommandService;
import com.flowable.core.util.flowable.CommonJumpTaskCmd;

public class CommandServiceImpl implements CommandService{

	@Autowired
	private ManagementService managementService;

	@Override
	public void jumpCommand(String taskId, String targetTaskDefKey) {
		
		managementService.executeCommand(new CommonJumpTaskCmd(taskId, targetTaskDefKey));
	}
}
