package com.flowable.core.dao.auth;

import java.util.List;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.auth.SystemUser;

public interface ISystemUserDao extends IBaseDao<SystemUser>{

	public SystemUser getUserByUsername(String username);
	
	public List<SystemUser> findSystemUser(SystemUser systemUser);
}
