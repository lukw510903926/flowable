package com.flowable.oa.service;

import java.util.List;

import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.entity.BizInfoConf;
import com.flowable.oa.entity.BizTimedTask;
import com.flowable.oa.util.mybatis.IBaseService;

public interface BizTimedTaskService extends IBaseService<BizTimedTask> {

    List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask);

    void saveTimedTask(BizInfo bizInfo, BizInfoConf bizConf);

    void submitBizTimedTask();
}
