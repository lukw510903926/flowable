package com.flowable.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.flowable.task.api.Task;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.AbstractVariable;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.BizInfoConf;

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
	Map<String, Object> loadBizLogInput(String logId) ;

	/**
	 * 获取某个流程的开始按钮
	 * 
	 * @param tempId
	 * @return
	 * @
	 */
	Map<String, String> loadStartButtons(String tempId) ;

	/**
	 * 加载当前在运行的所有流程
	 * 
	 * @return
	 */
	Map<String, Object> loadProcessList() ;

	/**
	 * 获取我的工单，包括：我创建的工单(1),我处理的工单(2X),我的待办任务(3),我的代签任务(4)
	 * 
	 * @param targe
	 * @return
	 * @
	 */
	PageHelper<BizInfo> queryMyBizInfos(String targe, Map<String, Object> params, PageHelper<BizInfo> page);
	/**
	 * 获取当前需要填写的属性列表<br>
	 * 如果没有工单号则获取模板的公共属性<br>
	 * 如果有工单号，则获取到工单当前需要处理的流程，然后再加载属性
	 * 
	 * @return
	 * @
	 */
	List<AbstractVariable> loadHandleProcessVariables(String processDefinitionId) ;
	
	/**
	 * 保存参数，如果是草稿，那么流程实例ID、任务ID皆留空，还不保存到流程参数；<br />
	 * 如果是创单，流程实例ID非空，任务ID留空；<br />
	 * 正常流转，流程实例ID、任务ID都非空。
	 * 
	 * @param processValList
	 * @param params
	 * @param now
	 */
	void saveOrUpdateVars(BizInfo bizInfo,BizInfoConf bizInfoConf, List<AbstractVariable> processValList, Map<String, Object> params,Date now);

	/**
	 * 保存工单草稿
	 * 
	 * @param params
	 * @param startProc 同时启动流程
	 * @param deleFileId 
	 * @return
	 * @
	 */
	BizInfo createBizDraft(Map<String, Object> params, MultiValueMap<String, MultipartFile> multiValueMap, boolean startProc, String[] deleFileId) ;
	
	/**
	 * 更新工单关联的任务信息（填充下一个（或初始）任务（环节）的信息）
	 * 
	 * @param bizInfo
	 */
	void updateBizTaskInfo(BizInfo bizInfo, BizInfoConf bizInfoConf);

	/**
	 * 更新工单信息
	 * 
	 * @param id
	 * @param params
	 * @param startProc 
	 * @return
	 * @
	 */
	BizInfo updateBiz(String id, Map<String, Object> params, MultiValueMap<String, MultipartFile> fileMap,boolean startProc);
	/**
	 * 处理工单，新增跟审批
	 * 
	 * @param params
	 * @param multiValueMap
	 * @return
	 * @
	 */
	BizInfo submit(Map<String, Object> params, MultiValueMap<String, MultipartFile> multiValueMap) ;
	
	/**
	 * 记录流程操作日志
	 * @param bizInfo
	 * @param task
	 * @param now
	 * @param params
	 */
	void writeBizLog(BizInfo bizInfo, Task task, Date now, Map<String, Object> params);
	
	List<AbstractVariable> loadHandleProcessValBean(BizInfo bean, String taskId) ;

	/**
	 * 根据工单号查询工单信息，并且处理工单的处理权限
	 * @return
	 * @
	 */
	Map<String, Object> queryWorkOrder(String id) ;

	BizInfo getBizInfo(String id) ;

	/**
	 * 下载或查看文件
	 * 
	 * @param action
	 * @param id
	 * @return [文件类型,InputStream]
	 * @
	 */
	Object[] downloadFile(String action, String id) ;

	/**
	 * 更新工单信息
	 * 
	 * @param params
	 * @return
	 * @
	 */
	BizInfo update(Map<String, Object> params) ;

}
