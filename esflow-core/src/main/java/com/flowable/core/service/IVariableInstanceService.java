package com.flowable.core.service;

import java.util.List;
import java.util.Map;

import com.flowable.core.bean.AbstractVariableInstance;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.BizInfoConf;
import com.flowable.core.bean.BizLog;

/**
 * 流程实例、流程任务实例数据处理
 */
public interface IVariableInstanceService {

	/**
	 * 添加
	 * 
	 * @param beans
	 * @
	 */
	public void addProcessInstance(AbstractVariableInstance... beans) ;

	public void updateProcessInstance(AbstractVariableInstance... beans) ;

	/**
	 * 加载某个工单所填写的所有数据
	 * 
	 * @param bean
	 * @return
	 * @
	 */
	public List<AbstractVariableInstance> loadInstances(BizInfo bean) ;

	public Map<String, AbstractVariableInstance> getVarMap(BizInfo bizInfo, BizInfoConf bizInfoConf, VariableLoadType type);

	/**
	 * 根据LOG记录加载对应的数据
	 * 
	 * @param logBean
	 * @return
	 * @
	 */
	public List<AbstractVariableInstance> loadValueByLog(BizLog logBean) ;
	

	enum VariableLoadType {
		ALL, UPDATABLE
	}

}
