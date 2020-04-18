package com.flowable.oa.core.service.auth.impl;

import com.flowable.oa.core.entity.auth.SysUserRole;
import com.flowable.oa.core.service.auth.ISysUserRoleService;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SysUserRole userRole) {

        SysUserRole entity = new SysUserRole();
        entity.setUserId(userRole.getUserId());
        this.deleteByModel(entity);
        String roleIds = userRole.getRoleIds();
        if (StringUtils.isNotBlank(roleIds)) {
            String[] roles = roleIds.split("\\,");
            Arrays.stream(roles).forEach(roleId -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(Long.valueOf(roleId));
                sysUserRole.setUserId(userRole.getUserId());
                this.save(sysUserRole);
            });
        }
    }

    @Override
    public List<Long> findUserIdsByRoleId(Long roleId) {

        SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(roleId);
        List<SysUserRole> roles = this.select(userRole);
        return Optional.ofNullable(roles).orElse(Collections.emptyList()).stream()
                .map(SysUserRole::getUserId).collect(Collectors.toList());
    }

    @Override
    public List<Long> findRoleIdsByUserId(Long userId) {

        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        List<SysUserRole> roles = this.select(userRole);
        return Optional.ofNullable(roles).orElse(Collections.emptyList()).stream()
                .map(SysUserRole::getRoleId).collect(Collectors.toList());
    }
}
