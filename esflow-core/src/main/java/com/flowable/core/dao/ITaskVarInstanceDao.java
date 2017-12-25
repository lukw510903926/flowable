package com.flowable.core.dao;

import java.util.List;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.AbstractVariableInstance;
import com.flowable.core.bean.BizLog;
import com.flowable.core.bean.TaskVariableInstance;

/**
 * 流程任务实例DAO
 */
public interface ITaskVarInstanceDao extends IBaseDao<TaskVariableInstance> {

    List<TaskVariableInstance> findByProcInstId(String processInstanceId);

    List<AbstractVariableInstance> loadValueByLog(BizLog logBean);

    List<TaskVariableInstance> findByTaskId(String taskId);

    /**
     * @param variableId
     */
    void deleteByVarId(String variableId);
}
