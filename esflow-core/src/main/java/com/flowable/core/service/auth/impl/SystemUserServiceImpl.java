package com.flowable.core.service.auth.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flowable.common.service.BaseServiceImpl;
import com.flowable.common.utils.ReflectionUtils;
import com.flowable.core.bean.auth.SystemRole;
import com.flowable.core.bean.auth.SystemUser;
import com.flowable.core.dao.auth.ISystemRoleDao;
import com.flowable.core.dao.auth.ISystemUserDao;
import com.flowable.core.service.auth.ISystemUserService;

@Service
public class SystemUserServiceImpl extends BaseServiceImpl<SystemUser> implements ISystemUserService{

	@Autowired
	private ISystemUserDao systemUserDao;
	
	@Autowired
	private ISystemRoleDao systemRoleDao;
	
	@Override
	public SystemUser getUserByUsername(String username) {
		
		if(StringUtils.isNotBlank(username)){
			return systemUserDao.getUserByUsername(username);
		}
		return null;
	}

	@Override
	public List<String> findUserByRole(SystemRole systemRole) {
		
		systemRole.setUsers(null);
		Map<String, Object> params = ReflectionUtils.beanToMap(systemRole);
		List<SystemRole> roles = this.systemRoleDao.findByParams(params, false);
		Set<SystemUser> userSet = new HashSet<SystemUser>();
		if (CollectionUtils.isNotEmpty(roles)) {
			roles.forEach(role -> userSet.addAll(role.getUsers()));
		}
		List<String> list = new ArrayList<String>();
		userSet.forEach(systemUser ->list.add(systemUser.getUsername()));
		return list;
	}
	
	@Override
	public String findOnlyUser(SystemRole systemRole) {

		List<String> list = this.findUserByRole(systemRole);
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<String> findUserRoles(String username) {
		
		Optional<SystemUser> systemUser = Optional.ofNullable(this.getUserByUsername(username));
		List<String> list = new ArrayList<String>();
		Set<SystemRole> roles = systemUser.map(user ->user.getRoles()).orElse(new HashSet<SystemRole>());
		roles.forEach(role ->list.add(role.getNameCn()));
		return list;
	}
}
