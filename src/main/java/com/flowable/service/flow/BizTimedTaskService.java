package com.flowable.service.flow;

import java.util.List;

import com.flowable.entity.BizInfo;
import com.flowable.entity.BizInfoConf;
import com.flowable.entity.BizTimedTask;
import com.flowable.util.service.IBaseService;

public interface BizTimedTaskService extends IBaseService<BizTimedTask> {

    List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask);

    void saveTimedTask(BizInfo bizInfo, BizInfoConf bizConf);

    void submitBizTimedTask();
}
