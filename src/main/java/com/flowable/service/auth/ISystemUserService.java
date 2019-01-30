package com.flowable.service.auth;

import java.util.List;

import com.flowable.entity.auth.SystemRole;
import com.flowable.entity.auth.SystemUser;
import com.flowable.util.service.IBaseService;

public interface ISystemUserService  extends IBaseService<SystemUser>{

	public SystemUser getUserByUsername(String username);

	public List<String> findUserByRole(SystemRole systemRole);

	public List<String> findUserRoles(String username);

	String findOnlyUser(SystemRole systemRole);
}
