package com.flowable.oa.service.auth.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.flowable.oa.entity.auth.SysUserRole;
import com.flowable.oa.service.auth.ISysUserRoleService;
import com.flowable.oa.service.auth.ISystemUserService;
import com.flowable.oa.util.exception.ServiceException;
import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.flowable.oa.entity.auth.SystemRole;
import com.flowable.oa.entity.auth.SystemUser;
import com.flowable.oa.service.auth.ISystemRoleService;
import tk.mybatis.mapper.entity.Example;

@Service
public class SystemUserServiceImplImpl extends BaseServiceImpl<SystemUser> implements ISystemUserService {

    @Autowired
    private ISystemRoleService systemRoleService;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Override
    public void saveOrUpdate(SystemUser systemUser) {

        if (!this.check(systemUser)) {
            throw new ServiceException("用户账号不可重复");
        }
        if (StringUtils.isNotBlank(systemUser.getId())) {
            this.updateNotNull(systemUser);
        } else {
            systemUser.setCreateTime(new Date());
            this.save(systemUser);
        }
    }

    @Override
    public void deleteByIds(List<String> list) {

        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(userId -> {
                this.deleteById(userId);
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                this.userRoleService.deleteByModel(userRole);
            });
        }
    }

    @Override
    public SystemUser getUserByUsername(String username) {

        if (StringUtils.isNotBlank(username)) {
            SystemUser systemUser = new SystemUser();
            systemUser.setUsername(username);
            return this.selectOne(systemUser);
        }
        return null;
    }

    @Override
    public List<SystemUser> findUserByRole(SystemRole systemRole) {

        List<SystemRole> roles = this.systemRoleService.select(systemRole);
        List<String> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roles)) {
            roles.forEach(role -> list.addAll(this.userRoleService.findUserIdsByRoleId(role.getId())));
        }
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Example example = new Example(SystemUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", list);
        return this.selectByExample(example);
    }

    @Override
    public String findOnlyUser(SystemRole systemRole) {

        List<SystemUser> list = this.findUserByRole(systemRole);
        return CollectionUtils.isNotEmpty(list) && list.size() == 1 ? list.get(0).getUsername() : null;
    }

    private boolean check(SystemUser systemUser) {

        SystemUser example = new SystemUser();
        example.setUsername(systemUser.getUsername());
        return this.check(systemUser.getId(), this.select(example));
    }
}
