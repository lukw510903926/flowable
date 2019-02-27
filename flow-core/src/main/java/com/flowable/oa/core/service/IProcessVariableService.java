package com.flowable.oa.core.service;

import java.util.List;

import com.flowable.oa.core.entity.ProcessVariable;
import com.flowable.oa.core.util.mybatis.IBaseService;
import com.github.pagehelper.PageInfo;
import org.flowable.engine.repository.ProcessDefinition;

/**
 * 流程属性模板业务，包括模板属性以及任务属性处理
 */
public interface IProcessVariableService extends IBaseService<ProcessVariable> {

	/**
	 * 删除
	 * 
	 * @param list
	 * @
	 */
	void deleteVariable(List<String> list);

	PageInfo<ProcessVariable> findProcessVariables(ProcessVariable variable, PageInfo<ProcessVariable> page);

	/**
	 * 参数复制
	 * 
	 * @param oldPdf
	 * @param newPdf
	 */
	void copyVariables(ProcessDefinition oldPdf, ProcessDefinition newPdf);
}