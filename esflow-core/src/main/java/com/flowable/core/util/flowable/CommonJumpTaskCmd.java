package com.flowable.core.util.flowable;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.FlowableEngineAgenda;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityManager;

/**
 * 节点跳转指令
 * @project : esflow-core
 * @createTime : 2017年12月22日 : 下午2:10:09
 * @author : lukewei
 * @description :
 */
public class CommonJumpTaskCmd implements Command<Void> {

	/**
	 * 任务id
	 */
	private String taskId;
	
	/**
	 * 目标节点Id
	 */
	private String targetNodeKey;

	public CommonJumpTaskCmd(String taskId, String targetNodeKey) {

		this.taskId = taskId;
		this.targetNodeKey = targetNodeKey;
	}

	@Override
	public Void execute(CommandContext commandContext) {

		ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();
		TaskEntityManager taskEntityManager = org.flowable.task.service.impl.util.CommandContextUtil
				.getTaskEntityManager();
		TaskEntity taskEntity = taskEntityManager.findById(taskId);
		ExecutionEntity ee = executionEntityManager.findById(taskEntity.getExecutionId());
		Process process = ProcessDefinitionUtil.getProcess(ee.getProcessDefinitionId());
		FlowElement targetFlowElement = process.getFlowElement(targetNodeKey);
		ee.setCurrentFlowElement(targetFlowElement);
		FlowableEngineAgenda agenda = CommandContextUtil.getAgenda();
		agenda.planContinueProcessInCompensation(ee);
		taskEntityManager.delete(taskId);
		return null;
	}
}