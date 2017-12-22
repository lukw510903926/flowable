package com.flowable.core.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.core.bean.Countersign;
import com.flowable.core.dao.CountersignDao;

@Repository
public class CountersignDaoImpl extends BaseDaoImpl<Countersign> implements CountersignDao{

	public List<Countersign> findCountersign(Countersign countersign){
		
		return null;
	}
}
