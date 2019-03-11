package com.flowable.oa.core.service.auth.impl;

import com.flowable.oa.core.entity.auth.SysRoleResource;
import com.flowable.oa.core.service.auth.ISysRoleResourceService;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-17 下午7:18
 **/
@Service
public class SysRoleResourceServiceImpl extends BaseServiceImpl<SysRoleResource> implements ISysRoleResourceService {

    public void saveOrUpdate(SysRoleResource roleResource) {

        SysRoleResource entity = new SysRoleResource();
        entity.setRoleId(roleResource.getRoleId());
        this.deleteByModel(entity);
        if (CollectionUtils.isNotEmpty(roleResource.getResourceIds())) {
            roleResource.getResourceIds().forEach(resourceId -> {
                SysRoleResource sysRoleResource = new SysRoleResource();
                sysRoleResource.setRoleId(roleResource.getRoleId());
                sysRoleResource.setResourceId(resourceId);
                this.save(sysRoleResource);
            });
        }
    }

}
