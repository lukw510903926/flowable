package com.flowable.oa.controller.sso;

import com.flowable.oa.entity.auth.SysRoleResource;
import com.flowable.oa.service.auth.ISysRoleResourceService;
import com.flowable.oa.util.RestResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-17 下午7:23
 **/
@RestController
@RequestMapping("/role/resource")
public class SysRoleResourceController {

    @Autowired
    private ISysRoleResourceService roleResourceService;

    @PostMapping("/save")
    public RestResult<Object> save(SysRoleResource sysRoleResource){

        if(StringUtils.isBlank(sysRoleResource.getRoleId())){
            return RestResult.parameter(sysRoleResource,"roleId 不可为空");
        }
        this.roleResourceService.saveOrUpdate(sysRoleResource);
        return RestResult.success();
    }
}
