package com.flowable.dao.flow;

import java.util.List;

import com.flowable.entity.Countersign;
import com.flowable.util.dao.IBaseDao;

public interface CountersignDao extends IBaseDao<Countersign> {

	List<Countersign> findCountersign(Countersign countersign);
}
