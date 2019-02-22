package com.flowable.oa.service;

import java.util.List;
import java.util.Map;

import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.entity.BizLog;
import com.flowable.oa.entity.ProcessVariableInstance;
import com.flowable.oa.util.mybatis.IBaseService;

/**
 * 流程实例、流程任务实例数据处理
 */
public interface IVariableInstanceService extends IBaseService<ProcessVariableInstance> {

	/**
	 * 添加
	 * 
	 * @param beans
	 * @
	 */
	void addProcessInstance(ProcessVariableInstance... beans) ;

	void updateProcessInstance(ProcessVariableInstance... beans) ;

	/**
	 * 加载某个工单所填写的所有数据
	 * 
	 * @param bean
	 * @return
	 * @
	 */
	List<ProcessVariableInstance> loadInstances(BizInfo bean) ;

	Map<String, ProcessVariableInstance> getVarMap(BizInfo bizInfo, String taskId, VariableLoadType type);

	/**
	 * 根据LOG记录加载对应的数据
	 * 
	 * @param logBean
	 * @return
	 * @
	 */
	List<ProcessVariableInstance> loadValueByLog(BizLog logBean) ;
	
	enum VariableLoadType {
		ALL, UPDATABLE
	}
}
