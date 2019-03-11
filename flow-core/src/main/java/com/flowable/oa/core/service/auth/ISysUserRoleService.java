package com.flowable.oa.core.service.auth;

import com.flowable.oa.core.entity.auth.SysUserRole;
import com.flowable.oa.core.util.mybatis.IBaseService;

import java.util.List;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/2/16 13:13
 **/
public interface ISysUserRoleService extends IBaseService<SysUserRole> {

    void saveOrUpdate(SysUserRole userRole);

    List<Integer> findUserIdsByRoleId(Integer roleId);

    List<Integer> findRoleIdsByUserId(Integer userId);
}
