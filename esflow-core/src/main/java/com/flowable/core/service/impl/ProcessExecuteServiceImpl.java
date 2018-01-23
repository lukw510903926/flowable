package com.flowable.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import com.flowable.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import com.flowable.core.service.IVariableInstanceService.VariableLoadType;
import com.flowable.common.exception.ServiceException;
import com.flowable.common.utils.DateUtils;
import com.flowable.common.utils.LoginUser;
import com.flowable.core.bean.BizFile;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.BizInfoConf;
import com.flowable.core.bean.BizLog;
import com.flowable.core.bean.ProcessVariable;
import com.flowable.core.bean.ProcessVariableInstance;
import com.flowable.core.bean.auth.SystemRole;
import com.flowable.core.service.auth.ISystemUserService;
import com.flowable.core.util.Constants;
import com.flowable.core.util.UploadFileUtil;
import com.flowable.core.util.WebUtil;
import com.flowable.core.util.WorkOrderUtil;

@Service
@Transactional(readOnly = true)
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

	@Override
	public Map<String, Object> loadBizLogInput(String logId) {

		BizLog logBean = logService.getBizLogById(logId);
		Map<String, Object> results = new HashMap<String, Object>();
		if (logBean == null) {
			return results;
		}
		List<ProcessVariableInstance> values = instanceService.loadValueByLog(logBean);
		if (CollectionUtils.isNotEmpty(values)) {
			values.forEach(instance -> results.put(instance.getVariable().getName(), instance.getValue()));
		}
		return results;
	}

	/**
	 * 加载所有的流程
	 */
	@Override
	public Map<String, Object> loadProcessList() {
		return processDefinitionService.loadProcessList();
	}

	/**
	 * 根据流程定义ID获取流程名
	 */
	public String getProcessDefinitionName(String procDefId) {

		return processDefinitionService.getProcDefById(procDefId).getName();
	}

	@Override
	public List<ProcessVariable> loadHandleProcessValBean(BizInfo bean, String taskId) {

		Task task = processDefinitionService.getTaskBean(taskId);
		ProcessVariable variable = new ProcessVariable();
		variable.setProcessDefinitionId(bean.getProcessDefinitionId());
		variable.setVersion(processDefinitionService.getWorkOrderVersion(bean));
		taskId = task == null ? taskId : task.getTaskDefinitionKey();
		variable.setTaskId(taskId);
		return this.processVariableService.findProcessVariables(variable);
	}

	/**
	 * 加载流程参数
	 *
	 * @param processDefinitionId
	 * @return
	 */
	public List<ProcessVariable> loadProcessValBean(String processDefinitionId) {

		Integer version = 0;
		if (StringUtils.isNotBlank(processDefinitionId)) {
			String[] definition = processDefinitionId.split(":");
			version = Integer.valueOf(definition[1]);
		}
		ProcessVariable variable = new ProcessVariable();
		variable.setTaskId(Constants.TASK_START);
		variable.setVersion(version);
		variable.setProcessDefinitionId(processDefinitionId);
		return processVariableService.findProcessVariables(variable);
	}

	/**
	 * 签收工单
	 *
	 * @param bizInfo
	 * @param username
	 * @return
	 */
	private BizInfo sign(BizInfo bizInfo, BizInfoConf bizInfoConf, String username) {

		String taskId = bizInfoConf.getTaskId();
		if (StringUtils.isEmpty(taskId)) {
			throw new ServiceException("请确认是否有权先签收任务!");
		}
		processDefinitionService.claimTask(taskId, username);
		bizInfoConf.setTaskAssignee(username);
		bizInfoService.updateBizInfo(bizInfo);
		bizInfo.setTaskAssignee(username);
		this.bizInfoConfService.saveOrUpdate(bizInfoConf);
		return bizInfo;
	}

	/**
	 * 根据工单号修改工单属性，只修改工单的业务属性
	 */
	@Override
	@Transactional
	public BizInfo update(Map<String, Object> params) {

		String workNumber = (String) params.get("base.bizId");
		if (StringUtils.isEmpty(workNumber)) {
			throw new ServiceException("工单号为空");
		}
		BizInfo bean = bizInfoService.get(workNumber);
		if (bean == null) {
			throw new ServiceException("找不到工单");
		}
		String createUser = bean.getCreateUser();
		if (!createUser.equals(WebUtil.getLoginUser().getUsername())) {
			throw new ServiceException("只有当前创单用户才能修改");
		}
		List<ProcessVariable> processValList = loadHandleProcessValBean(bean, null);
		List<ProcessVariableInstance> list4 = instanceService.loadInstances(bean);
		// 设置流程参数

		for (ProcessVariable proAbs : processValList) {
			// 如果数据为空则不更新
			String value = (String) params.get(proAbs.getName());
			if (StringUtils.isEmpty(value)) {
				continue;
			}
			ProcessVariableInstance valueBean = null;
			for (ProcessVariableInstance instAbs : list4) {
				if (instAbs.getVariable().getId().equals(proAbs.getId())) {
					valueBean = instAbs;
					break;
				}
			}
			if (valueBean == null) {
				valueBean = new ProcessVariableInstance();
				valueBean.setProcessInstanceId(bean.getProcessInstanceId());
				valueBean.setValue(value);
				valueBean.setVariable(proAbs);
				instanceService.addProcessInstance(valueBean);
			} else {
				valueBean.setValue(value);
				instanceService.updateProcessInstance(valueBean);
			}
		}
		return bean;
	}

	@Override
	@Transactional
	public BizInfo createBizDraft(Map<String, Object> params, MultiValueMap<String, MultipartFile> multiValueMap,
			boolean startProc, String[] deleFileId) {

		String source = (String) params.get("$source");
		source = StringUtils.isBlank(source) ? "人工发起" : source;
		String procDefId = (String) params.get("base.tempID");
		String dt1 = (String) params.get("base.limitTime");
		String createUser = (String) params.get("base.createUser");
		String tempBizId = (String) params.get("tempBizId");
		Date limitTime = DateUtils.parseDate(dt1);
		Date now = new Date();
		BizInfo bizInfo = null;
		// BizInfoConf bizInfoConf = null;
		if (StringUtils.isNotBlank(tempBizId)) {
			bizInfo = bizInfoService.get(tempBizId);
			// bizInfoConf = this.bizInfoConfService.get(bizInfo.getId());
		} else {
			bizInfo = new BizInfo();
			bizInfo.setWorkNum(WorkOrderUtil.builWorkNumber(procDefId));
		}
		// if (bizInfoConf == null) {
		BizInfoConf bizInfoConf = new BizInfoConf();
		bizInfoConf.setBizInfo(bizInfo);
		// }
		bizInfo.setSource(source);
		bizInfo.setLimitTime(limitTime);
		bizInfo.setProcessDefinitionId(procDefId);
		bizInfo.setBizType(getProcessDefinitionName(procDefId));
		bizInfo.setStatus(Constants.BIZ_TEMP);
		bizInfo.setCreateTime(now);

		createUser = StringUtils.isNotBlank(createUser) ? createUser : WebUtil.getLoginUser().getUsername();
		bizInfo.setCreateUser(createUser);
		bizInfoConf.setTaskAssignee(createUser);
		bizInfo.setTitle((String) params.get("base.workTitle"));
		bizInfoConf.setBizInfo(bizInfo);
		this.bizInfoConfService.saveOrUpdate(bizInfoConf);
		bizInfoService.addBizInfo(bizInfo);
		if (startProc) {
			startProc(bizInfo, bizInfoConf, params, now);
		} else {
			List<ProcessVariable> processValList = loadHandleProcessValBean(bizInfo, Constants.TASK_START);
			saveOrUpdateVars(bizInfo, Constants.TASK_START, processValList, params, now);
		}
		TaskEntityImpl task = new TaskEntityImpl(); // 开始节点没有任务对象
		task.setId(Constants.TASK_START);
		task.setName((String) params.get("base.handleName"));
		saveFile(multiValueMap, now, bizInfo, task);
		this.deleBizFiles(deleFileId);
		return bizInfo;
	}

	public BizInfo startProc(BizInfo bizInfo, BizInfoConf bizInfoConf, Map<String, Object> params, Date now) {

		String procDefId = bizInfo.getProcessDefinitionId();
		List<ProcessVariable> processValList = loadHandleProcessValBean(bizInfo, Constants.TASK_START);
		Map<String, Object> variables = setVariables(bizInfo, params, processValList);
		ProcessInstance instance = processDefinitionService.newProcessInstance(procDefId, variables);
		bizInfo.setProcessInstanceId(instance.getId());
		this.processDefinitionService.autoClaim(instance.getId());// TODO任务创建时的自动签收

		TaskEntityImpl task = new TaskEntityImpl(); // 开始节点没有任务对象
		task.setId(Constants.TASK_START);
		task.setName((String) params.get("base.handleName"));
		writeBizLog(bizInfo, task, now, params);
		updateBizTaskInfo(bizInfo, bizInfoConf);
		bizInfoService.update(bizInfo);
		// 保存流程字段
		saveOrUpdateVars(bizInfo, Constants.TASK_START, processValList, params, now);
		return bizInfo;
	}

	/**
	 * 提交工单，实现流转
	 *
	 * @param params
	 * @param fileMap
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@Transactional
	public BizInfo submit(Map<String, Object> params, MultiValueMap<String, MultipartFile> fileMap) {

		logger.info("params :" + params);
		String bizId = (String) params.get("base.bizId");
		BizInfo bizInfo = bizInfoService.getByBizId(bizId);
		if (null == bizInfo) {
			throw new ServiceException("工单不存在");
		}
		BizInfoConf bizInfoConf = this.bizInfoConfService.getMyWork(bizId);
		if (bizInfoConf == null) {
			throw new ServiceException("请确认是否有提交工单权限");
		}
		String taskId = bizInfoConf.getTaskId();
		List<ProcessVariable> processValList = loadHandleProcessValBean(bizInfo, taskId);
		this.submit(params, fileMap, bizInfo, bizInfoConf, processValList);
		return bizInfo;
	}

	@Override
	@Transactional
	public BizInfo updateBiz(Map<String, Object> params, MultiValueMap<String, MultipartFile> fileMap) {

		String bizId = (String) params.get("base.bizId");
		BizInfo bizInfo = bizInfoService.getByBizId(bizId);
		if (null == bizInfo) {
			throw new ServiceException("工单不存在");
		}
		BizInfoConf bizInfoConf = this.bizInfoConfService.getMyWork(bizId);
		if (bizInfoConf == null) {
			throw new ServiceException("请确认是否有提交工单权限");
		}
		List<ProcessVariable> processValList = loadHandleProcessValBean(bizInfo, bizInfoConf.getTaskId());
		processValList.addAll(loadHandleProcessValBean(bizInfo, Constants.TASK_START));
		this.submit(params, fileMap, bizInfo, bizInfoConf, processValList);
		return bizInfo;
	}

	private BizInfo submit(Map<String, Object> params, MultiValueMap<String, MultipartFile> fileMap, BizInfo bizInfo,
			BizInfoConf bizInfoConf, List<ProcessVariable> processValList) {

		logger.info("params :" + params);
		Date now = new Date();
		Task task = processDefinitionService.getTaskBean(bizInfoConf.getTaskId());
		String buttonId = (String) params.get("base.buttonId");
		if (Constants.SIGN.equalsIgnoreCase(buttonId)) {
			sign(bizInfo, bizInfoConf, WebUtil.getLoginUser().getUsername());
		} else {
			completeTask(params, now, bizInfo, bizInfoConf, processValList);
		}
		bizInfoService.updateBizInfo(bizInfo);
		saveFile(fileMap, now, bizInfo, task);
		writeBizLog(bizInfo, task, now, params);
		return bizInfo;
	}

	private void completeTask(Map<String, Object> params, Date now, BizInfo bizInfo, BizInfoConf bizInfoConf,
			List<ProcessVariable> processValList) {

		Map<String, Object> variables = this.setVariables(bizInfo, params, processValList);
		processDefinitionService.completeTask(bizInfo, bizInfoConf.getTaskId(), WebUtil.getLoginUser(), variables);
		saveOrUpdateVars(bizInfo, bizInfoConf.getTaskId(), processValList, params, now);
		updateBizTaskInfo(bizInfo, bizInfoConf);
	}

	private Map<String, Object> setVariables(BizInfo bizInfo, Map<String, Object> params,List<ProcessVariable> processValList) {

		String buttonId = (String) params.get("base.buttonId");
		Map<String, Object> variables = new HashMap<String, Object>();
		// 设置流程参数
		for (ProcessVariable abc : processValList) {
			if (abc.isProcessVariable() != null && abc.isProcessVariable()) {
				Object value = WorkOrderUtil.convObject((String) params.get(abc.getName()), abc.getViewComponent());
				variables.put(abc.getName(), value);
			}
		}
		variables.put("SYS_FORMTYPE", params.get(IProcessExecuteService.systemFormType));
		variables.put(Constants.SYS_BUTTON_VALUE, buttonId);
		variables.put(Constants.SYS_BIZ_CREATEUSER, bizInfo.getCreateUser());
		variables.put(Constants.SYS_BIZ_ID, bizInfo.getId());
		variables.put(Constants.COUNTER_SIGN, this.getUsernames((String) params.get("handleUser")));
		return variables;
	}

	@Override
	public void saveOrUpdateVars(BizInfo bizInfo, String taskId, List<ProcessVariable> processValList,
			Map<String, Object> params, Date now) {

		String procInstId = bizInfo.getProcessInstanceId();
		Map<String, ProcessVariableInstance> currentVars = instanceService.getVarMap(bizInfo, taskId,
				VariableLoadType.UPDATABLE);
		for (ProcessVariable processVariable : processValList) {
			String proName = processVariable.getName().trim();
			String component = processVariable.getViewComponent();
			String value = "REQUIREDFILE".equalsIgnoreCase(component) ? "file" : (String) params.get(proName);
			if (StringUtils.isEmpty(value)) {
				continue;
			}
			ProcessVariableInstance valueBean = currentVars.get(proName);
			if (null != valueBean) {
				valueBean.setValue(value);
				valueBean.setCreateTime(now);
				instanceService.updateProcessInstance(valueBean);
			} else {
				valueBean = new ProcessVariableInstance();
				valueBean.setProcessInstanceId(procInstId);
				valueBean.setValue(value);
				valueBean.setCreateTime(now);
				valueBean.setVariable(processVariable);
				valueBean.setBizId(bizInfo.getId());
				if (Constants.TASK_START.equalsIgnoreCase(processVariable.getTaskId())) {
					valueBean.setTaskId(Constants.TASK_START);
				} else {
					valueBean.setTaskId(taskId);
				}
				instanceService.addProcessInstance(valueBean);
			}
		}
	}

	private void deleBizFiles(String[] ids) {

		File file;
		BizFile bizFile;
		List<String> filePathList = new ArrayList<String>();
		if (ArrayUtils.isNotEmpty(ids)) {
			for (String id : ids) {
				bizFile = bizFileService.getBizFileById(id);
				filePathList.add(bizFile.getPath());
			}
			bizFileService.deleteBizFile(ids);
			for (String filePath : filePathList) {
				file = new File(filePath);
				FileUtils.deleteQuietly(file);
			}
		}
	}

	@Override
	public void updateBizTaskInfo(BizInfo bizInfo, BizInfoConf bizInfoConf) {

		List<Task> taskList = processDefinitionService.getNextTaskInfo(bizInfo.getProcessInstanceId());
		// 如果nextTaskInfo返回null，标示流程已结束
		if (CollectionUtils.isEmpty(taskList)) {
			bizInfoConf = this.bizInfoConfService.getMyWork(bizInfo.getId());
			this.bizInfoConfService.delete(bizInfoConf);
			bizInfo.setStatus(Constants.BIZ_END);
			bizInfo.setTaskDefKey(Constants.BIZ_END);
		} else {
			Task taskInfo = taskList.get(0);
			bizInfoConf.setTaskId(taskInfo.getId());
			bizInfoConf.setTaskAssignee(taskInfo.getAssignee());
			StringBuffer taskIds = new StringBuffer(taskInfo.getId() + ",");
			StringBuffer taskAssignee = new StringBuffer();
			if (StringUtils.isNotBlank(taskInfo.getAssignee())) {
				taskAssignee.append(taskInfo.getAssignee() + ",");
			}
			this.bizInfoConfService.saveOrUpdate(bizInfoConf);
			BizInfoConf bizConf = null;
			if (taskList.size() > 1) {
				for (int i = 1; i < taskList.size(); i++) {
					bizConf = new BizInfoConf();
					taskInfo = taskList.get(i);
					bizConf.setTaskId(taskInfo.getId());
					bizConf.setTaskAssignee(taskInfo.getAssignee());
					bizConf.setBizInfo(bizInfo);
					this.bizInfoConfService.saveOrUpdate(bizConf);
					taskIds.append(taskIds + taskInfo.getId() + ",");
					if (StringUtils.isNotBlank(taskInfo.getAssignee())) {
						taskAssignee.append(taskAssignee + taskInfo.getAssignee() + ",");
					}
					this.bizInfoConfService.saveOrUpdate(bizConf);
				}
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
	private void saveFile(MultiValueMap<String, MultipartFile> fileMap, Date now, BizInfo bizInfo, Task task) {

		if (MapUtils.isNotEmpty(fileMap)) {
			for (String fileCatalog : fileMap.keySet()) {
				List<MultipartFile> files = fileMap.get(fileCatalog);
				if (CollectionUtils.isNotEmpty(files)) {
					files.forEach(file -> {
						BizFile bizFile = UploadFileUtil.saveFile(file);
						if (bizFile != null) {
							bizFile.setCreateDate(now);
							bizFile.setFileCatalog(fileCatalog);
							bizFile.setCreateUser(WebUtil.getLoginUser().getUsername());
							if (null != task) {
								bizFile.setTaskName(task.getName());
								bizFile.setTaskId(task.getId());
							}
							bizFile.setBizInfo(bizInfo);
							bizFileService.addBizFile(bizFile);
						}
					});
				}
			}
		}
	}

	private ArrayList<String> getUsernames(String handleUser) {

		ArrayList<String> list = new ArrayList<String>();
		if (StringUtils.isNotBlank(handleUser)) {
			if (handleUser.startsWith(Constants.BIZ_GROUP)) {
				String group = handleUser.replace(Constants.BIZ_GROUP, "");
				if (StringUtils.isNotBlank(group)) {
					List<String> usernames = sysUserService.findUserByRole(new SystemRole(null, group));
					if (CollectionUtils.isNotEmpty(usernames)) {
						usernames.forEach(username -> list.add(username));
					}
				}
			} else {
				String[] usernames = handleUser.split("\\,");
				for (String username : usernames) {
					if (StringUtils.isNotBlank(username)) {
						list.add(username);
					}
				}
			}
		}
		return list;
	}

	@Override
	public void writeBizLog(BizInfo bizInfo, Task task, Date now, Map<String, Object> params) {

		BizLog logBean = new BizLog();
		logBean.setCreateTime(now);
		logBean.setTaskID(task.getId());
		logBean.setTaskName(task.getName());
		logBean.setBizInfo(bizInfo);
		logBean.setHandleDescription((String) params.get("base.handleMessage"));
		logBean.setHandleResult((String) params.get("base.handleResult"));
		LoginUser loginUser = WebUtil.getLoginUser();
		String username = loginUser != null ? loginUser.getUsername() : (String) params.get("loginUser");
		logBean.setHandleUser(username);
		logBean.setHandleName((String) params.get("base.handleName"));
		logService.addBizLog(logBean);
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
		if (buttons == null || buttons.size() <= 0) {
			buttons = buttons == null ? new HashMap<String, String>() : buttons;
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
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> queryWorkOrder(String id) {

		String loginUser = WebUtil.getLoginUser().getUsername();
		Map<String, Object> result = new HashMap<String, Object>();
		// 加载工单对象
		BizInfo bizInfo = bizInfoService.get(id);
		if (bizInfo == null) {
			throw new ServiceException("找不到工单:" + id);
		}
		result.put("workInfo", bizInfo);
		BizInfoConf bizInfoConf = this.bizInfoConfService.getMyWork(id);
		String taskId = bizInfoConf == null ? null : bizInfoConf.getTaskId();
		// 加载工单详情字段
		result.put("processVariables", loadProcessValBean(bizInfo.getProcessDefinitionId()));

		// 处理扩展信息
		Map<String, Object> extInfo = new HashMap<String, Object>();
		extInfo.put("createUser", sysUserService.getUserByUsername(bizInfo.getCreateUser()));
		extInfo.put("base_taskID", taskId);
		result.put("extInfo", extInfo);

		// 子工单信息
		result.put("subBizInfo", bizInfoService.getBizByParentId(id));
		String curreOp = null;
		if (StringUtils.isNotEmpty(taskId)) {
			curreOp = processDefinitionService.getWorkAccessTask(taskId, loginUser);
		}
		result.put("CURRE_OP", curreOp);
		Task task = processDefinitionService.getTaskBean(taskId);
		if (task != null) {
			result.put("$currentTaskName", task.getName());
		}
		List<ProcessVariable> currentVariables = loadHandleProcessValBean(bizInfo, taskId);
		// 加载当前编辑的业务字段,只有当前操作为HANDLE的时候才加载
		if (Constants.HANDLE.equalsIgnoreCase(curreOp)) {
			result.put("currentVariables", currentVariables);
			extInfo.put("handleUser", sysUserService.getUserByUsername(loginUser));
			Map<String, String> buttons = processDefinitionService.findOutGoingTransNames(taskId, false);
			if (MapUtils.isEmpty(buttons)) {
				buttons = new HashMap<String, String>();
				buttons.put("submit", "提交");
			}
			result.put("SYS_BUTTON", buttons);
		} else if (Constants.SIGN.equalsIgnoreCase(curreOp)) {
			Map<String, String> buttons = new HashMap<String, String>(1);
			buttons.put(Constants.SIGN, "签收");
			result.put("SYS_BUTTON", buttons);
		}
		// 加载工单流程参数
		result.put("serviceInfo", instanceService.loadInstances(bizInfo));
		// 加载流程参数附件
		result.put("annexs", bizFileService.loadBizFilesByBizId(bizInfo.getId(), Constants.TASK_START));
		// 加载日志
		List<BizLog> bizLogs = logService.loadBizLogs(bizInfo.getId());
		Map<String, List<ProcessVariableInstance>> logVars = new HashMap<String, List<ProcessVariableInstance>>(0);
		Map<String, Object> fileMap = new HashMap<String, Object>();
		if (CollectionUtils.isNotEmpty(bizLogs)) {
			for (BizLog bizLog : bizLogs) {
				fileMap.put(bizLog.getId(), bizFileService.loadBizFilesByBizId(id, bizLog.getTaskID()));
				logVars.put(bizLog.getId(), instanceService.loadValueByLog(bizLog));
			}
		}
		result.put("files", fileMap);
		result.put("workLogs", bizLogs);
		result.put("logVars", logVars);
		return result;
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
			BizInfo bean = bizInfoService.get(id);
			if (bean == null) {
				throw new ServiceException("找不到工单");
			}
			result[0] = "IMAGE";
			result[1] = processDefinitionService.viewProcessImage(bean.getProcessInstanceId());
		} else {
			BizFile bean = bizFileService.getBizFileById(id);
			if (bean == null) {
				throw new ServiceException("找不到附件");
			}
			File file = UploadFileUtil.getUploadFile(bean);
			if (!file.exists()) {
				throw new ServiceException("找不到附件");
			}
			InputStream is = null;
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				logger.error("文件未找到 : {}",e);
			}
			result[0] = bean.getFileType();
			result[1] = is;
			result[2] = file.length();
			result[3] = bean.getName();
		}
		return result;
	}
}
