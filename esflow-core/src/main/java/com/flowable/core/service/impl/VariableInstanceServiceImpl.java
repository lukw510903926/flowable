package com.flowable.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.core.bean.AbstractVariableInstance;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.BizInfoConf;
import com.flowable.core.bean.BizLog;
import com.flowable.core.bean.ProcessVariableInstance;
import com.flowable.core.bean.TaskVariableInstance;
import com.flowable.core.dao.IProcessVarInstanceDao;
import com.flowable.core.dao.ITaskVarInstanceDao;
import com.flowable.core.service.IVariableInstanceService;

@Service
@Transactional(readOnly = true)
public class VariableInstanceServiceImpl implements IVariableInstanceService {

	@Autowired
	private IProcessVarInstanceDao processInstanceDao;

	@Autowired
	private ITaskVarInstanceDao taskInstanceDao;
	
	@Override
	@Transactional
	public void addProcessInstance(AbstractVariableInstance... beans)  {
		for (AbstractVariableInstance bean : beans) {
			if (bean instanceof ProcessVariableInstance)
				processInstanceDao.save((ProcessVariableInstance) bean);
			else {
				taskInstanceDao.save((TaskVariableInstance) bean);
			}
		}
	}
	
	@Override
	public void updateProcessInstance(AbstractVariableInstance... beans)  {
		for (AbstractVariableInstance bean : beans) {
			if (bean.getId() == null)
				continue;
			if (bean instanceof ProcessVariableInstance)
				processInstanceDao.update((ProcessVariableInstance) bean);
			else {
				taskInstanceDao.update((TaskVariableInstance) bean);
			}
		}
	}

	/**
	 * 加载某个工单所填写的所有数据
	 * 
	 * @param bean
	 * @return
	 * @
	 */
	@Override
	public List<AbstractVariableInstance> loadInstances(BizInfo bean)  {
		
		List<AbstractVariableInstance> result = new ArrayList<AbstractVariableInstance>();
		List<ProcessVariableInstance> temp1 = null;
		temp1 = processInstanceDao.loadProcessInstancesByBizId(bean.getId());
		if (temp1 != null)
			result.addAll(temp1);
		return result;
	}

	@Override
	public Map<String, AbstractVariableInstance> getVarMap(BizInfo bizInfo,BizInfoConf bizInfoConf, VariableLoadType type) {
		
		Map<String, AbstractVariableInstance> map = new HashMap<String, AbstractVariableInstance>();
		List<ProcessVariableInstance> pList = processInstanceDao.loadProcessInstancesByBizId(bizInfo.getId());
		List<TaskVariableInstance> tList = null;
		switch (type) {
		case ALL:
			tList = taskInstanceDao.findByProcInstId(bizInfo.getProcessInstanceId());
			break;
		case UPDATABLE:
			tList = taskInstanceDao.findByTaskId(bizInfoConf.getTaskId());
			break;
		}
		if (null != pList)
			for (ProcessVariableInstance var : pList) {
				map.put(var.getVariable().getName(), var);
			}
		if (null != tList)
			for (TaskVariableInstance var : tList) {
				map.put(var.getVariable().getName()	+ (type == VariableLoadType.ALL ? ('-' + bizInfoConf.getTaskId()) : ""), var);
			}
		return map;
	}

	@Override
	public List<AbstractVariableInstance> loadValueByLog(BizLog logBean)  {
		return taskInstanceDao.loadValueByLog(logBean);
	}

	@Override
	public List<TaskVariableInstance> findTaskVariableInstance(Map<String, String> params) {
		
		return this.taskInstanceDao.findTaskVariableInstance(params);
	}
	
	@Override
	public TaskVariableInstance getTaskVarInstanceByVarName(Map<String,String> params){
		
		return this.taskInstanceDao.getTaskVarInstanceByVarName(params);
	}
}
