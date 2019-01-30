package com.flowable.dao.flow;

import com.flowable.entity.BizCounterUser;
import com.flowable.util.PageHelper;
import com.flowable.util.dao.IBaseDao;

/**
 * 
 * 2016年8月23日
 * @author lukw 
 * 下午8:01:21
 * com.eastcom.esflow.dao
 * @email lukw@eastcom-sw.com
 */
public interface BizCounterUserDao extends IBaseDao<BizCounterUser>{

	PageHelper<BizCounterUser> findBizCounterUser(PageHelper<BizCounterUser> page, BizCounterUser user);

}
