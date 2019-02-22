package com.flowable.oa.core.service.auth;

import com.flowable.oa.core.entity.auth.SystemRole;
import com.flowable.oa.core.entity.auth.SystemUser;
import com.flowable.oa.core.util.mybatis.IBaseService;

import java.util.List;


public interface ISystemUserService  extends IBaseService<SystemUser> {

	void saveOrUpdate(SystemUser systemUser);

	SystemUser getUserByUsername(String username);

	List<SystemUser> findUserByRole(SystemRole systemRole);

	String findOnlyUser(SystemRole systemRole);
}
