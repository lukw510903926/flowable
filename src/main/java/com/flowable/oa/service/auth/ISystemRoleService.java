package com.flowable.oa.service.auth;

import com.flowable.oa.entity.auth.SystemUser;
import com.flowable.oa.util.mybatis.IBaseService;
import com.flowable.oa.entity.auth.SystemRole;

import java.util.List;

public interface ISystemRoleService extends IBaseService<SystemRole> {

    /**
     * 保存
     * @param systemRole
     */
    void saveOrUpdate(SystemRole systemRole);

    List<String> findUserRoles(String username);

    List<SystemRole> findUserRole(SystemUser systemUser);
}
