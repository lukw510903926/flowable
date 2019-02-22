package com.flowable.oa.core.service;


import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.BizInfoConf;
import com.flowable.oa.core.entity.BizTimedTask;
import com.flowable.oa.core.util.mybatis.IBaseService;

public interface BizTimedTaskService extends IBaseService<BizTimedTask> {

    void saveTimedTask(BizInfo bizInfo, BizInfoConf bizConf);

    void submitBizTimedTask();
}
