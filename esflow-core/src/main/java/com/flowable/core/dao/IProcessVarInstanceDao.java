package com.flowable.core.dao;

import java.util.List;
import java.util.Map;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.ProcessVariableInstance;

/**
 * 流程实例DAO
 */
public interface IProcessVarInstanceDao extends IBaseDao<ProcessVariableInstance> {

    List<ProcessVariableInstance> loadProcessInstances(String processInstanceId);

    List<ProcessVariableInstance> loadProcessInstancesByBizId(String bizId);

    /**
     * 获取工单配置信息
     *
     * @param bizId
     * @return
     */
    List<Map<String, String>> getBizInfo(String bizId);

    List<ProcessVariableInstance> getProcessVariableInstances(Map<String, String> params);

    void deleteByVarId(String variableId);
}
