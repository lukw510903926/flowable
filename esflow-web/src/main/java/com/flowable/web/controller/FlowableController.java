package com.flowable.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowable.core.bean.BizInfo;
import com.flowable.core.service.IBizInfoService;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.UserTask;
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

import com.alibaba.fastjson.JSONObject;
import com.flowable.core.service.CommandService;
import com.flowable.core.service.IProcessDefinitionService;

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

    @Autowired
    private IBizInfoService bizInfoService;

    @ResponseBody
    @RequestMapping("/flow")
    public Map<String, Object> findOutGoingTransNames() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, Object> result = new HashMap<String, Object>();
        TaskEntityImpl task = (TaskEntityImpl) taskService.createTaskQuery().taskId("2527").singleResult();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId())
                .singleResult();
        String processDefinitionId = pi.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        for (FlowElement flowElement : flowElements) {
            if (flowElement.getId().equalsIgnoreCase(task.getTaskDefinitionKey())) {
                if (flowElement instanceof UserTask) {
                    UserTask u = (UserTask) flowElement;
                    List<SequenceFlow> outgoingFlows = u.getOutgoingFlows();
                    SequenceFlow sequenceFlow = outgoingFlows.get(0);
                    ExclusiveGateway userTask = (ExclusiveGateway) sequenceFlow.getTargetFlowElement();
                    userTask.getOutgoingFlows().forEach(outgoingFlow -> result.put(outgoingFlow.getId(), outgoingFlow.getName()));
                }
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/jump/{bizId}")
    public BizInfo jump(@PathVariable("bizId") String bizId) {

        BizInfo bizInfo = this.bizInfoService.get(bizId);
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("base.handleMessage", "工单跳转");
		params.put("base.handleResult", "工单跳转");
		params.put("base.handleName", "工单跳转");
		params.put("base.bizId", bizId);
		params.put("base.taskDefKey", "vendorHandle");
        commandService.jumpCommand(params);
        return bizInfo;
    }

    @ResponseBody
    @RequestMapping("/nextTask/{instanceId}")
    public String nextTask(@PathVariable("instanceId") String instanceId) {

        return JSONObject.toJSONString(processDefinitionService.getNextTaskInfo(instanceId));
    }
}
