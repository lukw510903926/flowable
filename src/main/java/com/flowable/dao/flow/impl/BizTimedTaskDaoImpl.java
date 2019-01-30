package com.flowable.dao.flow.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.dao.flow.BizTimedTaskDao;
import com.flowable.entity.BizTimedTask;
import com.flowable.util.dao.BaseDaoImpl;

@Repository
public class BizTimedTaskDaoImpl extends BaseDaoImpl<BizTimedTask> implements BizTimedTaskDao {

	@Override
	public List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask) {

		List<Object> list = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(" from BizTimedTask t where 1=1 ");
		if(bizTask.getEndTime()!=null){
			hql.append(" and t.endTime = ? ");
			list.add(bizTask.getEndTime());
		}
		if(StringUtils.isNotBlank(bizTask.getBizId())){
			hql.append(" and t.bizId = ? ");
			list.add(bizTask.getBizId());
		}
		if(StringUtils.isNotBlank(bizTask.getTaskId())){
			hql.append(" and t.taskId = ? ");
			list.add(bizTask.getTaskId());
		}
		return this.find(hql.toString(), list.toArray());
	}
	
	@Override
	public void deleteTimedTask(String id){
		
		if(StringUtils.isNotBlank(id)){
			String sql =" DELETE FROM ESFLOW.BIZ_TIMED_TASK WHERE ID = ? ";
			this.executeBySql(sql,new Object[]{id});
		}
	}
}
