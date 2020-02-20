package com.flowable.oa.core.service.auth.impl;

import com.flowable.oa.core.entity.auth.SysUserRole;
import com.flowable.oa.core.service.auth.ISysUserRoleService;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/2/16 13:13
 **/
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRole> implements ISysUserRoleService {

    @Override
    public void saveOrUpdate(SysUserRole userRole) {

        SysUserRole entity = new SysUserRole();
        entity.setUserId(userRole.getUserId());
        this.deleteByModel(entity);
        String roleIds = userRole.getRoleIds();
        if (StringUtils.isNotBlank(roleIds)) {
            String[] roles = roleIds.split("\\,");
            Arrays.stream(roles).forEach(roleId -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(Integer.valueOf(roleId));
                sysUserRole.setUserId(userRole.getUserId());
                this.save(sysUserRole);
            });
        }
    }

    @Override
    public List<Integer> findUserIdsByRoleId(Integer roleId) {

        SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(roleId);
        List<SysUserRole> roles = this.select(userRole);
        List<Integer> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roles)) {
            roles.forEach(entity -> list.add(entity.getUserId()));
        }
        return list;
    }

    @Override
    public List<Integer> findRoleIdsByUserId(Integer userId) {

        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        List<SysUserRole> roles = this.select(userRole);
        List<Integer> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roles)) {
            roles.forEach(entity -> list.add(entity.getRoleId()));
        }
        return list;
    }
}
