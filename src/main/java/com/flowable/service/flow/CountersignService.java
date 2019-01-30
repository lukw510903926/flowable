package com.flowable.service.flow;

import java.util.List;

import com.flowable.entity.Countersign;
import com.flowable.util.service.IBaseService;

public interface CountersignService extends IBaseService<Countersign> {

    List<Countersign> findCountersign(Countersign countersign);
}
	