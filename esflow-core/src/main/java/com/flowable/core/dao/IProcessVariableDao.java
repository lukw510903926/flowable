package com.flowable.core.dao;

import java.util.List;
import java.util.Map;

import com.flowable.common.dao.IBaseDao;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.ProcessVariable;

/**
 * 流程全局配置DAO
 */
public interface IProcessVariableDao extends IBaseDao<ProcessVariable> {

    List<ProcessVariable> loadProcessVariables(String processDefinitionId, int version);

    List<ProcessVariable> getRefList(Map<String, Object> params);

    void deleteById(String id);

    /**
     * 流程参数分页
     *
     * @param processDefinitionId
     * @param version
     * @param page
     * @return
     * @
     */
    PageHelper<ProcessVariable> loadProcessVariables(String processDefinitionId, int version, PageHelper<ProcessVariable> page);

    int getProcessOrder(ProcessVariable bean);
}
