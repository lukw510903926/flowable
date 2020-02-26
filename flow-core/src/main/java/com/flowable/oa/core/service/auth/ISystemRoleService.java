package com.flowable.oa.core.service.auth;

import com.flowable.oa.core.entity.auth.SystemRole;
import com.flowable.oa.core.entity.auth.SystemUser;
import com.flowable.oa.core.util.mybatis.IBaseService;
import java.util.List;

public interface ISystemRoleService extends IBaseService<SystemRole> {

    /**
     * 保存
     *
     * @param systemRole
     */
    @Override
    void saveOrUpdate(SystemRole systemRole);

    List<String> findUserRoles(String username);

    List<SystemRole> findUserRole(SystemUser systemUser);
}
