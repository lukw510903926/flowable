package com.flowable.oa.api.controller.sso;

import com.flowable.oa.core.entity.auth.SystemUser;
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
 * @since 2019/2/16 15:23
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ISystemUserService systemUserService;

    @GetMapping("/info/{userId}")
    public RestResult<SystemUser> info(@PathVariable("userId") Long userId) {

        return RestResult.success(this.systemUserService.selectByKey(userId));
    }

    @PostMapping("/save")
    public RestResult<SystemUser> save(SystemUser systemUser) {

        this.systemUserService.saveOrUpdate(systemUser);
        return RestResult.success(systemUser);
    }

    @PostMapping("/delete")
    public RestResult<Object> delete(@RequestBody List<Serializable> list) {

        this.systemUserService.deleteByIds(list);
        return RestResult.success();
    }

    @PostMapping("/list")
    public RestResult<DataGrid<SystemUser>> list(PageInfo<SystemUser> pageInfo, SystemUser systemUser) {

        PageInfo<SystemUser> page = this.systemUserService.findByModel(pageInfo, systemUser, true);
        DataGrid<SystemUser> dataGrid = new DataGrid<>();
        dataGrid.setRows(page.getList());
        dataGrid.setTotal(page.getTotal());
        return RestResult.success(dataGrid);
    }
}
