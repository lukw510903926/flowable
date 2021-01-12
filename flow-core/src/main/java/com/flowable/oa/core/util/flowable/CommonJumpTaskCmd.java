package com.flowable.oa.core.util.flowable;

import lombok.Data;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.FlowableEngineAgenda;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.identitylink.service.IdentityLinkService;
import org.flowable.task.service.TaskService;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

/**
 * <p>节点跳转指令
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/9 18:45
 **/
@Data
public class CommonJumpTaskCmd implements Command<Void> {

    @Override
    public Void execute(CommandContext commandContext) {

        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();
        TaskService taskService = CommandContextUtil.getTaskService();
        TaskEntity taskEntity = taskService.getTask(taskId);
        ExecutionEntity executionEntity = executionEntityManager.findById(taskEntity.getExecutionId());
        IdentityLinkService identityLinkService = CommandContextUtil.getIdentityLinkService();
        identityLinkService.deleteIdentityLinksByTaskId(taskId);
        Process process = ProcessDefinitionUtil.getProcess(executionEntity.getProcessDefinitionId());
        FlowElement targetFlowElement = process.getFlowElement(targetNodeKey);
        executionEntity.setCurrentFlowElement(targetFlowElement);
        FlowableEngineAgenda agenda = CommandContextUtil.getAgenda();
        agenda.planContinueProcessInCompensation(executionEntity);
        taskService.deleteTask(taskEntity, true);
        return null;
    }

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
}