package com.flowable.oa.core.service;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.util.LoginUser;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :  流程处理 与流程引擎交互
 * @since : 2020/6/21 22:20
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
    Map<String, Object> getActivityTask(BizInfo bean, LoginUser user);

    /**
     * 获取当前任务的前一个任务 KEY <br>
     *
     * @param taskId 当前任务ID
     * @return @
     */
    String getParentTask(String taskId);

    /**
     * 获取任务处理方式
     *
     * @param processDefinitionId
     * @return
     */
    Map<String, String> loadStartButtons(String processDefinitionId);

    /**
     * 获取某个任务的外出线名，用于动态生成提交按钮，逻辑如下<br>
     * 1. 如果任务只有一个出口，并且下个结点为网关，则去网关所有的出口名<br>
     * 2. 否则取任务所有出口名
     *
     * @param taskId
     * @return @
     */
    Map<String, String> findOutGoingTransNames(String taskId);

    /**
     * 显示流程实例图片
     *
     * @param processInstanceId
     * @return @
     */
    InputStream viewProcessImage(String processInstanceId);

    /**
     * 新增流程实例
     *
     * @param id        模板ID
     * @param variables 流程变量
     * @return @
     */
    ProcessInstance newProcessInstance(String id, Map<String, Object> variables);

    /**
     * 处理流程
     *
     * @param bean      工单对象ID
     * @param taskId    任务ID
     * @param variables 流程变量
     * @return @
     */
    boolean completeTask(BizInfo bean, String taskId, Map<String, Object> variables);

    /**
     * 签收任务
     *
     * @param taskId
     * @param username
     * @return @
     */
    boolean claimTask(String taskId, String username);

    /**
     * 转派任务
     *
     * @param taskId
     * @param toAssignment   需要转派的人或组
     * @param assignmentType 标记为人或组取值为：group|user
     * @return @
     */
    boolean assignmentTask(String taskId, String toAssignment, String assignmentType);

    /**
     * 获取下一步正在处理的任务信息,如果返回null标示流程已结束
     *
     * @param processInstanceId
     * @return [任务ID, 任务KEY, 任务名, 待签收人/角色] @
     */
    List<Task> getNextTaskInfo(String processInstanceId);

    /**
     * 获取任务信息
     *
     * @param taskId 任务ID
     * @return 任务对象 @
     */
    Task getTaskBean(String taskId);

    /**
     * 获取当前用户对工单有权限处理的任务，并返回操作权限 返回HANDLE，表示可以进行处理，SIGN表示可以进行签收，其他无权限<br>
     * 返回格式：任务ID:权限
     *
     * @param taskId   taskId
     * @param username 用户
     * @return @
     */
    String getWorkAccessTask(String taskId, String username);

    /**
     * 获取流程定义
     *
     * @param id 流程定义ID
     * @return
     */
    ProcessDefinition getProcDefById(String id);

    /**
     * 获取一个流程的最新定义
     *
     * @param key 流程定义Key
     * @return
     */
    ProcessDefinition getLatestProcDefByKey(String key);

    /**
     * 部署流程之后,根据上一版本的流程对象,拷贝上次的参数配置到最新的流程中
     *
     * @param processDefinition
     */
    void copyVariables(ProcessDefinition processDefinition);

    /**
     * 自动签收
     *
     * @param processInstanceId
     * @return
     */
    boolean autoClaim(String processInstanceId);

    /**
     * 任务代办组
     *
     * @param task
     * @return
     */
    List<String> getTaskCandidateGroup(Task task);

    /**
     * 获取流程实例
     *
     * @param processInstanceId
     * @return
     */
    ProcessInstance getProcessInstance(String processInstanceId);

    /**
     * 获取流程运行PATH,以逗号开头，使用逗号分隔，根据历史任务的结束时间倒序排序
     *
     * @param processInstanceId
     * @return @
     */
    String getProcessPath(String processInstanceId);
}