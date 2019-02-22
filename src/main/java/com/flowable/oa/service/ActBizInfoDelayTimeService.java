package com.flowable.oa.service;

import com.flowable.oa.entity.ActBizInfoDelayTime;
import com.flowable.oa.util.mybatis.IBaseService;

public interface ActBizInfoDelayTimeService extends IBaseService<ActBizInfoDelayTime> {

    void saveOrUpdate(ActBizInfoDelayTime actBizInfo);
}
