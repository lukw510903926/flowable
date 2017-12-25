package com.flowable.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.core.bean.ActBizInfoDelayTime;
import com.flowable.core.dao.ActBizInfoDelayTimeDao;
import org.springframework.util.CollectionUtils;

@Repository
public class ActBizInfoDelayTimeDaoImpl extends BaseDaoImpl<ActBizInfoDelayTime> implements ActBizInfoDelayTimeDao {

	@Override
	public ActBizInfoDelayTime findActBizInfoByBizId(String bizId, String taskName) {

		StringBuilder hql = new StringBuilder("from ActBizInfoDelayTime a where a.bizId = ?  ");
		hql.append(" and applyStatus=0 order by createTime desc ");
		List<ActBizInfoDelayTime> list = this.find(hql.toString(), new Object[] { bizId });
		if (!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<ActBizInfoDelayTime> findActBizInfoDelayTime(ActBizInfoDelayTime delayTime) {

		StringBuilder hql = new StringBuilder(" from ActBizInfoDelayTime a where 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if (delayTime != null) {
			if (StringUtils.isNotBlank(delayTime.getBizId())) {
				hql.append(" and a.bizId = ? ");
				list.add(delayTime.getBizId());
			}
			if (StringUtils.isNotBlank(delayTime.getTaskName())) {
				hql.append(" and a.taskName = ? ");
				list.add(delayTime.getTaskName());
			}
			if (StringUtils.isNotBlank(delayTime.getTaskId())) {
				hql.append(" and a.taskId = ? ");
				list.add(delayTime.getTaskId());
			}
		}
		hql.append(" order by a.createTime desc");
		return this.find(hql.toString(), list.toArray());
	}
}
