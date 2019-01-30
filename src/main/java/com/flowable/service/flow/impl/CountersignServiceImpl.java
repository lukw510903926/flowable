package com.flowable.service.flow.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.dao.flow.CountersignDao;
import com.flowable.entity.Countersign;
import com.flowable.service.flow.CountersignService;
import com.flowable.util.service.BaseServiceImpl;

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
