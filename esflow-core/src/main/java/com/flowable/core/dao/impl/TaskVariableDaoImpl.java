package com.flowable.core.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.common.utils.PageHelper;
import com.flowable.common.utils.ReflectionUtils;
import com.flowable.core.bean.TaskVariable;
import com.flowable.core.dao.ITaskVariableDao;

@Repository
public class TaskVariableDaoImpl extends BaseDaoImpl<TaskVariable> implements ITaskVariableDao {

	@Override
	public List<TaskVariable> loadTaskVariables(String processDefinitionId, int version, String... taskIdS) {

		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("FROM TaskVariable BEAN WHERE BEAN.processDefinitionId=? ");
		args.add(processDefinitionId);

		if (taskIdS != null && taskIdS.length > 0) {
			hql.append(" AND (");
			for (String id : taskIdS) {
				hql.append(" BEAN.taskId=? OR ");
				args.add(id);
			}
			hql = hql.delete(hql.length() - 4, hql.length());
			hql.append(")");
		}

		hql.append(" AND BEAN.version = ");
		if (version < 0) {
			args.add(processDefinitionId);
			hql.append("(SELECT max(version) FROM TaskVariable WHERE processDefinitionId=?)");
		} else {
			hql.append("?");
			args.add(version);
		}
		hql.append(" ORDER BY BEAN.groupOrder ASC,BEAN.order ASC");
		return this.find(hql.toString(), args.toArray());
	}

	@Override
	public PageHelper<TaskVariable> loadTaskVariables(String processDefinitionId, int version,
			PageHelper<TaskVariable> page, String taskId) {

		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("FROM TaskVariable BEAN WHERE BEAN.processDefinitionId=? ");
		args.add(processDefinitionId);

		hql.append(" and BEAN.taskId=? ");
		args.add(taskId);

		hql.append(" AND BEAN.version = ");
		if (version < 0) {
			args.add(processDefinitionId);
			hql.append("(SELECT max(version) FROM TaskVariable WHERE processDefinitionId=?)");
		} else {
			hql.append("?");
			args.add(version);
		}
		hql.append(" ORDER BY BEAN.groupOrder ASC,BEAN.order ASC");
		return this.find(page, hql.toString(), args.toArray());
	}

	@Override
	public int getProcessOrder(TaskVariable bean) {
		String sql = "SELECT MAX(NAME_ORDER) FROM RM.ACT_B_PROC_TASK_VAL WHERE "
				+ (bean.getGroupName() == null ? "1=1" : "GROUP_NAME=?");
		sql += " AND TEMP_ID='" + bean.getProcessDefinitionId() + "' AND TASK_ID='" + bean.getTaskId()
				+ "' AND VERSION=" + bean.getVersion();
		List<Object> list = this.findBySql(sql, new Object[] { bean.getGroupName() });
		if (list == null || list.size() <= 0 || list.get(0) == null) {
			return 0;
		}
		return (Integer) ReflectionUtils.convert(list.get(0), Integer.class);
	}

	public List<TaskVariable> getRefList(Map<String, Object> params) {
		StringBuffer hql = new StringBuffer(
				" from TaskVariable where processDefinitionId=? and version=? and refVariable is not null and refVariable<>''");
		List<TaskVariable> list = this.find(hql.toString(),
				new Object[] { params.get("processId"), params.get("version") });
		return list;
	}

	@Override
	public List<TaskVariable> findTaskVariable(TaskVariable taskVariable) {

		StringBuilder hql = new StringBuilder(" from TaskVariable t where 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if (StringUtils.isNotBlank(taskVariable.getProcessDefinitionId())) {
			hql.append(" and t.processDefinitionId = ? ");
			list.add(taskVariable.getProcessDefinitionId());
		}
		if (StringUtils.isNotBlank(taskVariable.getVariableGroup())) {
			hql.append(" and t.variableGroup like ? ");
			list.add("%" + taskVariable.getVariableGroup() + "%");
		}
		if (StringUtils.isNotBlank(taskVariable.getTaskId())) {
			hql.append(" and t.taskId = ? ");
			list.add(taskVariable.getTaskId());
		}
		return this.find(hql.toString(), list.toArray());
	}
}
