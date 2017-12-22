package com.flowable.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.AbstractVariable;
import com.flowable.core.bean.ProcessVariable;
import com.flowable.core.bean.ProcessVariableInstance;
import com.flowable.core.bean.TaskVariable;
import com.flowable.core.dao.IProcessVarInstanceDao;
import com.flowable.core.dao.IProcessVariableDao;
import com.flowable.core.dao.ITaskVarInstanceDao;
import com.flowable.core.dao.ITaskVariableDao;
import com.flowable.core.service.IProcessVariableService;

@Service
@Transactional(readOnly = true)
public class ProcessVariableServiceImpl implements IProcessVariableService {

	@Autowired
	private IProcessVariableDao processVariableDao;
	
	@Autowired
	private IProcessVarInstanceDao processVarInstanceDao;

	@Autowired
	private ITaskVariableDao taskVariableDao;
	
	@Autowired
	private ITaskVarInstanceDao taskVarInstanceDao;

	public int getProcessOrder(AbstractVariable bean)  {
		if (bean == null || bean.getProcessDefinitionId() == null) {
			return 0;
		}
		if (bean instanceof TaskVariable) {
			TaskVariable bean2 = (TaskVariable) bean;
			if (bean2.getTaskId() == null) {
				return 0;
			}
			return taskVariableDao.getProcessOrder((TaskVariable) bean);
		} else {
			return processVariableDao.getProcessOrder((ProcessVariable) bean);
		}
	}

	@Override
	@Transactional
	public void addVariable(AbstractVariable... beans)  {
		for (AbstractVariable bean : beans) {
			if (bean instanceof TaskVariable) {
				taskVariableDao.save((TaskVariable) bean);
				continue;
			}
			processVariableDao.save((ProcessVariable) bean);
		}
	}

	@Override
	@Transactional
	public void updateVariable(AbstractVariable... beans)  {
		for (AbstractVariable bean : beans) {
			if (bean.getId() == null) {

				continue;
			}
			if (bean instanceof TaskVariable) {
				taskVariableDao.update((TaskVariable) bean);
				continue;
			}
			processVariableDao.update((ProcessVariable) bean);
		}
	}

	@Override
	@Transactional
	public void deleteVariable(AbstractVariable... beans)  {
		
		Map<String,String> params = new HashMap<String, String>();
		for (AbstractVariable bean : beans) {
			params.put("variableId", bean.getId());
			if (bean.getId() == null) {
				continue;
			}
			if (bean instanceof TaskVariable) {
				this.taskVarInstanceDao.deleteByVarId(bean.getId());
				taskVariableDao.delete((TaskVariable) bean);
			}else{
				this.processVarInstanceDao.deleteByVarId(bean.getId());
				this.processVariableDao.deleteById(bean.getId());
			}
		}
	}

	public AbstractVariable getVariableById(String id)  {
		AbstractVariable bean = getVariable(id, ProcessVariable.class);
		if (bean == null) {
			bean = getVariable(id, TaskVariable.class);
		}
		return bean;
	}

	public AbstractVariable getVariable(String id, Class<? extends AbstractVariable> type)  {
		if (type == null) {
			return null;
		}
		if (type == ProcessVariable.class) {
			return processVariableDao.getById(id);
		} else if (type == TaskVariable.class) {
			return taskVariableDao.getById(id);
		}
		return null;
	}

	/**
	 * 根据流程模板ID，获取模板的公共属性列表<br>
	 * 版本号，如果为空则去最新的版本
	 * @return
	 * @
	 */
	public List<ProcessVariable> loadVariables(String processDefinitionId, int version)  {
			
		return processVariableDao.loadProcessVariables(processDefinitionId, version);
	}
	
	@Override
	public PageHelper<ProcessVariable> loadProcessVariables(String processDefinitionId, int version,PageHelper<ProcessVariable> page){
		
		return processVariableDao.loadProcessVariables(processDefinitionId, version,page);
	}

	/**
	 * 根据流程模板ID，任务ID加载某个模板的指定流程任务ID，如果任务ID为空，则加载所有的任务ID对应的属性<br>
	 * 版本号，如果为空则去最新的版本<br>
	 * 此处模板必须对应模板的版本ID
	 * @return
	 * @
	 */
	public List<TaskVariable> loadTaskVariables(String processDefinitionId, int version, String... taskIds)  {
			
		return taskVariableDao.loadTaskVariables(processDefinitionId, version, taskIds);
	}
	
	@Override
	public PageHelper<TaskVariable> loadTaskVariables(String processDefinitionId, int version,PageHelper<TaskVariable> page, String taskIds){

		return taskVariableDao.loadTaskVariables(processDefinitionId, version,page, taskIds);
	}

	@Override
	public List<ProcessVariableInstance> getProcessVariableInstances(Map<String, String> params) {
		
		return this.processVarInstanceDao.getProcessVariableInstances(params);
	}
	
	@Override
	public List<Map<String,String>> getBizInfo(String bizId){
		
		return this.processVarInstanceDao.getBizInfo(bizId);
	}

}
