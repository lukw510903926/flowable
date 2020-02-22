package com.flowable.oa.api.controller.sso;

import com.flowable.oa.core.entity.auth.SystemUser;
import com.flowable.oa.core.service.auth.ISystemUserService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/2/16 15:23
 **/
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ISystemUserService systemUserService;

    @ResponseBody
    @GetMapping("/info/{userId}")
    public SystemUser info(@PathVariable("userId") Integer userId) {

        return this.systemUserService.selectByKey(userId);
    }

    @ResponseBody
    @PostMapping("/save")
    public RestResult<SystemUser> save(SystemUser systemUser) {

        this.systemUserService.saveOrUpdate(systemUser);
        return RestResult.success(systemUser);
    }

    @ResponseBody
    @PostMapping("/delete")
    public RestResult<Object> delete(@RequestBody List<Integer> list) {

        this.systemUserService.deleteByIds(list);
        return RestResult.success();
    }

    @ResponseBody
    @PostMapping("/list")
    public DataGrid<SystemUser> list(PageInfo<SystemUser> pageInfo, SystemUser systemUser) {

        PageInfo<SystemUser> page = this.systemUserService.findByModel(pageInfo, systemUser, true);
        DataGrid<SystemUser> dataGrid = new DataGrid<>();
        dataGrid.setRows(page.getList());
        dataGrid.setTotal(page.getTotal());
        return dataGrid;
    }
}
