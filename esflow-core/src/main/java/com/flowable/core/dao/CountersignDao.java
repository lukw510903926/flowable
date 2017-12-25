package com.flowable.core.dao;

import java.util.List;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.Countersign;

public interface CountersignDao extends IBaseDao<Countersign> {

	List<Countersign> findCountersign(Countersign countersign);
}
