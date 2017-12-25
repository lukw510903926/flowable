package com.flowable.core.service;

import java.util.List;
import java.util.Map;

import com.flowable.common.service.IBaseService;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.BizCounterUser;

/**
 * 2016年8月23日
 *
 * @author lukw
 * 下午8:13:52
 * com.eastcom.esflow.service
 * @email lukw@eastcom-sw.com
 */

public interface BizCounterUserService extends IBaseService<BizCounterUser> {

    PageHelper<BizCounterUser> findBizCounterUser(PageHelper<BizCounterUser> page, BizCounterUser user);

    void deleteUser(BizCounterUser user);

    void saveUser(List<Map<String, String>> list, String bizId, String taskId);

    void updateUser(List<Map<String, String>> list, String bizId, String taskId);

}

