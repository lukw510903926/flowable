package com.flowable.core.dao;

import java.util.List;
import java.util.Map;

import com.flowable.common.dao.IBaseDao;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.TaskVariable;

/**
 * 流程任务模板DAO
 */
public interface ITaskVariableDao extends IBaseDao<TaskVariable> {

    List<TaskVariable> loadTaskVariables(String processDefinitionId, int version, String... taskIdS);

    int getProcessOrder(TaskVariable bean);

    List<TaskVariable> getRefList(Map<String, Object> params);

    List<TaskVariable> findTaskVariable(TaskVariable taskVariable);

    /**
     * 参数分页查询
     *
     * @param processDefinitionId
     * @param version
     * @param taskIdS
     * @param page
     * @return
     */
    PageHelper<TaskVariable> loadTaskVariables(String processDefinitionId, int version, PageHelper<TaskVariable> page, String taskIdS);
}
