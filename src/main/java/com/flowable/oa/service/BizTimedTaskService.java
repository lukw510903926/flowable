package com.flowable.oa.service;

import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.entity.BizInfoConf;
import com.flowable.oa.entity.BizTimedTask;
import com.flowable.oa.util.mybatis.IBaseService;

public interface BizTimedTaskService extends IBaseService<BizTimedTask> {

    void saveTimedTask(BizInfo bizInfo, BizInfoConf bizConf);

    void submitBizTimedTask();
}
