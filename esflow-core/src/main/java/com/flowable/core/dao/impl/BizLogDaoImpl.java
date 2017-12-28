package com.flowable.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.core.bean.BizLog;
import com.flowable.core.dao.IBizLogDao;

@Repository
public class BizLogDaoImpl extends BaseDaoImpl<BizLog> implements IBizLogDao {

	@Override
	public List<BizLog> loadLogByBizId(String bizId)  {
		return this.find("FROM BizLog bean WHERE bean.bizInfo.id=? ORDER BY bean.createTime ASC", new Object[] { bizId });
	}


	@Override
	public List<BizLog> findBizLogs(BizLog bizLog){
		
		
		List<Object> list = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("FROM BizLog bean WHERE 1=1 ");
		if(StringUtils.isNotBlank(bizLog.getHandleResult())){
			hql.append(" and bean.handleResult =? ");
			list.add(bizLog.getHandleResult());
		}
		if(bizLog.getBizInfo()!= null && StringUtils.isNotBlank(bizLog.getBizInfo().getId())){
			hql.append(" and bean.bizInfo.id = ? ");
			list.add(bizLog.getBizInfo().getId());
		}
		if(StringUtils.isNotBlank(bizLog.getTaskName())){
			hql.append(" and bean.taskName =? ");
			list.add(bizLog.getTaskName());
		}
		hql.append(" order by bean.createTime asc ");
		return this.find(hql.toString(),list.toArray()); 
	}
}
