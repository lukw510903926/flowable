package com.flowable.oa.service;

import java.util.List;

import com.flowable.oa.entity.Countersign;
import com.flowable.oa.util.mybatis.IBaseService;

public interface CountersignService extends IBaseService<Countersign> {

    List<Countersign> findCountersign(Countersign countersign);
}
