package com.flowable.dao.flow.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.dao.flow.CountersignDao;
import com.flowable.entity.Countersign;
import com.flowable.util.dao.BaseDaoImpl;

@Repository
public class CountersignDaoImpl extends BaseDaoImpl<Countersign> implements CountersignDao {

    @Override
    public List<Countersign> findCountersign(Countersign countersign) {

        List<Object> list = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer(" from Countersign c where 1=1");
        if (StringUtils.isNotBlank(countersign.getBizId())) {
            hql.append(" and c.bizId = ? ");
            list.add(countersign.getBizId());
        }
        if (StringUtils.isNotBlank(countersign.getTaskId())) {
            hql.append(" and c.taskId = ? ");
            list.add(countersign.getTaskId());
        }
        if (StringUtils.isNotBlank(countersign.getTaskAssignee())) {
            hql.append(" and c.taskAssignee = ? ");
            list.add(countersign.getTaskAssignee());
        }
        if (countersign.getResultType() != null) {
            hql.append(" and c.resultType = ? ");
            list.add(countersign.getResultType());
        }
        if (countersign.getIsComplete() != null) {
            hql.append(" and c.isComplete = ? ");
            list.add(countersign.getIsComplete());
        }
        return this.find(hql.toString(), list.toArray());
    }
}
