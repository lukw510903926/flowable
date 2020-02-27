package com.flowable.oa.api.controller.sso;

import com.flowable.oa.core.entity.auth.SystemRole;
import com.flowable.oa.core.entity.auth.SystemUser;
import com.flowable.oa.core.service.auth.ISystemRoleService;
import com.flowable.oa.core.service.auth.ISystemUserService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午11:11
 **/
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private ISystemUserService sysUserService;

    @Autowired
    private ISystemRoleService systemRoleService;

    @RequestMapping("/loadUsersByUserName")
    public RestResult<SystemUser> loadUsersByUserName(String userName) {

        return RestResult.success(this.sysUserService.getUserByUsername(userName));
    }

    @GetMapping("/info/{roleId}")
    public RestResult<SystemRole> info(@PathVariable("roleId") Integer roleId) {

        return RestResult.success(this.systemRoleService.selectByKey(roleId));
    }

    @PostMapping("/save")
    public RestResult<SystemRole> save(SystemRole systemRole) {

        this.systemRoleService.saveOrUpdate(systemRole);
        return RestResult.success(systemRole);
    }

    @PostMapping("/delete")
    public RestResult<Object> delete(@RequestBody List<Serializable> list) {

        this.systemRoleService.deleteByIds(list);
        return RestResult.success();
    }

    @PostMapping("/list")
    public RestResult<DataGrid<SystemRole>> list(PageInfo<SystemRole> pageInfo, SystemRole systemRole) {

        PageInfo<SystemRole> page = this.systemRoleService.findByModel(pageInfo, systemRole, true);
        DataGrid<SystemRole> dataGrid = new DataGrid<>();
        dataGrid.setRows(page.getList());
        dataGrid.setTotal(page.getTotal());
        return RestResult.success(dataGrid);
    }
}
