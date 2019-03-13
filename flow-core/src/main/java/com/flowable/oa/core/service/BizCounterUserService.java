package com.flowable.oa.core.service;

import java.util.List;
import java.util.Map;

import com.flowable.oa.core.entity.BizCounterUser;
import com.flowable.oa.core.util.mybatis.IBaseService;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/13 11:15
 **/
public interface BizCounterUserService extends IBaseService<BizCounterUser> {

    void saveUser(List<Map<String, String>> list, String bizId, String taskId);
}

