package com.flowable.core.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public List<TaskVariableInstance> findTaskVariableInstance(Map<String,String> params){
		
		StringBuilder hql = new StringBuilder(" FROM TaskVariableInstance bean where 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if(params != null){
			if(StringUtils.isNotBlank(params.get("processInstanceId"))){
				hql.append(" and bean.processInstanceId = ? ");
				list.add(params.get("processInstanceId"));
			}
			if(StringUtils.isNotBlank(params.get("taskId"))){
				hql.append(" and bean.taskId = ? ");
				list.add(params.get("taskId"));
			}
			if(StringUtils.isNotBlank(params.get("name"))){
				hql.append(" and bean.variable.name = ? ");
				list.add(params.get("name"));
			}
			if(StringUtils.isNotBlank(params.get("variableId"))){
				hql.append(" and bean.variable.id = ? ");
				list.add(params.get("variableId"));
			}
		}
		return this.find(hql.toString(),list.toArray());
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
	
	@Override
	public TaskVariableInstance getTaskVarInstanceByVarName(Map<String,String> params){
		
		StringBuilder hql = new StringBuilder("  from TaskVariableInstance a join fetch a.variable b where b.name = ? ");
		hql.append(" and b.taskId = ? and a.processInstanceId = ? and a.createTime = ");
		hql.append(" (select max(b.createTime) from TaskVariableInstance b join b.variable c where b.processInstanceId = ? and c.taskId = ?)");
		List<TaskVariableInstance> list = this.find(hql.toString(),new Object[]{params.get("name"),params.get("taskId"),params.get("instanceId"),params.get("instanceId"),params.get("taskId")});
		if(list == null || list.isEmpty())
			return null;
		return list.get(0);
	}
}
