package com.flowable.oa.core.service;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.BizLog;
import com.flowable.oa.core.entity.ProcessVariableInstance;
import com.flowable.oa.core.util.mybatis.IBaseService;

import java.util.List;
import java.util.Map;


/**
 * 流程实例、流程任务实例数据处理
 */
public interface IVariableInstanceService extends IBaseService<ProcessVariableInstance> {

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
