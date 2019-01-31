package com.flowable.oa.service.auth;

import java.util.List;

import com.flowable.oa.entity.auth.SystemRole;
import com.flowable.oa.entity.auth.SystemUser;
import com.flowable.oa.util.mybatis.IBaseService;

public interface ISystemUserService  extends IBaseService<SystemUser> {

	SystemUser getUserByUsername(String username);

	List<String> findUserByRole(SystemRole systemRole);

	List<String> findUserRoles(String username);

	String findOnlyUser(SystemRole systemRole);
}
