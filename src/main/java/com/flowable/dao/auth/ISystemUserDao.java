package com.flowable.dao.auth;

import java.util.List;

import com.flowable.entity.auth.SystemUser;
import com.flowable.util.dao.IBaseDao;

public interface ISystemUserDao extends IBaseDao<SystemUser>{

	public SystemUser getUserByUsername(String username);
	
	public List<SystemUser> findSystemUser(SystemUser systemUser);
}
