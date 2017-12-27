package com.flowable.core.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.common.exception.ServiceException;
import com.flowable.common.utils.PageHelper;
import com.flowable.common.utils.ReflectionUtils;
import com.flowable.core.bean.ProcessVariable;
import com.flowable.core.dao.IProcessVariableDao;

@Repository
public class ProcessVariableDaoImpl extends BaseDaoImpl<ProcessVariable> implements IProcessVariableDao {

    @Override
    public List<ProcessVariable> loadProcessVariables(String processDefinitionId, int version) {

        StringBuffer hql = new StringBuffer();
        hql.append("FROM ProcessVariable BEAN WHERE BEAN.processDefinitionId=? ");
        hql.append(" AND BEAN.version = ");
        Object[] args = null;
        if (version < 0) {
            args = new Object[]{processDefinitionId, processDefinitionId};
            hql.append("(SELECT max(version) FROM ProcessVariable WHERE processDefinitionId=?)");
        } else {
            hql.append("?");
            args = new Object[]{processDefinitionId, version};
        }
        hql.append(" ORDER BY BEAN.groupOrder ASC,BEAN.order ASC");
        return this.find(hql.toString(), args);
    }

    @Override
    public PageHelper<ProcessVariable> loadProcessVariables(String processDefinitionId, int version,
                                                            PageHelper<ProcessVariable> page) {

        StringBuffer hql = new StringBuffer();
        hql.append("FROM ProcessVariable BEAN WHERE BEAN.processDefinitionId=? ");
        hql.append(" AND BEAN.version = ");
        Object[] args = null;
        if (version < 0) {
            args = new Object[]{processDefinitionId, processDefinitionId};
            hql.append("(SELECT max(version) FROM ProcessVariable WHERE processDefinitionId=?)");
        } else {
            hql.append("?");
            args = new Object[]{processDefinitionId, version};
        }
        hql.append(" ORDER BY BEAN.groupOrder ASC,BEAN.order ASC");
        return this.find(page, hql.toString(), args);
    }

    @Override
    public PageHelper<ProcessVariable> findProcessVariables(ProcessVariable variable, PageHelper<ProcessVariable> page) {

        List<Object> list = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer();
        this.buildHql(list,variable,hql);
        return this.find(page, hql.toString(), list.toArray());
    }

    @Override
    public List<ProcessVariable> findProcessVariables(ProcessVariable variable) {

        List<Object> list = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer();
        this.buildHql(list,variable,hql);
        return this.find(hql.toString(), list.toArray());
    }

    private void buildHql(List<Object> list,ProcessVariable variable, StringBuffer hql ){

        hql.append(" FROM ProcessVariable BEAN WHERE 1=1 ");
        if (StringUtils.isNotBlank(variable.getProcessDefinitionId())) {
            hql.append(" and BEAN.processDefinitionId=? ");
            list.add(variable.getProcessDefinitionId());
        }
        hql.append(" AND BEAN.version = ");
        Integer version = variable.getVersion();
        if (version != null) {
            if (version < 0) {
                hql.append("(SELECT max(version) FROM ProcessVariable WHERE processDefinitionId=?)");
                list.add(variable.getProcessDefinitionId());
            } else {
                hql.append("?");
                list.add(version);
            }
        }
        if (StringUtils.isNotBlank(variable.getTaskId())) {
            hql.append(" and BEAN.taskId = ? ");
            list.add(variable.getTaskId());
        }
        hql.append(" ORDER BY BEAN.groupOrder ASC,BEAN.order ASC");
    }

    @Override
    public List<ProcessVariable> getRefList(Map<String, Object> params) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from ProcessVariable where processDefinitionId=? ");
        hql.append("and version=? and refVariable is not null and refVariable<>''");
        List<ProcessVariable> list = this.find(hql.toString(),
                new Object[]{params.get("processId"), params.get("version")});
        return list;
    }

    @Override
    public void deleteById(String id) {

        String hql = "delete from ProcessVariable where id = ? ";
        this.execute(hql, new Object[]{id});
    }

    @Override
    public int getProcessOrder(ProcessVariable bean) throws ServiceException {

        List<Object> array = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("SELECT MAX(NAME_ORDER) FROM RM.ACT_B_PROC_VAL WHERE 1=1");
        if (StringUtils.isNotBlank(bean.getGroupName())) {
            sql.append(" and GROUP_NAME=?");
            array.add(bean.getGroupName());
        }
        sql.append(" AND TEMP_ID= ? AND VERSION= ? ");
        array.add(bean.getProcessDefinitionId());
        array.add(bean.getVersion());
        List<Object> list = this.findBySql(sql.toString(), array.toArray());
        if (list == null || list.get(0) == null) {
            return 0;
        }
        return (Integer) ReflectionUtils.convert(list.get(0), Integer.class);
    }
}
