package com.flowable.oa.service.impl;

import java.util.List;

import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.oa.entity.Countersign;
import com.flowable.oa.service.CountersignService;

@Service
public class CountersignServiceImplImpl extends BaseServiceImpl<Countersign> implements CountersignService {

	@Override
	@Transactional
	public List<Countersign> findCountersign(Countersign countersign) {

		return this.findByModel(countersign, false);
	}
}
