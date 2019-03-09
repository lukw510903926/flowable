package com.flowable.oa.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.flowable.oa.core.entity.*;
import com.flowable.oa.core.entity.auth.SystemRole;
import com.flowable.oa.core.entity.auth.SystemUser;
import com.flowable.oa.core.service.*;
import com.flowable.oa.core.service.auth.ISystemUserService;
import com.flowable.oa.core.util.*;
import com.flowable.oa.core.util.exception.ServiceException;
import com.flowable.oa.core.vo.BizFileVo;
import com.flowable.oa.core.vo.BizLogVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProcessExecuteServiceImpl implements IProcessExecuteService {

    private Logger logger = LoggerFactory.getLogger(IProcessExecuteService.class);

    @Autowired
    private IProcessVariableService processVariableService;

    @Autowired
    private IBizInfoService bizInfoService;

    @Autowired
    private IBizLogService logService;

    @Autowired
    private IBizFileService bizFileService;

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    @Autowired
    private ISystemUserService sysUserService;

    @Autowired
    private BizInfoConfService bizInfoConfService;

    @Autowired
    private IVariableInstanceService instanceService;

    @Value("${biz.file.path}")
    private String bizFileRootPath;

    @Override
    public Map<String, Object> loadBizLogInput(String logId) {

        BizLog logBean = logService.selectByKey(logId);
        Map<String, Object> results = new HashMap<>();
        List<ProcessVariableInstance> values = Optional.ofNullable(logBean).map(entity -> instanceService.loadValueByLog(entity)).orElse(new ArrayList<>());
        values.forEach(instance -> results.put(instance.getVariableName(), instance.getValue()));
        return results;
    }

    /**
     * 根据流程定义ID获取流程名
     */
    @Override
    public String getProcessDefinitionName(String procDefId) {

        return processDefinitionService.getProcDefById(procDefId).getName();
    }

    @Override
    public List<ProcessVariable> loadProcessVariables(BizInfo bean, String taskDefKey) {

        ProcessVariable variable = new ProcessVariable();
        variable.setProcessDefinitionId(bean.getProcessDefinitionId());
        variable.setTaskId(taskDefKey);
        return this.processVariableService.select(variable);
    }

    /**
     * 签收工单
     *
     * @param bizInfo
     * @return
     */
    private BizInfo sign(BizInfo bizInfo, BizInfoConf bizInfoConf) {

        String taskId = bizInfoConf.getTaskId();
        if (StringUtils.isEmpty(taskId)) {
            throw new ServiceException("请确认是否有权先签收任务!");
        }
        String username = WebUtil.getLoginUser().getUsername();
        processDefinitionService.claimTask(taskId, username);
        bizInfoConf.setTaskAssignee(username);
        bizInfoService.saveOrUpdate(bizInfo);
        bizInfo.setTaskAssignee(username);
        this.bizInfoConfService.saveOrUpdate(bizInfoConf);
        return bizInfo;
    }

    @Override
    @Transactional
    public BizInfo createBizDraft(Map<String, Object> params, MultiValueMap<String, MultipartFile> multiValueMap, boolean startProc) {

        String source = MapUtils.getString(params, "$source", "人工发起");
        String procDefId = MapUtils.getString(params, "base.tempID");
        String tempBizId = MapUtils.getString(params, "tempBizId");
        Date now = new Date();
        BizInfo bizInfo;
        BizInfoConf bizInfoConf;
        String username = WebUtil.getLoginUsername();
        if (StringUtils.isNotBlank(tempBizId)) {
            bizInfo = bizInfoService.selectByKey(tempBizId);
            bizInfoConf = this.bizInfoConfService.getMyWork(tempBizId);
        } else {
            bizInfo = new BizInfo();
            bizInfo.setWorkNum(WorkOrderUtil.builWorkNumber(procDefId));
            bizInfoConf = new BizInfoConf();
            bizInfoConf.setTaskAssignee(username);
        }
        bizInfo.setCreateUser(username);
        bizInfo.setSource(source);
        bizInfo.setProcessDefinitionId(procDefId);
        bizInfo.setBizType(getProcessDefinitionName(procDefId));
        bizInfo.setStatus(Constants.BIZ_TEMP);
        bizInfo.setCreateTime(now);
        bizInfo.setTitle(MapUtils.getString(params, "base.workTitle"));
        bizInfoService.saveOrUpdate(bizInfo);
        bizInfoConf.setBizId(bizInfo.getId());
        this.bizInfoConfService.saveOrUpdate(bizInfoConf);
        TaskEntityImpl task = new TaskEntityImpl(); // 开始节点没有任务对象
        task.setId(Constants.TASK_START);
        task.setName(MapUtils.getString(params,"base.handleName"));
        Map<String, List<BizFileVo>> fileMap = saveFile(multiValueMap, now, bizInfo, task);
        //附件的值处理
        fileMap.forEach((key, value) -> params.put(key, JSONObject.toJSONString(value)));
        List<ProcessVariable> processValList = loadProcessVariables(bizInfo, Constants.TASK_START);
        if (startProc) {
            startProc(bizInfo, bizInfoConf, params, now, task, processValList);
        }
        saveOrUpdateVars(bizInfo, Constants.TASK_START, processValList, params, now);
        return bizInfo;
    }

    private BizInfo startProc(BizInfo bizInfo, BizInfoConf bizInfoConf, Map<String, Object> params, Date now, Task task, List<ProcessVariable> processValList) {

        String procDefId = bizInfo.getProcessDefinitionId();
        Map<String, Object> variables = setVariables(bizInfo, params, processValList);
        ProcessInstance instance = processDefinitionService.newProcessInstance(procDefId, variables);
        bizInfo.setProcessInstanceId(instance.getId());
        this.processDefinitionService.autoClaim(instance.getId());// TODO任务创建时的自动签收
        writeBizLog(bizInfo, task, now, params);
        updateBizTaskInfo(bizInfo, bizInfoConf);
        bizInfoService.saveOrUpdate(bizInfo);
        return bizInfo;
    }

    /**
     * 提交工单，实现流转
     *
     * @param params
     * @param fileMap
     * @return
     */
    @Override
    @Transactional
    public BizInfo submit(Map<String, Object> params, MultiValueMap<String, MultipartFile> fileMap) {

        logger.info("params :" + params);
        String bizId = MapUtils.getString(params, "base.bizId");
        BizInfo bizInfo = bizInfoService.selectByKey(bizId);
        if (null == bizInfo) {
            throw new ServiceException("工单不存在");
        }
        BizInfoConf bizInfoConf = this.bizInfoConfService.getMyWork(bizId);
        if (bizInfoConf == null) {
            throw new ServiceException("请确认是否有提交工单权限");
        }
        Date now = new Date();
        List<ProcessVariable> processValList = loadProcessVariables(bizInfo, bizInfo.getTaskDefKey());
        Task task = processDefinitionService.getTaskBean(bizInfoConf.getTaskId());
        String buttonId = MapUtils.getString(params, "base.buttonId");
        Map<String, List<BizFileVo>> bizFileMap = saveFile(fileMap, now, bizInfo, task);
        bizFileMap.forEach((key, value) -> params.put(key, JSONObject.toJSONString(value)));
        if (Constants.SIGN.equalsIgnoreCase(buttonId)) {
            sign(bizInfo, bizInfoConf);
        } else {
            Map<String, Object> variables = this.setVariables(bizInfo, params, processValList);
            processDefinitionService.completeTask(bizInfo, bizInfoConf.getTaskId(), variables);
            saveOrUpdateVars(bizInfo, bizInfoConf.getTaskId(), processValList, params, now);
            updateBizTaskInfo(bizInfo, bizInfoConf);
        }
        bizInfoService.saveOrUpdate(bizInfo);
        writeBizLog(bizInfo, task, now, params);
        return bizInfo;
    }

    private Map<String, Object> setVariables(BizInfo bizInfo, Map<String, Object> params, List<ProcessVariable> processValList) {

        String buttonId = MapUtils.getString(params, "base.buttonId");
        Map<String, Object> variables = new HashMap<>();
        // 设置流程参数
        for (ProcessVariable variable : processValList) {
            if (variable.getIsProcessVariable() != null && variable.getIsProcessVariable()) {
                Object value = WorkOrderUtil.convObject((String) params.get(variable.getName()), variable.getViewComponent());
                variables.put(variable.getName(), value);
            }
        }
        variables.put("SYS_FORMTYPE", params.get(IProcessExecuteService.systemFormType));
        variables.put(Constants.SYS_BUTTON_VALUE, buttonId);
        variables.put(Constants.SYS_BIZ_CREATEUSER, bizInfo.getCreateUser());
        variables.put(Constants.SYS_BIZ_ID, bizInfo.getId());
        variables.put(Constants.COUNTER_SIGN, this.getUserNames((String) params.get("handleUser")));
        return variables;
    }

    /**
     * 保存参数，如果是草稿，那么流程实例ID、任务ID皆留空，还不保存到流程参数；<br />
     * 如果是创单，流程实例ID非空，任务ID留空；<br />
     * 正常流转，流程实例ID、任务ID都非空。
     *
     * @param params
     * @param now
     */
    private void saveOrUpdateVars(BizInfo bizInfo, String taskId, List<ProcessVariable> processValList, Map<String, Object> params, Date now) {

        String procInstId = bizInfo.getProcessInstanceId();
        Map<String, ProcessVariableInstance> currentVars = instanceService.getVarMap(bizInfo, taskId, IVariableInstanceService.VariableLoadType.UPDATABLE);
        for (ProcessVariable processVariable : processValList) {
            String proName = processVariable.getName().trim();
            String value = MapUtils.getString(params, proName);
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            ProcessVariableInstance valueBean = currentVars.get(proName);
            if (null != valueBean) {
                valueBean.setValue(value);
                valueBean.setCreateTime(now);
                valueBean.setHandleUser(WebUtil.getLoginUser().getUsername());
                instanceService.saveOrUpdate(valueBean);
            } else {
                valueBean = new ProcessVariableInstance();
                valueBean.setProcessInstanceId(procInstId);
                valueBean.setHandleUser(WebUtil.getLoginUser().getUsername());
                valueBean.setValue(value);
                valueBean.setCreateTime(now);
                valueBean.setViewComponent(processVariable.getViewComponent());
                valueBean.setVariableId(processVariable.getId());
                valueBean.setVariableAlias(processVariable.getAlias());
                valueBean.setVariableName(processVariable.getName());
                valueBean.setBizId(bizInfo.getId());
                taskId = Constants.TASK_START.equals(processVariable.getTaskId()) ? Constants.TASK_START : taskId;
                valueBean.setTaskId(taskId);
                instanceService.saveOrUpdate(valueBean);
            }
        }
    }

    @Override
    public void updateBizTaskInfo(BizInfo bizInfo, BizInfoConf bizInfoConf) {

        String bizId = bizInfo.getId();
        List<Task> taskList = processDefinitionService.getNextTaskInfo(bizInfo.getProcessInstanceId());
        // 如果nextTaskInfo返回null，标示流程已结束
        if (CollectionUtils.isEmpty(taskList)) {
            this.bizInfoConfService.deleteByBizId(bizId);
            bizInfo.setStatus(Constants.BIZ_END);
            bizInfo.setTaskDefKey(Constants.BIZ_END);
        } else {
            Task taskInfo = taskList.get(0);
            bizInfoConf.setTaskId(taskInfo.getId());
            StringBuilder taskIds = new StringBuilder(taskInfo.getId() + ",");
            StringBuilder taskAssignee = new StringBuilder();
            if (StringUtils.isNotBlank(taskInfo.getAssignee())) {
                taskAssignee.append(taskInfo.getAssignee()).append(",");
                bizInfoConf.setTaskAssignee(taskInfo.getAssignee());
            } else {
                bizInfoConf.setTaskAssignee(null);
            }
            bizInfoConf.setBizId(bizId);
            this.bizInfoConfService.saveOrUpdate(bizInfoConf);
            if (taskList.size() > 1) {
                taskList.forEach(entity -> {
                    BizInfoConf bizConf = new BizInfoConf();
                    bizConf.setTaskId(entity.getId());
                    bizConf.setTaskAssignee(entity.getAssignee());
                    bizConf.setBizId(bizId);
                    this.bizInfoConfService.saveOrUpdate(bizConf);
                    taskIds.append(entity.getId()).append(",");
                    if (StringUtils.isNotBlank(entity.getAssignee())) {
                        taskAssignee.append(entity.getAssignee()).append(",");
                    }
                    bizInfoConf.setBizId(bizId);
                    this.bizInfoConfService.saveOrUpdate(bizConf);
                });
            }
            bizInfo.setTaskId(taskIds.substring(0, taskIds.lastIndexOf(",")));
            String assignee = taskAssignee.toString();
            assignee = StringUtils.isBlank(assignee) ? null : assignee.substring(0, assignee.lastIndexOf(","));
            bizInfo.setStatus(taskInfo.getName());
            bizInfo.setTaskName(taskInfo.getName());
            bizInfo.setTaskDefKey(taskInfo.getTaskDefinitionKey());
            bizInfo.setTaskAssignee(assignee);
        }
    }

    /**
     * 附件保存
     *
     * @param fileMap
     * @param now
     * @param bizInfo
     * @param task
     */
    private Map<String, List<BizFileVo>> saveFile(MultiValueMap<String, MultipartFile> fileMap, Date now, BizInfo bizInfo, Task task) {

        Map<String, List<BizFileVo>> bizFileMap = new HashMap<>();
        if (MapUtils.isNotEmpty(fileMap)) {
            for (String fileCatalog : fileMap.keySet()) {
                List<MultipartFile> files = fileMap.get(fileCatalog);
                if (CollectionUtils.isNotEmpty(files)) {
                    List<BizFileVo> list = new ArrayList<>();
                    files.forEach(file -> {
                        BizFile bizFile = UploadFileUtil.saveFile(file, bizFileRootPath);
                        if (bizFile != null) {
                            bizFile.setCreateDate(now);
                            bizFile.setFileCatalog(fileCatalog);
                            bizFile.setCreateUser(WebUtil.getLoginUsername());
                            bizFile.setTaskId(task.getId());
                            bizFile.setTaskName(task.getName());
                            bizFile.setBizId(bizInfo.getId());
                            bizFileService.addBizFile(bizFile);
                            BizFileVo fileVo = new BizFileVo();
                            BeanUtils.copyProperties(bizFile, fileVo);
                            list.add(fileVo);
                        }
                    });
                    bizFileMap.put(fileCatalog, list);
                }
            }
        }
        return bizFileMap;
    }

    private ArrayList<String> getUserNames(String handleUser) {

        ArrayList<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(handleUser)) {
            if (handleUser.startsWith(Constants.BIZ_GROUP)) {
                String group = handleUser.replace(Constants.BIZ_GROUP, "");
                if (StringUtils.isNotBlank(group)) {
                    SystemRole systemRole = new SystemRole();
                    systemRole.setNameCn(group);
                    List<SystemUser> systemUsers = sysUserService.findUserByRole(systemRole);
                    if (CollectionUtils.isNotEmpty(systemUsers)) {
                        systemUsers.stream().map(SystemUser::getUsername).filter(StringUtils::isNotBlank).forEach(list::add);
                    }
                }
            } else {
                String[] userNames = handleUser.split("\\,");
                Arrays.stream(userNames).filter(StringUtils::isNotBlank).forEach(list::add);
            }
        }
        return list;
    }

    @Override
    public BizLog writeBizLog(BizInfo bizInfo, Task task, Date now, Map<String, Object> params) {

        BizLog logBean = new BizLog();
        logBean.setCreateTime(now);
        logBean.setTaskID(task.getId());
        logBean.setTaskName(task.getName());
        logBean.setBizId(bizInfo.getId());
        logBean.setHandleDescription(MapUtils.getString(params, "base.handleMessage"));
        logBean.setHandleResult(MapUtils.getString(params, "base.handleResult"));
        LoginUser loginUser = WebUtil.getLoginUser();
        logBean.setHandleUser(loginUser.getUsername());
        logBean.setHandleUserName(loginUser.getName());
        logBean.setHandleName(MapUtils.getString(params, "base.handleName"));
        logService.addBizLog(logBean);
        return logBean;
    }

    /**
     * 获取某个流程的开始按钮
     *
     * @param tempId
     * @return
     * @throws ServiceException
     */
    @Override
    public Map<String, String> loadStartButtons(String tempId) {

        Map<String, String> buttons = processDefinitionService.loadStartButtons(tempId);
        if (MapUtils.isEmpty(buttons)) {
            buttons = new HashMap<>();
            buttons.put("submit", "提交");
        }
        return buttons;
    }

    /**
     * 根据工单号查询工单信息，并且处理工单的处理权限,KEY列表如下<br>
     * ---ID跟taskID配套，如果传了taskID,则会判断当前是否可编辑，否则工单只呈现 workInfo： 工单对象信息<br>
     * CURRE_OP: 当前用户操作权限<br>
     * ProcessValBeanMap :需要呈现的业务字段<br>
     * ProcessTaskValBeans:当前编辑的业务字段<br>
     * extInfo :扩展信息<br>
     * extInfo.createUser:创建人信息<br>
     * serviceInfo:业务字段信息内容<br>
     * annexs:附件列表<br>
     * workLogs:日志
     *
     * @param bizId
     * @return
     * @throws ServiceException
     */
    @Override
    public Map<String, Object> queryWorkOrder(String bizId) {

        String loginUser = WebUtil.getLoginUser().getUsername();
        Map<String, Object> result = new HashMap<>();
        BizInfo bizInfo = bizInfoService.selectByKey(bizId);
        if (bizInfo == null) {
            throw new ServiceException("找不到工单:" + bizId);
        }
        result.put("workInfo", bizInfo);
        BizInfoConf bizInfoConf = this.bizInfoConfService.getMyWork(bizId);
        String taskId = Optional.ofNullable(bizInfoConf).map(BizInfoConf::getTaskId).orElse(null);
        // 处理扩展信息
        Map<String, Object> extInfo = new HashMap<>();
        extInfo.put("createUser", sysUserService.getUserByUsername(bizInfo.getCreateUser()));
        extInfo.put("base_taskID", taskId);
        result.put("extInfo", extInfo);
        String currentOp = Optional.ofNullable(taskId).map(task -> processDefinitionService.getWorkAccessTask(task, WebUtil.getLoginUser().getUsername())).orElse(null);
        // 子工单信息
        result.put("subBizInfo", bizInfoService.getBizByParentId(bizId));
        result.put("CURRE_OP", currentOp);
        result.put("$currentTaskName", bizInfo.getTaskName());
        List<ProcessVariable> currentVariables = loadProcessVariables(bizInfo, bizInfo.getTaskDefKey());
        // 加载当前编辑的业务字段,只有当前操作为HANDLE的时候才加载
        if (Constants.HANDLE.equalsIgnoreCase(currentOp)) {
            result.put("currentVariables", currentVariables);
            extInfo.put("handleUser", sysUserService.getUserByUsername(loginUser));
            Map<String, String> buttons = processDefinitionService.findOutGoingTransNames(taskId);
            if (MapUtils.isEmpty(buttons)) {
                buttons = new HashMap<>();
                buttons.put("submit", "提交");
            }
            result.put("SYS_BUTTON", buttons);
        } else if (Constants.SIGN.equalsIgnoreCase(currentOp)) {
            Map<String, String> buttons = new HashMap<>(1);
            buttons.put(Constants.SIGN, "签收");
            result.put("SYS_BUTTON", buttons);
        }
        List<BizLogVo> bizLogVos = this.loadBizLog(bizInfo);
        this.loadBizFile(bizInfo, bizLogVos);
        this.loadVariableInstance(bizInfo, bizLogVos);
        result.put("workLogs", bizLogVos);
        return result;
    }

    /**
     * 加载日志
     *
     * @param bizInfo
     * @return
     */
    private List<BizLogVo> loadBizLog(BizInfo bizInfo) {

        List<BizLogVo> bizLogVos = new ArrayList<>();
        List<BizLog> bizLogs = logService.loadBizLogs(bizInfo.getId());
        if (CollectionUtils.isNotEmpty(bizLogs)) {
            bizLogs.forEach(bizLog -> {
                BizLogVo bizLogVo = new BizLogVo();
                BeanUtils.copyProperties(bizLog, bizLogVo);
                bizLogVos.add(bizLogVo);
            });
        }
        return bizLogVos;
    }

    /**
     * 加载工单附件
     *
     * @param bizInfo
     * @param bizLogVos
     */
    private void loadBizFile(BizInfo bizInfo, List<BizLogVo> bizLogVos) {

        List<BizFile> bizFiles = this.bizFileService.loadBizFilesByBizId(bizInfo.getId(), null);
        if (CollectionUtils.isNotEmpty(bizFiles) && CollectionUtils.isNotEmpty(bizLogVos)) {
            Map<String, List<BizFile>> taskFileMap = bizFiles.stream().collect(Collectors.groupingBy(BizFile::getTaskId));
            bizLogVos.forEach(entity -> entity.setBizFiles(taskFileMap.get(entity.getTaskID())));
        }
    }

    /**
     * 处理参数中的配置值
     *
     * @param bizInfo
     * @param bizLogVos
     */
    private void loadVariableInstance(BizInfo bizInfo, List<BizLogVo> bizLogVos) {

        List<ProcessVariableInstance> variableInstances = this.instanceService.loadInstances(bizInfo);
        if (CollectionUtils.isNotEmpty(variableInstances) && CollectionUtils.isNotEmpty(bizLogVos)) {
            Map<String, List<ProcessVariableInstance>> taskInstanceMap = variableInstances.stream().collect(Collectors.groupingBy(ProcessVariableInstance::getTaskId));
            bizLogVos.forEach(log -> log.setVariableInstances(taskInstanceMap.get(log.getTaskID())));
        }
    }

    /**
     * 下载或查看文件
     *
     * @param action
     * @param id
     * @return [文件类型, InputStream]
     * @throws ServiceException
     */
    @Override
    public Object[] downloadFile(String action, String id) {

        Object[] result = new Object[4];
        if ("work".equalsIgnoreCase(action)) {
            BizInfo bean = bizInfoService.selectByKey(id);
            if (bean == null) {
                throw new ServiceException("找不到工单");
            }
            result[0] = "IMAGE";
            result[1] = processDefinitionService.viewProcessImage(bean.getProcessInstanceId());
        } else {
            BizFile bean = bizFileService.selectByKey(id);
            if (bean == null) {
                throw new ServiceException("找不到附件");
            }
            File file = UploadFileUtil.getUploadFile(bean, bizFileRootPath);
            if (!file.exists()) {
                throw new ServiceException("找不到附件");
            }
            InputStream is = null;
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                logger.error("文件未找到 : {}", e);
            }
            result[0] = bean.getFileType();
            result[1] = is;
            result[2] = file.length();
            result[3] = bean.getName();
        }
        return result;
    }
}
