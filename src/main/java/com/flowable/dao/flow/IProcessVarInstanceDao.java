package com.flowable.dao.flow;

import java.util.List;
import java.util.Map;

import com.flowable.entity.BizLog;
import com.flowable.entity.ProcessVariableInstance;
import com.flowable.util.dao.IBaseDao;

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
