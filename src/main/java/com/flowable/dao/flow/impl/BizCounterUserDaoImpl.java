package com.flowable.dao.flow.impl;

import org.springframework.stereotype.Repository;

import com.flowable.dao.flow.BizCounterUserDao;
import com.flowable.entity.BizCounterUser;
import com.flowable.util.PageHelper;
import com.flowable.util.dao.BaseDaoImpl;

/**
 * 
 * 2016年8月23日
 * @author lukw 
 * 下午8:01:21
 * com.eastcom.esflow.dao
 * @email lukw@eastcom-sw.com
 */
@Repository
public class BizCounterUserDaoImpl extends BaseDaoImpl<BizCounterUser> implements BizCounterUserDao{

	@Override
	public PageHelper<BizCounterUser> findBizCounterUser(PageHelper<BizCounterUser> page, BizCounterUser user){
		
		return null;
	}

}
