package com.flowable.core.dao;

import java.util.List;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.BizTimedTask;

public interface BizTimedTaskDao extends IBaseDao<BizTimedTask>{

	public List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask);

	void deleteTimedTask(String id);
}
