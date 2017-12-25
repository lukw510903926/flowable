package com.flowable.core.dao;

import java.util.List;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.AbstractVariableInstance;
import com.flowable.core.bean.BizLog;
import com.flowable.core.bean.TaskVariableInstance;

/**
 * 流程任务实例DAO
 */
public interface ITaskVarInstanceDao extends IBaseDao<TaskVariableInstance> {

	public List<TaskVariableInstance> findByProcInstId(String processInstanceId) ;

	public List<AbstractVariableInstance> loadValueByLog(BizLog logBean) ;

	public List<TaskVariableInstance> findByTaskId(String taskId) ;

	/**
	 * 
	 * @param variableId
	 */
	public void deleteByVarId(String variableId);
}
