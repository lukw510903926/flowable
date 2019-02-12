package com.flowable.oa.service.auth.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.flowable.oa.service.auth.ISystemUserService;
import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flowable.oa.entity.auth.SystemRole;
import com.flowable.oa.entity.auth.SystemUser;
import com.flowable.oa.service.auth.ISystemRoleService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class SystemUserServiceImplImpl extends BaseServiceImpl<SystemUser> implements ISystemUserService {

	@Autowired
	private ISystemRoleService systemRoleService;
	
	@Override
	public SystemUser getUserByUsername(String username) {
		
		if(StringUtils.isNotBlank(username)){
			Example example = new Example(SystemUser.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("username", username);
			List<SystemUser> list = this.selectByExample(example);
			return CollectionUtils.isEmpty(list) ? null : list.get(0);
		}
		return null;
	}

	@Override
	public List<String> findUserByRole(SystemRole systemRole) {
		
		systemRole.setUsers(null);
		List<SystemRole> roles = this.systemRoleService.findByModel(systemRole, false);
		Set<SystemUser> userSet = new HashSet<>();
		if (CollectionUtils.isNotEmpty(roles)) {
			roles.forEach(role -> userSet.addAll(role.getUsers()));
		}
		List<String> list = new ArrayList<>();
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
		List<String> list = new ArrayList<>();
		Set<SystemRole> roles = systemUser.map(SystemUser::getRoles).orElse(new HashSet<>());
		roles.forEach(role ->list.add(role.getNameCn()));
		return list;
	}
}
