package com.flowable.core.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.core.bean.AbstractVariableInstance;
import com.flowable.core.bean.BizLog;
import com.flowable.core.bean.TaskVariableInstance;
import com.flowable.core.dao.ITaskVarInstanceDao;

@Repository
public class TaskVarInstanceDaoImpl extends BaseDaoImpl<TaskVariableInstance> implements ITaskVarInstanceDao {

	public List<TaskVariableInstance> findByProcInstId(String processInstanceId)  {
		
		String hql = " from TaskVariableInstance where processInstanceId = ? order by createTime asc";
		return this.find(hql, new Object[] {processInstanceId});
	}

	@Override
	public List<AbstractVariableInstance> loadValueByLog(BizLog logBean)  {
		
		StringBuffer hql = new StringBuffer("FROM TaskVariableInstance bean left join fetch bean.variable ");
		hql.append("  v WHERE bean.processInstanceId=? AND bean.taskId=?  order by v.order asc");
		return this.find(hql.toString(),new Object[] { logBean.getBizInfo().getProcessInstanceId(), logBean.getTaskID() });
	}

	@Override
	public List<TaskVariableInstance> findByTaskId(String taskId)  {
		
		String hql = " from TaskVariableInstance where taskId = ? order by createTime asc";
		return this.find(hql, new Object[] {taskId});
	}
	
	@Override
	public void deleteByVarId(String variableId){
		
		if(StringUtils.isNotBlank(variableId)){
			String hql = " delete from TaskVariableInstance p where p.variable.id = ? ";
			this.execute(hql,new Object[]{variableId});
		}
	}
}
