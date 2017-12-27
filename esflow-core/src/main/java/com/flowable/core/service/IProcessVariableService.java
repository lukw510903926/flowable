package com.flowable.core.service;

import java.util.List;
import java.util.Map;

import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.ProcessVariable;
import com.flowable.core.bean.ProcessVariableInstance;

/**
 * 流程属性模板业务，包括模板属性以及任务属性处理
 */
public interface IProcessVariableService {

	/**
	 * 添加
	 * @param beans
	 * @
	 */
	void addVariable(ProcessVariable... beans) ;

	/**
	 * 修改
	 * @param beans
	 * @
	 */
	void updateVariable(ProcessVariable... beans) ;

	/**
	 * 
	 * @param bean
	 * @return
	 * @
	 */
	int getProcessOrder(ProcessVariable bean) ;

	/**
	 * 删除
	 * @param beans
	 * @
	 */
	void deleteVariable(ProcessVariable... beans) ;

	ProcessVariable getVariableById(String id) ;

	/**
	 * 获取流程参数
	 * @param params
	 * @return
	 */
	List<ProcessVariableInstance> getProcessVariableInstances(Map<String, String> params);

	PageHelper<ProcessVariable> findProcessVariables(ProcessVariable variable, PageHelper<ProcessVariable> page);

	List<ProcessVariable> findProcessVariables(ProcessVariable variable);
}