package com.flowable.core.service;

import java.util.List;
import java.util.Map;

import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.AbstractVariable;
import com.flowable.core.bean.ProcessVariable;
import com.flowable.core.bean.ProcessVariableInstance;
import com.flowable.core.bean.TaskVariable;

/**
 * 流程属性模板业务，包括模板属性以及任务属性处理
 */
public interface IProcessVariableService {

	/**
	 * 添加
	 * @param beans
	 * @
	 */
	void addVariable(AbstractVariable... beans) ;

	/**
	 * 修改
	 * @param beans
	 * @
	 */
	void updateVariable(AbstractVariable... beans) ;

	/**
	 * 
	 * @param bean
	 * @return
	 * @
	 */
	int getProcessOrder(AbstractVariable bean) ;

	/**
	 * 删除
	 * @param beans
	 * @
	 */
	void deleteVariable(AbstractVariable... beans) ;

	AbstractVariable getVariableById(String id) ;

	AbstractVariable getVariable(String id, Class<? extends AbstractVariable> type) ;

	/**
	 * 根据流程模板ID，获取模板的公共属性列表
	 * @param processDefinitionId
	 * @param version
	 * @return
	 * @
	 */
	List<ProcessVariable> loadVariables(String processDefinitionId, int version) ;

	/**
	 * 根据流程模板ID，任务ID加载某个模板的指定流程任务ID，如果任务ID为空，则加载所有的任务ID对应的属性
	 * @param version
	 * @param taskIds
	 * @return
	 * @
	 */
	List<TaskVariable> loadTaskVariables(String processDefinitionId, int version, String... taskIds) ;
	
	/**
	 * 获取流程参数
	 * @param params
	 * @return
	 */
	List<ProcessVariableInstance> getProcessVariableInstances(Map<String, String> params);


	PageHelper<TaskVariable> loadTaskVariables(String processDefinitionId, int version,PageHelper<TaskVariable> page, String taskIds) ;

	PageHelper<ProcessVariable> loadProcessVariables(String processDefinitionId, int version,PageHelper<ProcessVariable> page);
	
}