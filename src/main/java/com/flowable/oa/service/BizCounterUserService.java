package com.flowable.oa.service;

import java.util.List;
import java.util.Map;

import com.flowable.oa.entity.BizCounterUser;
import com.flowable.oa.util.mybatis.IBaseService;
import com.github.pagehelper.PageInfo;

/**
 * 2016年8月23日
 *
 * @author lukw
 * 下午8:13:52
 * com.eastcom.esflow.service
 * @email lukw@eastcom-sw.com
 */

public interface BizCounterUserService extends IBaseService<BizCounterUser> {

	PageInfo<BizCounterUser> findBizCounterUser(PageInfo<BizCounterUser> page, BizCounterUser user);

    void deleteUser(BizCounterUser user);

    void saveUser(List<Map<String, String>> list, String bizId, String taskId);

    void updateUser(List<Map<String, String>> list, String bizId, String taskId);

}

