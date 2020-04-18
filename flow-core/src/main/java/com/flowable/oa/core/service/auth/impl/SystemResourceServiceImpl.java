package com.flowable.oa.core.service.auth.impl;

import com.flowable.oa.core.entity.auth.SysRoleResource;
import com.flowable.oa.core.entity.auth.SystemResource;
import com.flowable.oa.core.service.auth.ISysRoleResourceService;
import com.flowable.oa.core.service.auth.ISystemResourceService;
import com.flowable.oa.core.util.exception.ServiceException;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-17 下午7:16
 **/
@Service
public class SystemResourceServiceImpl extends BaseServiceImpl<SystemResource> implements ISystemResourceService {

    @Autowired
    private ISysRoleResourceService roleResourceService;

    @Override
    public void saveOrUpdate(SystemResource resource) {

        if (!this.check(resource)) {
            throw new ServiceException("资源路径/同一父节点下资源名称 不可重复");
        }
        String resourceUrl = resource.getResourceUrl();
        if (StringUtils.isNotBlank(resourceUrl)) {
            resource.setPermission(resourceUrl.replaceAll("\\/", ".").replaceAll("\\{", ".").replaceAll("\\}", "."));
        }
        if (resource.getId() != null) {
            resource.setCreateTime(new Date());
            this.save(resource);
        } else {
            this.updateNotNull(resource);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> list) {

        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(resourceId -> {
                SystemResource resource = new SystemResource();
                resource.setParentId(resourceId);
                List<SystemResource> select = this.select(resource);
                String name = Optional.ofNullable(this.selectByKey(resourceId)).map(SystemResource::getName).orElse(null);
                if (CollectionUtils.isNotEmpty(select)) {
                    throw new ServiceException(name + " 下存在资源节点不可删除");
                }
                this.deleteById(resourceId);
            });
        }

    }

    @Override
    public PageInfo<SystemResource> list(PageInfo<SystemResource> pageInfo, SystemResource resource) {

        PageInfo<SystemResource> result = this.findByModel(pageInfo, resource, false);
        if (CollectionUtils.isNotEmpty(result.getList())) {
            result.getList().forEach(entity -> Optional.ofNullable(entity.getParentId()).map(resourceId -> {
                        SystemResource systemResource = this.selectByKey(resourceId);
                        return Optional.ofNullable(systemResource).map(item -> {
                            entity.setParentName(item.getName());
                            return item.getName();
                        }).orElse("");
                    })
            );
        }
        return result;
    }

    @Override
    public List<SystemResource> findResourceByRoleId(Long roleId) {

        SysRoleResource roleResource = new SysRoleResource();
        roleResource.setRoleId(roleId);
        List<SysRoleResource> roleResources = this.roleResourceService.select(roleResource);
        if (CollectionUtils.isNotEmpty(roleResources)) {
            List<Long> resourceIds = new ArrayList<>();
            roleResources.forEach(entity -> resourceIds.add(entity.getResourceId()));
            Example example = new Example(SystemResource.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id,", resourceIds);
            return this.selectByExample(example);
        }
        return null;
    }

    private boolean check(SystemResource resource) {

        SystemResource entity = new SystemResource();
        entity.setResourceUrl(resource.getResourceUrl());
        if (this.check(resource.getId(), this.select(entity))) {
            entity.setResourceUrl(null);
            entity.setName(resource.getName());
            entity.setParentId(resource.getParentId());
            return this.check(resource.getId(), this.select(entity));
        }
        return false;
    }

}
