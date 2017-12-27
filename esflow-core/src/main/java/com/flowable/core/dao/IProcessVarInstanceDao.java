package com.flowable.core.dao;

import java.util.List;
import java.util.Map;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.BizLog;
import com.flowable.core.bean.ProcessVariableInstance;

/**
 * 流程实例DAO
 */
public interface IProcessVarInstanceDao extends IBaseDao<ProcessVariableInstance> {

    List<ProcessVariableInstance> loadProcessInstances(String processInstanceId);

    List<ProcessVariableInstance> loadProcessInstancesByBizId(String bizId);

    List<ProcessVariableInstance> findProcessInstances(ProcessVariableInstance instance);

    List<ProcessVariableInstance> getProcessVariableInstances(Map<String, String> params);

    void deleteByVarId(String variableId);

    List<ProcessVariableInstance> loadValueByLog(BizLog logBean);

    List<ProcessVariableInstance> findByProcInstId(String processInstanceId);

}
