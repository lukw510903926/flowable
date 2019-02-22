package com.flowable.oa.service.auth;

import java.util.List;

import com.flowable.oa.entity.auth.SystemRole;
import com.flowable.oa.entity.auth.SystemUser;
import com.flowable.oa.util.mybatis.IBaseService;

public interface ISystemUserService  extends IBaseService<SystemUser> {

	void saveOrUpdate(SystemUser systemUser);

	SystemUser getUserByUsername(String username);

	List<SystemUser> findUserByRole(SystemRole systemRole);

	String findOnlyUser(SystemRole systemRole);
}
