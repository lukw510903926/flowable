package com.flowable.core.service.auth;

import java.util.List;

import com.flowable.common.service.IBaseService;
import com.flowable.core.bean.auth.SystemRole;
import com.flowable.core.bean.auth.SystemUser;

public interface ISystemUserService  extends IBaseService<SystemUser>{

	public SystemUser getUserByUsername(String username);

	public List<String> findUserByRole(SystemRole systemRole);

	public List<String> findUserRoles(String username);

	String findOnlyUser(SystemRole systemRole);
}
