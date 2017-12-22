package com.flowable.core.dao.auth.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.core.bean.auth.SystemUser;
import com.flowable.core.dao.auth.ISystemUserDao;

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
