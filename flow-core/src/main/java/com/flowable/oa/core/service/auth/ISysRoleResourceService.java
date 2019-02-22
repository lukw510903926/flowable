package com.flowable.oa.core.service.auth;


import com.flowable.oa.core.entity.auth.SysRoleResource;
import com.flowable.oa.core.util.mybatis.IBaseService;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-17 下午7:14
 **/
public interface ISysRoleResourceService extends IBaseService<SysRoleResource> {

    void saveOrUpdate(SysRoleResource roleResource);
}
