package com.flowable.dao.flow;

import java.util.List;

import com.flowable.entity.BizTimedTask;
import com.flowable.util.dao.IBaseDao;

public interface BizTimedTaskDao extends IBaseDao<BizTimedTask>{

	List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask);

	void deleteTimedTask(String id);
}
