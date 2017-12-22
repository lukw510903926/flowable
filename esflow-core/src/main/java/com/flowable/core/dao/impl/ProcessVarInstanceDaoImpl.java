package com.flowable.core.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.core.bean.ProcessVariableInstance;
import com.flowable.core.dao.IProcessVarInstanceDao;

@Repository
public class ProcessVarInstanceDaoImpl extends BaseDaoImpl<ProcessVariableInstance> implements IProcessVarInstanceDao {

	@Override
	public List<ProcessVariableInstance> loadProcessInstances(String processInstanceId)  {
		
		if(StringUtils.isNotBlank(processInstanceId)) {
			String hql = " from ProcessVariableInstance where  processInstanceId= ? ";
			return this.find(hql,new Object[] {processInstanceId});
		}
		return null;
	}
	
	@Override
	public List<ProcessVariableInstance> loadProcessInstancesByBizId(String bizId) {
		
		if(StringUtils.isNotBlank(bizId)) {
			String hql = " from ProcessVariableInstance where  bizId= ? ";
			return this.find(hql,new Object[] {bizId});
		}
		return new ArrayList<ProcessVariableInstance>();
	}
	

	@Override
	public List<Map<String,String>> getBizInfo(String bizId){
		
		StringBuilder hql = new StringBuilder(" select new map(a.value as value,a.variable.alias as alias");
		hql.append(",a.variable.name as name) from ProcessVariableInstance a  where a.bizId = ? ");
		return this.find(hql.toString(),new Object[]{bizId});
	}
	
	@Override
	public List<ProcessVariableInstance> getProcessVariableInstances(Map<String,String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" from ProcessVariableInstance p join fetch p.variable b where 1=1 ");
		if(StringUtils.isNotBlank(params.get("instanceId"))){
			hql.append(" and p.processInstanceId = ? ");
			list.add(params.get("instanceId"));
		}
		if(StringUtils.isNotBlank(params.get("name"))){
			hql.append(" and b.name = ? ");
			list.add(params.get("name"));
		}
		if(StringUtils.isNotBlank(params.get("variableId"))){
			hql.append(" and b.id = ? ");
			list.add(params.get("variableId"));
		}
		if(StringUtils.isNotBlank(params.get("bizId"))){
			hql.append(" and p.bizId = ? ");
			list.add(params.get("bizId"));
		}
		return this.find(hql.toString(),list.toArray());
	}
	
	@Override
	public void deleteByVarId(String variableId){
		
		if(StringUtils.isNotBlank(variableId)){
			String hql = " delete from ProcessVariableInstance p where p.variable.id = ? ";
			this.execute(hql,new Object[]{variableId});
		}
	}
}
