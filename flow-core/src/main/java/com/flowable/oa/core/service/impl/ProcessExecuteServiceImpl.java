package com.flowable.oa.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.flowable.oa.core.entity.BizFile;
import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.BizInfoConf;
import com.flowable.oa.core.entity.BizLog;
import com.flowable.oa.core.entity.ProcessVariable;
import com.flowable.oa.core.entity.ProcessVariableInstance;
import com.flowable.oa.core.entity.auth.SystemRole;
import com.flowable.oa.core.entity.auth.SystemUser;
import com.flowable.oa.core.service.BizInfoConfService;
import com.flowable.oa.core.service.IBizFileService;
import com.flowable.oa.core.service.IBizInfoService;
import com.flowable.oa.core.service.IBizLogService;
import com.flowable.oa.core.service.IProcessDefinitionService;
import com.flowable.oa.core.service.IProcessExecuteService;
import com.flowable.oa.core.service.IProcessVariableService;
import com.flowable.oa.core.service.IVariableInstanceService;
import com.flowable.oa.core.service.auth.ISystemUserService;
import com.flowable.oa.core.util.Constants;
import com.flowable.oa.core.util.LoginUser;
import com.flowable.oa.core.util.UploadFileUtil;
import com.flowable.oa.core.util.WebUtil;
import com.flowable.oa.core.util.WorkOrderUtil;
import com.flowable.oa.core.util.exception.ServiceException;
import com.flowable.oa.core.vo.BizFileVo;
import com.flowable.oa.core.vo.BizLogVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020/2/20 5:57 下午
 */
@Slf4j
@Service
public class ProcessExecuteServiceImpl implements IProcessExecuteService {

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

    @Autowired
    private Environment environment;

    @Override
    public Map<String, Object> loadBizLogInput(Long logId) {

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
        String username = WebUtil.getLoginUsername();
        processDefinitionService.claimTask(taskId, username);
        bizInfoConf.setTaskAssignee(username);
        this.bizInfoConfService.saveOrUpdate(bizInfoConf);
        return bizInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizInfo createBizDraft(Map<String, Object> params, MultiValueMap<String, MultipartFile> multiValueMap, boolean startProc) {

        String source = MapUtils.getString(params, "$source", "人工发起");
        String procDefId = MapUtils.getString(params, "base.tempID");
        Long tempBizId = MapUtils.getLong(params, "tempBizId");
        Date now = new Date();
        BizInfo bizInfo;
        String username = WebUtil.getLoginUsername();
        if (null != tempBizId) {
            bizInfo = bizInfoService.selectByKey(tempBizId);
        } else {
            bizInfo = new BizInfo();
            bizInfo.setWorkNum(WorkOrderUtil.buildWorkNumber(procDefId));
        }
        bizInfo.setCreateUser(username);
        bizInfo.setSource(source);
        bizInfo.setProcessDefinitionId(procDefId);
        bizInfo.setBizType(getProcessDefinitionName(procDefId));
        bizInfo.setStatus(Constants.BIZ_TEMP);
        bizInfo.setCreateTime(now);
        bizInfo.setTitle(MapUtils.getString(params, "base.workTitle"));
        bizInfoService.saveOrUpdate(bizInfo);
        // 开始节点没有任务对象
        TaskEntityImpl task = new TaskEntityImpl();
        task.setId(Constants.TASK_START);
        task.setName(MapUtils.getString(params, "base.handleName"));
        Map<String, List<BizFileVo>> fileMap = saveFile(multiValueMap, now, bizInfo, task);
        //附件的值处理
        fileMap.forEach((key, value) -> params.put(key, JSONObject.toJSONString(value)));
        List<ProcessVariable> processValList = loadProcessVariables(bizInfo, Constants.TASK_START);
        if (startProc) {
            Map<String, Object> variables = setVariables(bizInfo, params, processValList);
            ProcessInstance instance = processDefinitionService.newProcessInstance(procDefId, variables);
            bizInfo.setProcessInstanceId(instance.getId());
            // TODO任务创建时的自动签收
            this.processDefinitionService.autoClaim(instance.getId());
            writeBizLog(bizInfo, task, now, params);
            updateBizTaskInfo(bizInfo);
        }
        saveOrUpdateVars(bizInfo, Constants.TASK_START, processValList, params, now);
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
    @Transactional(rollbackFor = Exception.class)
    public BizInfo submit(Map<String, Object> params, MultiValueMap<String, MultipartFile> fileMap) {

        log.info("params : {}", params);
        Long bizId = MapUtils.getLong(params, "base.bizId");
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
            updateBizTaskInfo(bizInfo);
        }
        writeBizLog(bizInfo, task, now, params);
        return bizInfo;
    }

    private Map<String, Object> setVariables(BizInfo bizInfo, Map<String, Object> params, List<ProcessVariable> processValList) {

        String buttonId = MapUtils.getString(params, "base.buttonId");
        String handleUser = MapUtils.getString(params, "handleUser");
        Map<String, Object> variables = new HashMap<>();
        // 设置流程参数
        for (ProcessVariable variable : processValList) {
            boolean isProcessVariable = Optional.ofNullable(variable.getIsProcessVariable()).orElse(false);
            if (!isProcessVariable) {
                variables.put(variable.getName(), params.get(variable.getName()));
            }
        }
        variables.put(Constants.SYS_BUTTON_VALUE, buttonId);
        variables.put(Constants.SYS_BIZ_CREATEUSER, bizInfo.getCreateUser());
        variables.put(Constants.SYS_BIZ_ID, bizInfo.getId());
        variables.put(Constants.COUNTER_SIGN, this.getUserNames(handleUser));
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
            }
            instanceService.saveOrUpdate(valueBean);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBizTaskInfo(BizInfo bizInfo) {

        Long bizId = bizInfo.getId();
        List<Task> taskList = processDefinitionService.getNextTaskInfo(bizInfo.getProcessInstanceId());
        this.bizInfoConfService.deleteByBizId(bizId);
        // 如果nextTaskInfo返回null，标示流程已结束
        if (CollectionUtils.isEmpty(taskList)) {
            bizInfo.setStatus(Constants.BIZ_END);
            bizInfo.setTaskDefKey(Constants.BIZ_END);
        } else {
            taskList.forEach(task -> {
                BizInfoConf bizInfoConf = new BizInfoConf();
                bizInfoConf.setBizId(bizId);
                bizInfoConf.setCreateTime(new Date());
                bizInfoConf.setTaskId(task.getId());
                bizInfoConf.setTaskAssignee(task.getAssignee());
                bizInfoConf.setBizId(bizId);
                this.bizInfoConfService.saveOrUpdate(bizInfoConf);
            });
            Task task = taskList.get(0);
            bizInfo.setStatus(task.getName());
            bizInfo.setTaskName(task.getName());
            bizInfo.setTaskDefKey(task.getTaskDefinitionKey());
            this.bizInfoService.saveOrUpdate(bizInfo);
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

        String bizFileRootPath = environment.getProperty("biz.file.path");
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
                            bizFileService.save(bizFile);
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
    @Transactional(rollbackFor = Exception.class)
    public BizLog writeBizLog(BizInfo bizInfo, Task task, Date now, Map<String, Object> params) {

        BizLog logBean = new BizLog();
        logBean.setCreateTime(now);
        logBean.setTaskId(task.getId());
        logBean.setTaskName(task.getName());
        logBean.setBizId(bizInfo.getId());
        LoginUser loginUser = WebUtil.getLoginUser();
        logBean.setHandleUser(loginUser.getUsername());
        logBean.setHandleUserName(loginUser.getName());
        logBean.setHandleName(MapUtils.getString(params, "base.handleName"));
        logBean.setHandleResult(MapUtils.getString(params, "base.handleResult"));
        logBean.setHandleDescription(MapUtils.getString(params, "base.handleMessage"));
        logService.save(logBean);
        return logBean;
    }

    /**
     * 获取某个流程的开始按钮
     *
     * @param tempId
     * @return
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
     */
    @Override
    public Map<String, Object> queryWorkOrder(Long bizId) {

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
            result.put(Constants.SYS_BUTTON, buttons);
        } else if (Constants.SIGN.equalsIgnoreCase(currentOp)) {
            Map<String, String> buttons = new HashMap<>(1);
            buttons.put(Constants.SIGN, "签收");
            result.put(Constants.SYS_BUTTON, buttons);
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
        BizLog entity = new BizLog();
        entity.setBizId(bizInfo.getId());
        List<BizLog> bizLogs = logService.select(entity);
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

        BizFile bizFile = new BizFile();
        bizFile.setBizId(bizInfo.getId());
        List<BizFile> bizFiles = this.bizFileService.select(bizFile);
        if (CollectionUtils.isNotEmpty(bizFiles) && CollectionUtils.isNotEmpty(bizLogVos)) {
            Map<String, List<BizFile>> taskFileMap = bizFiles.stream().collect(Collectors.groupingBy(BizFile::getTaskId));
            bizLogVos.forEach(entity -> entity.setBizFiles(taskFileMap.get(entity.getTaskId())));
        }
    }

    /**
     * 处理参数中的配置值
     *
     * @param bizInfo
     * @param bizLogVos
     */
    private void loadVariableInstance(BizInfo bizInfo, List<BizLogVo> bizLogVos) {

        ProcessVariableInstance variableInstance = new ProcessVariableInstance();
        variableInstance.setBizId(bizInfo.getId());
        List<ProcessVariableInstance> variableInstances = this.instanceService.select(variableInstance);
        if (CollectionUtils.isNotEmpty(variableInstances) && CollectionUtils.isNotEmpty(bizLogVos)) {
            Map<String, List<ProcessVariableInstance>> taskInstanceMap = variableInstances.stream().collect(Collectors.groupingBy(ProcessVariableInstance::getTaskId));
            bizLogVos.forEach(log -> log.setVariableInstances(taskInstanceMap.get(log.getTaskId())));
        }
    }

}
