package com.flowable.core.dao;

import java.util.List;

import org.flowable.engine.repository.ProcessDefinition;

public interface IProcessModelDao {

	public List<String> getTaskCandidateGroup(String taskId);

	/**
	 * 部署流程之后,根据最新的流程ID,拷贝上次的参数配置
	 * @param oldPdf
	 * @param newPdf
	 * @throws Exception
	 */
	public void copyVariables(ProcessDefinition oldPdf, ProcessDefinition newPdf) throws Exception;

}
