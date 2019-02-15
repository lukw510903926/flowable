package com.flowable.oa.service.impl;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.flowable.oa.entity.BizCounterUser;
import com.flowable.oa.service.BizCounterUserService;

/**
 * 2016年8月23日
 *
 * @author lukw 下午8:16:03 com.eastcom.esflow.service.impl
 * @email 13507615840@163.com
 */
@Service
public class BizCounterUserServiceImplImpl extends BaseServiceImpl<BizCounterUser> implements BizCounterUserService {

    @Override
    public PageInfo<BizCounterUser> findBizCounterUser(PageInfo<BizCounterUser> page, BizCounterUser user) {

        if (page != null) {
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
        }
        return new PageInfo<>(this.findByModel(user, false));
    }

    @Override
    @Transactional
    public void saveUser(List<Map<String, String>> list, String bizId, String taskId) {

        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(map -> {
                BizCounterUser user = new BizCounterUser();
                user.setBizId(bizId);
                user.setTaskId(StringUtils.isBlank(taskId) ? "START" : taskId);
                user.setName(map.get("name"));
                user.setUsername(map.get("username"));
                user.setDeptmentName(map.get("deptmentName"));
                this.save(user);
            });
        }
    }

    @Override
    @Transactional
    public void updateUser(List<Map<String, String>> list, String bizId, String taskId) {

        BizCounterUser user = new BizCounterUser();
        user.setBizId(bizId);
        user.setTaskId(StringUtils.isBlank(taskId) ? "START" : taskId);
        this.deleteByModel(user);
        this.saveUser(list, bizId, taskId);
    }
}
