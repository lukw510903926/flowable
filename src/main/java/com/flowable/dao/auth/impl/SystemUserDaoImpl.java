package com.flowable.dao.auth.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.dao.auth.ISystemUserDao;
import com.flowable.entity.auth.SystemUser;
import com.flowable.util.dao.BaseDaoImpl;

@Repository
public class SystemUserDaoImpl extends BaseDaoImpl<SystemUser> implements ISystemUserDao{

	@Override
	public SystemUser getUserByUsername(String username) {
		return this.getUniqueResult("username", username);
	}

	@Override
	public List<SystemUser> findSystemUser(SystemUser systemUser) {

		StringBuffer hql = new StringBuffer(" from SystemUser where 1=1 ");
		List<Object> list = new ArrayList<Object>();
		
		if(StringUtils.isNotBlank(systemUser.getName())){
			hql.append(" and name like ? ");
			list.add("%" + systemUser.getName()+ "%");
		}
		if(StringUtils.isNotBlank(systemUser.getUsername())){
			hql.append(" and username = ? ");
			list.add(systemUser.getUsername());
		}
		return this.find(hql.toString(), list.toArray());
	}
	
}
