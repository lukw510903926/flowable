package com.flowable.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.common.service.BaseServiceImpl;
import com.flowable.core.bean.Countersign;
import com.flowable.core.dao.CountersignDao;
import com.flowable.core.service.CountersignService;

@Service
@Transactional
public class CountersignServiceImpl extends BaseServiceImpl<Countersign> implements CountersignService {

	@Autowired
	private CountersignDao countersignDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Countersign> findCountersign(Countersign countersign) {

		return this.countersignDao.findCountersign(countersign);
	}
}
