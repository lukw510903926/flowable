package com.flowable.core.service;

import java.util.List;

import com.flowable.common.service.IBaseService;
import com.flowable.core.bean.Countersign;

public interface CountersignService extends IBaseService<Countersign> {

	public List<Countersign> findCountersign(Countersign countersign);
}
