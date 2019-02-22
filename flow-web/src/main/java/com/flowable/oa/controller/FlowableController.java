package com.flowable.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.service.CommandService;
import com.flowable.oa.core.service.IProcessDefinitionService;
import org.flowable.bpmn.model.*;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FlowableController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private CommandService commandService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    @ResponseBody
    @RequestMapping("/flow")
    public Map<String, Object> findOutGoingTransNames() {

        Map<String, Object> result = new HashMap<String, Object>();
        TaskEntityImpl task = (TaskEntityImpl) taskService.createTaskQuery().taskId("2527").singleResult();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId())
                .singleResult();
        String processDefinitionId = pi.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
        FlowElement flowElement = process.getFlowElement(task.getTaskDefinitionKey());
        if (flowElement != null) {
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                outgoingFlows.forEach(sequenceFlow -> {
                    if (sequenceFlow.getTargetFlowElement() instanceof ExclusiveGateway) {
                        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) sequenceFlow.getTargetFlowElement();
                        exclusiveGateway.getOutgoingFlows().forEach(outgoingFlow -> result.put(outgoingFlow.getId(), outgoingFlow.getName()));
                    }
                });
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/jump/{bizId}")
    public BizInfo jump(@PathVariable("bizId") String bizId) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("base.handleMessage", "工单跳转");
        params.put("base.handleResult", "工单跳转");
        params.put("base.handleName", "工单跳转");
        params.put("base.bizId", bizId);
        params.put("base.taskDefKey", "vendorHandle");
        return commandService.jumpCommand(params);
    }

    @ResponseBody
    @RequestMapping("/nextTask/{instanceId}")
    public String nextTask(@PathVariable("instanceId") String instanceId) {

        return JSONObject.toJSONString(processDefinitionService.getNextTaskInfo(instanceId));
    }
}
