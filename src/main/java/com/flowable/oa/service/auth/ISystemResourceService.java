package com.flowable.oa.service.auth;

import com.flowable.oa.entity.auth.SystemResource;
import com.flowable.oa.util.mybatis.IBaseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-17 下午7:13
 **/
public interface ISystemResourceService extends IBaseService<SystemResource> {

    void saveOrUpdate(SystemResource resource);

    void deleteByIds(List<String> list);

    PageInfo<SystemResource> list(PageInfo<SystemResource> pageInfo,SystemResource resource);

    /**
     * 获取角色下的资源
     * @param roleId
     * @return
     */
    List<SystemResource> findResourceByRoleId(String roleId);
}
