package com.flowable.oa.controller.sso;

import com.flowable.oa.core.entity.auth.SysUserRole;
import com.flowable.oa.core.entity.auth.SystemRole;
import com.flowable.oa.core.entity.auth.SystemUser;
import com.flowable.oa.core.service.auth.ISysUserRoleService;
import com.flowable.oa.core.service.auth.ISystemRoleService;
import com.flowable.oa.core.service.auth.ISystemUserService;
import com.flowable.oa.core.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/2/16 16:07
 **/
@RestController
@RequestMapping("user/role")
public class SysUserRoleController {

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private ISystemRoleService systemRoleService;

    @Autowired
    private ISystemUserService systemUserService;

    /**
     * 用户添加角色
     *
     * @return
     */
    @PostMapping("/save")
    public RestResult<Object> save(SysUserRole sysUserRole) {

        this.userRoleService.saveOrUpdate(sysUserRole);
        return RestResult.success();
    }

    /**
     * 角色下用户
     *
     * @param roleId
     * @return
     */
    @ResponseBody
    @GetMapping("users/{roleId}")
    public List<SystemUser> findUserByRoleId(@PathVariable("roleId") String roleId) {

        SystemRole systemRole = new SystemRole();
        systemRole.setId(roleId);
        return this.systemUserService.findUserByRole(systemRole);
    }

    /**
     * 用户角色
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @GetMapping("/roles/{userId}")
    public List<SystemRole> findRoleByUserId(@PathVariable("userId") String userId) {

        SystemUser systemUser = new SystemUser();
        systemUser.setId(userId);
        return this.systemRoleService.findUserRole(systemUser);
    }
}
