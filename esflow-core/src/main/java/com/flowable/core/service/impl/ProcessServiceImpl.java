package com.flowable.core.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowElementsContainer;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.RepositoryServiceImpl;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.flowable.common.exception.ServiceException;
import com.flowable.common.utils.LoginUser;
import com.flowable.common.utils.ReflectionUtils;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.auth.SystemRole;
import com.flowable.core.dao.IProcessModelDao;
import com.flowable.core.service.IProcessDefinitionService;
import com.flowable.core.service.auth.ISystemUserService;
import com.flowable.core.util.Constants;
import com.flowable.core.util.WebUtil;
import com.flowable.core.util.flowable.HistroyActivitiFlow;

@Service
@Transactional(readOnly = true)
public class ProcessServiceImpl implements IProcessDefinitionService {

    private static Logger logger = Logger.getLogger(ProcessServiceImpl.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IProcessModelDao processModelDao;

    @Autowired
    private ProcessEngineConfiguration engineConfiguration;

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public Map<String, Object> getActivityTask(BizInfo bean, LoginUser user) {

        if (bean == null || Constants.BIZ_END.equals(bean.getStatus())) {
            return null;
        }
        Map<String, Object> result = null;
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(bean.getProcessInstanceId())
                .taskAssignee(user.getUsername()).list();
        String curreOp = null;
        Task task = null;
        if (!CollectionUtils.isEmpty(taskList)) {
            StringBuffer temp = new StringBuffer();
            temp.append("(HANDLE)listSize:" + taskList.size() + "==>");
            taskList.forEach(t -> temp.append(t.getId() + "::" + t.getTaskDefinitionKey() + " || "));
            logger.debug("=========" + temp.toString());
            task = taskList.get(0);
            curreOp = Constants.HANDLE;
        } else {
            List<String> roles = systemUserService.findUserRoles(user.getUsername());
            if (!CollectionUtils.isEmpty(roles)) {
                taskList = taskService.createTaskQuery().taskCandidateGroupIn(roles).list();
                if (!CollectionUtils.isEmpty(taskList)) {
                    StringBuffer temp = new StringBuffer();
                    temp.append("(SIGN)listSize:" + taskList.size() + "==>");
                    taskList.forEach(t -> temp.append(t.getId() + "::" + t.getTaskDefinitionKey() + " || "));
                    logger.debug("=========" + temp.toString());
                    task = taskList.get(0);
                    curreOp = Constants.SIGN;
                }
            }
        }
        if (curreOp != null && task != null) {
            result = new HashMap<String, Object>();
            result.put("taskID", task.getId());
            result.put("curreOp", curreOp);
        }
        return result;
    }

    @Override
    public Map<String, String> loadStartButtons(String tempId) {

        Map<String, String> result = null;
        /*
         * ActivityImpl activity = getStartActivityImpl(tempId); if (activity != null) {
         * ProcessDefinitionEntity pde = (ProcessDefinitionEntity)
         * activity.getProcessDefinition(); result = findOutGoingTransNames(pde,
         * activity); }
         */
        result = result == null ? new HashMap<String, String>() : result;
        if (result.size() == 0) {
            result.put("submit", "提交");
        }
        return result;
    }

    /**
     * 获取任务节点 出口线
     */
    @Override
    public Map<String, String> findOutGoingTransNames(String taskID, boolean def) {

        Map<String, String> result = new HashMap<String, String>();
        UserTask userTask = this.getCurrentTask(taskID);
        List<SequenceFlow> list = this.getOutgoingFlows(userTask);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(sequence -> {
                result.put(sequence.getId(), sequence.getName());
            });
        }
        if (def && MapUtils.isEmpty(result)) {
            result.put("submit", "提交");
        }
        return result;
    }

    /**
     * 获取UserTask节点信息
     *
     * @param taskId
     * @return
     */

    public UserTask getCurrentTask(String taskId) {

        UserTask currentTask = null;
        TaskEntityImpl task = (TaskEntityImpl) taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance processInstance = this.getProceInstance(task.getProcessInstanceId());
        String processDefinitionId = processInstance.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Process process = bpmnModel.getProcesses().get(0);
        FlowElement flowElement = process.getFlowElement(task.getTaskDefinitionKey());
        if (flowElement != null && flowElement instanceof UserTask) {
            currentTask = (UserTask) flowElement;
        }
        return currentTask;
    }

    /**
     * 获取任务节点出口线
     *
     * @param userTask
     * @return
     */
    private List<SequenceFlow> getOutgoingFlows(UserTask userTask) {

        List<SequenceFlow> list = new ArrayList<SequenceFlow>();
        if (userTask == null) {
            return list;
        }
        List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
        if (!CollectionUtils.isEmpty(outgoingFlows)) {
            outgoingFlows.forEach(sequenceFlow -> {
                FlowElement targetFlowElement = sequenceFlow.getTargetFlowElement();
                if (targetFlowElement instanceof ExclusiveGateway) {
                    ExclusiveGateway exclusiveGateway = (ExclusiveGateway) sequenceFlow.getTargetFlowElement();
                    List<SequenceFlow> sequenceFlows = exclusiveGateway.getOutgoingFlows();
                    if (!CollectionUtils.isEmpty(sequenceFlows)) {
                        list.addAll(sequenceFlows);
                    }
                }
            });
        }
        return list;
    }

    @Transactional
    public ProcessInstance newProcessInstance(String id, Map<String, Object> variables) {

        return runtimeService.startProcessInstanceById(id, variables);
    }

    /**
     * 获取当前任务的前一个任务<br>
     * 如果当前任务的前置任务有多个，可以指定parentTaskDefinitionKey 来确定获取，否则将获取最后进入当前任务的结点作为前置结点
     *
     * @param taskID 当前任务ID
     * @return @
     */
    @Override
    public String getParentTask(String taskID) {

        UserTask userTask = this.getCurrentTask(taskID);
        Optional<FlowElementsContainer> parent = Optional.ofNullable(userTask.getParentContainer());
        return parent.map(container -> {
            List<FlowElement> list = new ArrayList<FlowElement>(container.getFlowElements());
            return list.get(0).getId();
        }).orElse(null);
    }

    /**
     * 获取流程运行PATH,以逗号开头，使用逗号分隔，根据历史任务的结束时间倒序排序
     *
     * @return @
     */
    public String getProcessPath(BizInfo bean) {

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        List<HistoricTaskInstance> list = historicTaskInstanceQuery.processInstanceId(bean.getProcessInstanceId())
                .orderByHistoricTaskInstanceEndTime().desc().list();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (HistoricTaskInstance hti : list) {
            if (hti.getEndTime() == null) {
                continue;
            }
            String tid = hti.getTaskDefinitionKey();
            if (sb.toString().endsWith("," + tid)) {
                continue;
            }
            sb.append("," + hti.getTaskDefinitionKey());
        }
        return sb.toString();
    }

    /**
     * 执行任务
     *
     * @param bizInfo
     * @param taskID
     * @param variables
     * @return @
     */
    @Override
    @Transactional
    public boolean completeTask(BizInfo bizInfo, String taskID, LoginUser user, Map<String, Object> variables) {

        try {
            String processInstanceId = bizInfo.getProcessInstanceId();
            variables.put("SYS_CURRENT_PID", processInstanceId);
            variables.put("SYS_CURRENT_WORKNUMBER", bizInfo.getWorkNum());
            variables.put("SYS_CURRENT_WORKID", bizInfo.getId());
            variables.put("SYS_CURRENT_TASKID", taskID);
            Task task = this.getTaskBean(taskID);
            UserTask userTask = this.getCurrentTask(taskID);
            taskService.complete(task.getId(), variables);
            variables.put("SYS_CURRENT_TASKKEY", task == null ? null : task.getTaskDefinitionKey());
            executeCommand(processInstanceId, user, userTask, variables);
            autoClaim(processInstanceId);
        } catch (Exception e) {
            logger.error("任务提交失败 : {}", e);
            throw new ServiceException("任务提交失败!");
        }
        return true;
    }

    /**
     * 自动签收//如果当前任务只有一个用户则设定此用户为自动签收
     *
     * @param processInstanceID
     * @return
     */
    @Override
    public boolean autoClaim(String processInstanceID) {

        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceID).list();
        for (Task task : list) {
            if (StringUtils.isNotEmpty(task.getAssignee())) {
                continue;
            }
            List<String> groups = getTaskCandidateGroup(task);
            if (CollectionUtils.isEmpty(groups)) {
                continue;
            }
            // TODO 只处理了一个角色的情况 未处理有候选角色的情况
            String username = this.systemUserService.findOnlyUser(new SystemRole(null, groups.get(0)));
            if (StringUtils.isNotEmpty(username)) {
                taskService.claim(task.getId(), username);
            }
        }
        return true;
    }

    private boolean executeCommand(String processInstanceId, LoginUser loginUser, UserTask userTask,
                                   Map<String, Object> variables) {

        String transfer_type = (String) variables.get("SYS_transfer_type");
        String transfer_value = (String) variables.get("SYS_transfer_value");
        String buttonValue = (String) variables.get("SYS_BUTTON_VALUE");

        if (StringUtils.isNotEmpty(buttonValue)) { // 获取当前任务的流出，并判断是否为当前活动任务
            ProcessInstance processInstance = this.getProceInstance(processInstanceId);
            if (processInstance == null) {
                return true;// 流程已结束
            }
            Task nextTask = this.getNextTaskInfo(processInstanceId).get(0);
            List<SequenceFlow> outgoingFlows = this.getOutgoingFlows(userTask);

            for (SequenceFlow pvmTransition : outgoingFlows) {
                if (pvmTransition.getId().equals(buttonValue) || "submit".equalsIgnoreCase(buttonValue)) {
                    String docv = (String) pvmTransition.getDocumentation();
                    if (StringUtils.isEmpty(docv)) {
                        continue;
                    }
                    // 如果该线为驳回先，则从历史里面去当前任务已经结束的最近一个任务，然后将单分配给此人
                    if (docv.startsWith("command:fallback")) {
                        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                                .processInstanceId(processInstanceId).finished().orderByHistoricTaskInstanceEndTime()
                                .desc().taskDefinitionKey(nextTask.getTaskDefinitionKey()).list();
                        if (!CollectionUtils.isEmpty(list)) {
                            HistoricTaskInstance hti = list.get(0);
                            if (StringUtils.isNotBlank(hti.getAssignee())) {
                                taskService.claim(nextTask.getId(), hti.getAssignee());
                            }
                        }
                        // 如果该线为循环，则将新的任务转派给当前用户 // 如果该线为转派，则将新的任务转派给制定的用户或组
                    } else if (docv.startsWith("command:repeat")) {
                        taskService.claim(nextTask.getId(), loginUser.getUsername());
                    } else if (docv.startsWith("command:transfer")) {
                        assignmentTask(nextTask, loginUser, transfer_value, transfer_type);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public ProcessInstance getProceInstance(String processInstanceID) {

        return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceID).singleResult();
    }

    /**
     * 签收任务
     *
     * @param bean
     * @param taskID
     * @param username
     * @return @
     */
    @Override
    public boolean claimTask(BizInfo bean, String taskID, String username) {

        // 签收进行权限判断
        Task task = getTaskBean(taskID);
        if (task == null) {
            throw new ServiceException("无效任务");
        }
        if (StringUtils.isNotEmpty(task.getAssignee())) {
            throw new ServiceException("任务已被签收");
        }
        boolean flag = claimRole(task, username);
        if (!flag) {
            List<String> list = getTaskCandidateGroup(task);
            StringBuilder roles = new StringBuilder();
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(role -> roles.append(role).append(" "));
            }
            throw new ServiceException("没有权限签收该任务,当前任务代办角色为 :" + roles.toString());
        }
        taskService.claim(taskID, username);
        return true;
    }

    private boolean claimRole(Task task, String username) {

        List<String> list = getTaskCandidateGroup(task);
        logger.info("group :" + list);
        boolean flag = false;
        if (!CollectionUtils.isEmpty(list)) {
            List<String> roles = systemUserService.findUserRoles(username);
            logger.info("user roles :" + roles);
            for (String group : list) {
                if (roles.contains(group)) {
                    flag = true;
                    break;
                }
            }
        } else {
            flag = true; // 环节没设置签收角色
        }
        return flag;
    }

    @Override
    public Map<String, Object> loadProcessList() {

        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        Map<String, Object> result = new HashMap<String, Object>();
        for (ProcessDefinition pd : list) {
            if (pd.getResourceName() != null
                    && pd.getResourceName().startsWith("org/activiti/explorer/demo/process/")) {
                continue;
            }
            result.put(pd.getKey(), pd.getName());
        }
        return result;
    }

    /**
     * 转派任务
     *
     * @param taskID
     * @param loginUser
     * @param toAssignment
     * @param assignmentType
     * @return @
     */
    @Override
    public boolean assignmentTask(String taskID, LoginUser loginUser, String toAssignment, String assignmentType) {

        if (!("group".equalsIgnoreCase(assignmentType) || "user".equalsIgnoreCase(assignmentType))) {
            throw new ServiceException("参数错误");
        }
        Task task = getTaskBean(taskID);
        if (!loginUser.getUsername().equals(task.getAssignee())) {
            throw new ServiceException("没有权限处理该任务");
        }
        return assignmentTask(task, loginUser, toAssignment, assignmentType);
    }

    @Override
    public List<String> getTaskCandidateGroup(Task task) {

        List<IdentityLink> links = taskService.getIdentityLinksForTask(task.getId());
        List<String> result = new ArrayList<String>();
        if (links != null && !links.isEmpty()) {
            for (IdentityLink il : links) {
                if ("candidate".equals(il.getType())) {
                    String groupName = il.getGroupId();
                    if (StringUtils.isNotEmpty(groupName)) {
                        result.add(groupName);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void interceptTask(String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        List<String> groups = this.getTaskCandidateGroup(task);
        if (!CollectionUtils.isEmpty(groups)) {
            for (String group : groups) {
                if (StringUtils.isNotBlank(group)) {
                    taskService.deleteCandidateGroup(task.getId(), group);
                }
            }
        }
        taskService.addCandidateUser(task.getId(), WebUtil.getLoginUser().getUsername());
    }

    private boolean assignmentTask(Task task, LoginUser user, String toAssignment, String assignmentType) {

        if (StringUtils.isEmpty(toAssignment)) {
            return false;
        }
        // 先删除定义的组，然后添加新的组
        List<String> groups = getTaskCandidateGroup(task);
        if (!CollectionUtils.isEmpty(groups)) {
            try {
                for (String group : groups) {
                    if (StringUtils.isNotBlank(group)) {
                        taskService.deleteCandidateGroup(task.getId(), group);
                    }
                }
                taskService.unclaim(task.getId());
            } catch (Exception e) {
                logger.error("任务签收失败 : {}", e);
                throw new ServiceException("任务签收失败 !");
            }
        }
        String[] temps = toAssignment.split(",");
        for (String t : temps) {
            if (StringUtils.isNotEmpty(t)) {
                taskService.addCandidateGroup(task.getId(), t);
            }
        }
        return true;
    }

    @Override
    public List<Task> getNextTaskInfo(String processInstanceId) {

        List<Task> taskList = new ArrayList<Task>();
        // 由于逻辑问题，当前先不处理下一步任务，只处理该任务是否已经结束
        ProcessInstance processInstance = this.getProceInstance(processInstanceId);
        if (processInstance != null) {// 已经结束
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
            if (!CollectionUtils.isEmpty(tasks)) {
                for (Task task : tasks) {
                    StringBuffer groups = new StringBuffer();
                    Task taskCopy = new TaskEntityImpl();
                    ReflectionUtils.copyBean(task, taskCopy);
                    if (StringUtils.isEmpty(task.getAssignee())) {
                        List<String> list = getTaskCandidateGroup(task);
                        for (String group : list) {
                            groups.append(group + ",");
                        }
                        if (StringUtils.isNotBlank(groups.toString())) {
                            taskCopy.setAssignee(Constants.BIZ_GROUP + groups.deleteCharAt(groups.lastIndexOf(",")));
                        }
                    }
                    taskList.add(taskCopy);
                }
            }
        }
        return taskList;
    }

    @Override
    public Task getTaskBean(String taskID) {

        if (StringUtils.isEmpty(taskID)) {
            return null;
        }
        return taskService.createTaskQuery().taskId(taskID).singleResult();
    }

    /**
     * 获取当前用户对工单有权限处理的任务，并返回操作权限 返回HANDLE，表示可以进行处理，SIGN表示可以进行签收，其他无权限<br>
     * 返回格式：任务ID:权限
     *
     * @param bean     工单对象
     * @param username 用户
     * @return @
     */
    @Override
    public String getWorkAccessTask(BizInfo bean, String username) {

        // 先判断当前工单的所有活动是否可以进行处理
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(bean.getProcessInstanceId()).list();
        for (Task task : tasks) {
            if (username.equals(task.getAssignee())) {
                return task.getId() + ":HANDLE";
            }
        }
        return null;
    }

    @Override
    public String getWorkAccessTask(String taskID, String username) {

        String result = null;
        Task task = getTaskBean(taskID);
        if (task == null) {
            return null;
        }
        // 判断指定处理人
        if (StringUtils.isNotEmpty(task.getAssignee())) {
            if (username.equals(task.getAssignee())) {
                result = Constants.HANDLE;
            }
        } else {
            // 无指定处理人，判断任务角色
            boolean flag = claimRole(task, username);
            if (flag) {
                result = Constants.SIGN;
            }
        }
        return result;
    }

    @Override
    public int getWorkOrderVersion(BizInfo bean) {

        String processDefinitionId = bean.getProcessDefinitionId();
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        if (definition == null) {
            throw new ServiceException("找不到流程定义:" + processDefinitionId);
        }
        return definition.getVersion();
    }

    @Override
    public String[] getWorkOrderHistoryTask(String processInstanceId) {

        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).list();
        List<String> temps = new ArrayList<String>();
        for (HistoricTaskInstance task : tasks) {
            if (task.getEndTime() == null) {
                continue;
            }
            if (!temps.contains(task.getTaskDefinitionKey())) {
                temps.add(task.getTaskDefinitionKey());
            }
        }
        return (String[]) temps.toArray(new String[temps.size()]);
    }

    public HistroyActivitiFlow getHighLightedElement(ProcessDefinitionEntity processDefinitionEntity,
                                                     List<HistoricActivityInstance> historicActivityInstances) {

        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
        List<String> activitys = new ArrayList<String>();// 用以保存高亮的流程
        for (int i = 0; i < historicActivityInstances.size(); i++) {

        }
        return new HistroyActivitiFlow(highFlows, activitys);
    }

    /**
     * 显示流程实例图片
     *
     * @param bean
     * @return @
     */
    @Override
    public InputStream viewProcessImage(BizInfo bean) {

        ProcessInstance processInstance = this.getProceInstance(bean.getProcessInstanceId());
        String processDefinitionId = null;
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(bean.getProcessInstanceId()).singleResult();
            if (historicProcessInstance != null) {
                processDefinitionId = historicProcessInstance.getProcessDefinitionId();
            }
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(bean.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc().list();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processDefinitionId);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        HistroyActivitiFlow histroyActivitiFlow = getHighLightedElement(processDefinition, list);
        try {
            ProcessDiagramGenerator processDiagramGenerator = engineConfiguration.getProcessDiagramGenerator();
            return processDiagramGenerator.generateDiagram(bpmnModel, "PNG", histroyActivitiFlow.getActivitys(),
                    histroyActivitiFlow.getHighFlows());

        } catch (Exception e) {
            logger.error(" 显示流程实例图片失败 : {}", e);
            throw new ServiceException("显示流程实例图片失败!");
        }
    }

    @Override
    public ProcessDefinition getProcDefById(String id) {

        return repositoryService.getProcessDefinition(id);
    }

    @Override
    public ProcessDefinition getLatestProcDefByKey(String key) {

        return repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion()
                .singleResult();
    }

    @Override
    @Transactional
    public boolean copyVariables(ProcessDefinition processDefinition) throws Exception {

        boolean flag = false;
        if (processDefinition != null) {
            ProcessDefinition newProcessDefinition = getLatestProcDefByKey(processDefinition.getKey());
            processModelDao.copyVariables(processDefinition, newProcessDefinition);
            flag = true;
        }
        return flag;
    }
}
