package com.flowable.service.flow;

import java.util.List;
import java.util.Map;

import com.flowable.entity.BizCounterUser;
import com.flowable.util.PageHelper;
import com.flowable.util.service.IBaseService;

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

