package com.flowable.core.dao.impl;

import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.BizCounterUser;
import com.flowable.core.dao.BizCounterUserDao;

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
