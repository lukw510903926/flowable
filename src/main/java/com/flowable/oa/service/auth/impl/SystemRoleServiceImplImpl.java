package com.flowable.oa.service.auth.impl;

import com.flowable.oa.entity.auth.SysUserRole;
import com.flowable.oa.entity.auth.SystemUser;
import com.flowable.oa.service.auth.ISysUserRoleService;
import com.flowable.oa.service.auth.ISystemUserService;
import com.flowable.oa.util.exception.ServiceException;
import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.flowable.oa.entity.auth.SystemRole;
import com.flowable.oa.service.auth.ISystemRoleService;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SystemRoleServiceImplImpl extends BaseServiceImpl<SystemRole> implements ISystemRoleService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Override
    public void saveOrUpdate(SystemRole systemRole) {

        if (!this.check(systemRole)) {
            throw new ServiceException("角色中文名称/英文名称不可重复");
        }
        if (StringUtils.isNotBlank(systemRole.getId())) {
            this.updateNotNull(systemRole);
        } else {
            systemRole.setCreateTime(new Date());
            this.save(systemRole);
        }
    }

    @Override
    public void deleteByIds(List<String> list) {

        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(roleId -> {
                this.deleteById(roleId);
                SysUserRole userRole = new SysUserRole();
                userRole.setRoleId(roleId);
                this.userRoleService.deleteByModel(userRole);
            });
        }
    }

    @Override
    public List<String> findUserRoles(String username) {

        SystemUser systemUser = new SystemUser();
        systemUser.setUsername(username);
        List<SystemRole> roles = this.findUserRole(systemUser);
        List<String> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roles)) {
            roles.forEach(role -> list.add(role.getNameCn()));
        }
        return list;
    }

    @Override
    public List<SystemRole> findUserRole(SystemUser systemUser) {

        SystemUser entity = this.systemUserService.selectOne(systemUser);
        List<String> roleIds = Optional.ofNullable(entity).map(user -> this.userRoleService.findRoleIdsByUserId(user.getId())).orElse(new ArrayList<>());
        if (CollectionUtils.isEmpty(roleIds)) {
            return null;
        }
        Example example = new Example(SystemRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", roleIds);
        return this.selectByExample(example);
    }

    private boolean check(SystemRole systemRole) {

        SystemRole example = new SystemRole();
        example.setNameCn(systemRole.getNameCn());
        if (this.check(systemRole.getId(), this.select(systemRole))) {
            example.setNameCn(null);
            example.setNameEn(systemRole.getNameEn());
            return this.check(systemRole.getId(), this.select(systemRole));
        }
        return false;
    }
}
