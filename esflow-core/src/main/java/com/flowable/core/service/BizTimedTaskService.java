package com.flowable.core.service;

import java.util.List;

import com.flowable.common.service.IBaseService;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.BizInfoConf;
import com.flowable.core.bean.BizTimedTask;

public interface BizTimedTaskService extends IBaseService<BizTimedTask> {

	public List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask);

	public void saveTimedTask(BizInfo bizInfo,BizInfoConf bizConf);

	public void sumitBizTimedTask();
}
