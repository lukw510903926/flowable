package com.flowable.oa.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.entity.BizInfoConf;
import com.flowable.oa.entity.ProcessVariable;
import org.flowable.task.api.Task;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

/**
 * 流程处理业务
 */
public interface IProcessExecuteService {

    static final String systemFormType = "_SYS_FORM_TYPE";

    /**
     * 获取某个日志对应的输入数据
     *
     * @param logId
     * @return
     * @
     */
    Map<String, Object> loadBizLogInput(String logId);

    /**
     * 获取某个流程的开始按钮
     *
     * @param tempId
     * @return
     * @
     */
    Map<String, String> loadStartButtons(String tempId);

    /**
     * 加载当前在运行的所有流程
     *
     * @return
     */
    Map<String, Object> loadProcessList();

    /**
     * 保存参数，如果是草稿，那么流程实例ID、任务ID皆留空，还不保存到流程参数；<br />
     * 如果是创单，流程实例ID非空，任务ID留空；<br />
     * 正常流转，流程实例ID、任务ID都非空。
     *
     * @param processValList
     * @param params
     * @param now
     */
    void saveOrUpdateVars(BizInfo bizInfo, String taskId, List<ProcessVariable> processValList, Map<String, Object> params, Date now);

    /**
     * 保存工单草稿
     *
     * @param params
     * @param startProc  同时启动流程
     * @return
     * @
     */
    BizInfo createBizDraft(Map<String, Object> params, MultiValueMap<String, MultipartFile> multiValueMap, boolean startProc);

    /**
     * 更新工单关联的任务信息（填充下一个（或初始）任务（环节）的信息）
     *
     * @param bizInfo
     */
    void updateBizTaskInfo(BizInfo bizInfo, BizInfoConf bizInfoConf);

    /**
     * 更新工单信息
     *
     * @param params
     * @param fileMap
     * @return
     * @
     */
    BizInfo updateBiz(Map<String, Object> params, MultiValueMap<String, MultipartFile> fileMap);

    /**
     * 处理工单，新增跟审批
     *
     * @param params
     * @param multiValueMap
     * @return
     * @
     */
    BizInfo submit(Map<String, Object> params, MultiValueMap<String, MultipartFile> multiValueMap);

    /**
     * 记录流程操作日志
     *
     * @param bizInfo
     * @param task
     * @param now
     * @param params
     */
    void writeBizLog(BizInfo bizInfo, Task task, Date now, Map<String, Object> params);

    /**
     * 加载工单任务参数
     *
     * @param bean
     * @param taskDefKey
     * @return
     */
    List<ProcessVariable> loadProcessVariables(BizInfo bean, String taskDefKey);

    /**
     * 根据工单号查询工单信息，并且处理工单的处理权限
     *
     * @return
     * @
     */
    Map<String, Object> queryWorkOrder(String id);

    /**
     * 下载或查看文件
     *
     * @param action
     * @param id
     * @return [文件类型, InputStream]
     * @
     */
    Object[] downloadFile(String action, String id);

}
