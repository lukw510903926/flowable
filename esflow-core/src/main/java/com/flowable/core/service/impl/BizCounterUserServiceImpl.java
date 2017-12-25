package com.flowable.core.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.common.service.BaseServiceImpl;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.BizCounterUser;
import com.flowable.core.dao.BizCounterUserDao;
import com.flowable.core.service.BizCounterUserService;

/**
 * 2016年8月23日
 * 
 * @author lukw 下午8:16:03 com.eastcom.esflow.service.impl
 * @email lukw@eastcom-sw.com
 */

@Service
@Transactional(readOnly = true)
public class BizCounterUserServiceImpl extends BaseServiceImpl<BizCounterUser> implements BizCounterUserService {

	@Autowired
	private BizCounterUserDao bizCounterUserDao;

	@Override
	public PageHelper<BizCounterUser> findBizCounterUser(PageHelper<BizCounterUser> page, BizCounterUser user) {

		return this.bizCounterUserDao.findBizCounterUser(page, user);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteUser(BizCounterUser user) {

		PageHelper<BizCounterUser> page = new PageHelper<BizCounterUser>();
		page.setPage(-1);
		page.setRows(-1);
		this.delete(this.findBizCounterUser(page, user).getList());
	}

	@Override
	@Transactional(readOnly = false)
	public void saveUser(List<Map<String, String>> list, String bizId, String taskId) {

		BizCounterUser user = null;
		if (!CollectionUtils.isNotEmpty(list)) {
			for (Map<String, String> map : list) {
				user = new BizCounterUser();
				user.setBizId(bizId);
				user.setTaskId(StringUtils.isBlank(taskId) ? "START" : taskId);
				user.setName(map.get("name"));
				user.setUsername(map.get("username"));
				user.setDeptmentName(map.get("deptmentName"));
				this.bizCounterUserDao.save(user);
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void updateUser(List<Map<String, String>> list, String bizId, String taskId) {

		BizCounterUser user = new BizCounterUser();
		user.setBizId(bizId);
		user.setTaskId(StringUtils.isBlank(taskId) ? "START" : taskId);
		this.deleteUser(user);
		this.saveUser(list, bizId, taskId);
	}
}
