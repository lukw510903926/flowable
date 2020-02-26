package com.flowable.oa.core.service.auth;

import com.flowable.oa.core.entity.auth.SystemResource;
import com.flowable.oa.core.util.mybatis.IBaseService;
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

    @Override
    void saveOrUpdate(SystemResource resource);

    PageInfo<SystemResource> list(PageInfo<SystemResource> pageInfo, SystemResource resource);

    /**
     * 获取角色下的资源
     *
     * @param roleId
     * @return
     */
    List<SystemResource> findResourceByRoleId(Long roleId);

    void deleteBatch(List<Long> list);
}
