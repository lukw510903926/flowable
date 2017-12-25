package com.flowable.core.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import com.flowable.common.utils.LoginUser;
import com.flowable.core.bean.BizInfo;

/**
 * 流程处理<br>
 * 与流程引擎交互
 */
public interface IProcessDefinitionService {

	/**
	 * 获取当前用户对当前工单的活动任务信息<br>
	 * taskID:任务ID<br>
	 * curreOp:当前操作
	 * 
	 * @param bean
	 * @param user
	 * @return @
	 */
	public Map<String, Object> getActivityTask(BizInfo bean, LoginUser user);

	/**
	 * 获取当前任务的前一个任务 KEY <br>
	 * 当前任务ID
	 * 
	 * @return @
	 */
	public String getParentTask(String taskId);

	public Map<String, String> loadStartButtons(String processDefinitionId);

	/**
	 * 获取某个任务的外出线名，用于动态生成提交按钮，逻辑如下<br>
	 * 1. 如果任务只有一个出口，并且下个结点为网关，则去网关所有的出口名<br>
	 * 2. 否则取任务所有出口名
	 * 
	 * @param taskID
	 * @return @
	 */
	public Map<String, String> findOutGoingTransNames(String taskID, boolean def);

	/**
	 * 显示流程实例图片
	 * 
	 * @return @
	 */
	public InputStream viewProcessImage(BizInfo bean);

	/**
	 * 新增流程实例
	 * 
	 * @param id
	 *            模板ID
	 * @param variables
	 *            流程变量
	 * @return @
	 */
	public ProcessInstance newProcessInstance(String id, Map<String, Object> variables);

	/**
	 * 处理流程
	 * 
	 * @param bean
	 *            工单对象ID
	 * @param taskID
	 *            任务ID
	 * @param variables
	 *            流程变量
	 * @return @
	 */
	public boolean completeTask(BizInfo bean, String taskID, LoginUser user, Map<String, Object> variables);

	/**
	 * 签收任务
	 * 
	 * @param bean
	 * @param taskID
	 * @return @
	 */
	public boolean claimTask(BizInfo bean, String taskID, String username);

	/**
	 * 转派任务
	 * 
	 * @param bean
	 * @param taskID
	 * @param user
	 * @param toAssignment
	 *            需要转派的人或组
	 * @param assignmentType
	 *            标记为人或组取值为：group|user
	 * @return @
	 */
	public boolean assignmentTask(BizInfo bean, String taskID, LoginUser user, String toAssignment,
			String assignmentType);

	/**
	 * 获取下一步正在处理的任务信息,如果返回null标示流程已结束
	 * 
	 * @param bean
	 * @return [任务ID,任务KEY,任务名,待签收人/角色] @
	 */
	List<Task> getNextTaskInfo(String processInstanceId);

	/**
	 * 获取任务信息
	 * 
	 * @param taskID
	 *            任务ID
	 * @return 任务对象 @
	 */
	public Task getTaskBean(String taskID);

	/**
	 * 获取当前用户对工单有权限处理的任务，并返回操作权限 返回HANDLE，表示可以进行处理，SIGN表示可以进行签收，其他无权限<br>
	 * 返回格式：任务ID:权限
	 * 
	 * @param bean
	 *            工单对象
	 * @return @
	 */
	public String getWorkAccessTask(BizInfo bean, String username);

	/**
	 * 判断用户对当前任务的权限，返回权限值<br>
	 * 返回HANDLE，表示可以进行处理，SIGN表示可以进行签收，其他无权限<br>
	 * 
	 * @param taskID
	 * @return @
	 */
	public String getWorkAccessTask(String taskID, String username);

	/**
	 * 根据工单对象获取到当前工单所运行的模板版本
	 * 
	 * @param bean
	 * @return
	 */
	public int getWorkOrderVersion(BizInfo bean);

	/**
	 * 获取到工单所处理过的历史环节（任务ID）
	 * 
	 * @return
	 */
	public String[] getWorkOrderHistoryTask(String processInstanceId);

	public Map<String, Object> loadProcessList();

	/**
	 * @param id
	 *            流程定义ID
	 * @return
	 */
	public ProcessDefinition getProcDefById(String id);

	/**
	 * 获取一个流程的最新定义
	 * 
	 * @param key
	 *            流程定义Key
	 * @return
	 */
	public ProcessDefinition getLatestProcDefByKey(String key);

	/**
	 * 部署流程之后,根据上一版本的流程对象,拷贝上次的参数配置到最新的流程中
	 * 
	 * @param processDefinition
	 * @throws Exception
	 */
	public boolean copyVariables(ProcessDefinition processDefinition) throws Exception;

	/**
	 * 
	 * @param nextTaskId
	 * @return @ 备注: 分派子单 返回目标任务节点的上个节点的处理
	 */
	public String[] getNextTaskInfo(String nextTaskId, Map<String, Object> params);

	public boolean autoClaim(String processInstanceID);

	/**
	 * 任务拦截
	 * 
	 * @param taskId
	 */
	public void interceptTask(String taskId);

	/**
	 * 任务代办组
	 * 
	 * @param task
	 * @return
	 */
	public List<String> getTaskCandidateGroup(Task task);

	ProcessInstance getProceInstance(String processInstanceID);

}