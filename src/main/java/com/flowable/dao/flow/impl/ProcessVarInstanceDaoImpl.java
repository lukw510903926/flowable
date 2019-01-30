package com.flowable.dao.flow.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.dao.flow.IProcessVarInstanceDao;
import com.flowable.entity.BizLog;
import com.flowable.entity.ProcessVariableInstance;
import com.flowable.util.dao.BaseDaoImpl;

@Repository
public class ProcessVarInstanceDaoImpl extends BaseDaoImpl<ProcessVariableInstance> implements IProcessVarInstanceDao {

    @Override
    public List<ProcessVariableInstance> loadProcessInstances(String processInstanceId) {

        if (StringUtils.isNotBlank(processInstanceId)) {
            String hql = " from ProcessVariableInstance where  processInstanceId= ? ";
            return this.find(hql, new Object[]{processInstanceId});
        }
        return null;
    }

    @Override
    public List<ProcessVariableInstance> findProcessInstances(ProcessVariableInstance instance) {

        StringBuffer hql = new StringBuffer(" from ProcessVariableInstance p where  1=1");
        List<Object> list = new ArrayList<Object>();
        if (StringUtils.isNotBlank(instance.getProcessInstanceId())) {
            hql.append(" and p.processInstanceId = ? ");
            list.add(instance.getProcessInstanceId());
        }
        if (StringUtils.isNotBlank(instance.getBizId())) {
            hql.append(" and bizId = ? ");
            list.add(instance.getBizId());
        }
        if (StringUtils.isNotBlank(instance.getTaskId())) {
            hql.append(" and p.taskId = ? ");
            list.add(instance.getTaskId());
        }
        return this.find(hql.toString(), list.toArray());
    }

    @Override
    public List<ProcessVariableInstance> loadProcessInstancesByBizId(String bizId) {

        if (StringUtils.isNotBlank(bizId)) {
            String hql = " from ProcessVariableInstance where  bizId= ? ";
            return this.find(hql, new Object[]{bizId});
        }
        return new ArrayList<ProcessVariableInstance>();
    }

    @Override
    public List<ProcessVariableInstance> getProcessVariableInstances(Map<String, String> params) {

        List<Object> list = new ArrayList<Object>();
        StringBuilder hql = new StringBuilder(" from ProcessVariableInstance p join fetch p.variable b where 1=1 ");
        if (StringUtils.isNotBlank(params.get("instanceId"))) {
            hql.append(" and p.processInstanceId = ? ");
            list.add(params.get("instanceId"));
        }
        if (StringUtils.isNotBlank(params.get("name"))) {
            hql.append(" and b.name = ? ");
            list.add(params.get("name"));
        }
        if (StringUtils.isNotBlank(params.get("variableId"))) {
            hql.append(" and b.id = ? ");
            list.add(params.get("variableId"));
        }
        if (StringUtils.isNotBlank(params.get("bizId"))) {
            hql.append(" and p.bizId = ? ");
            list.add(params.get("bizId"));
        }
        return this.find(hql.toString(), list.toArray());
    }

    @Override
    public void deleteByVarId(String variableId) {

        if (StringUtils.isNotBlank(variableId)) {
            String hql = " delete from ProcessVariableInstance p where p.variable.id = ? ";
            this.execute(hql, new Object[]{variableId});
        }
    }

    @Override
    public List<ProcessVariableInstance> loadValueByLog(BizLog logBean) {

        StringBuffer hql = new StringBuffer("FROM ProcessVariableInstance bean left join fetch bean.variable ");
        hql.append("  v WHERE bean.processInstanceId=? AND bean.taskId=?  order by v.order asc");
        return this.find(hql.toString(), new Object[]{logBean.getBizInfo().getProcessInstanceId(), logBean.getTaskID()});
    }

    @Override
    public List<ProcessVariableInstance> findByProcInstId(String processInstanceId) {

        String hql = " from ProcessVariableInstance where processInstanceId = ? order by createTime asc";
        return this.find(hql, new Object[]{processInstanceId});
    }

}
