package com.flowable.oa.api.controller.sso;

import com.flowable.oa.core.entity.auth.SystemResource;
import com.flowable.oa.core.service.auth.ISystemResourceService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-17 下午7:34
 **/
@Controller
@RequestMapping("resource")
public class SystemResourceController {

    @Autowired
    private ISystemResourceService resourceService;

    @PostMapping("/save")
    public RestResult<SystemResource> save(SystemResource resource) {

        this.resourceService.saveOrUpdate(resource);
        return RestResult.success(resource);
    }

    @PostMapping("/delete")
    public RestResult<Object> delete(@RequestBody List<Serializable> list) {

        this.resourceService.deleteByIds(list);
        return RestResult.success();
    }

    @RequestMapping("/list")
    public RestResult<DataGrid<SystemResource>> list(PageInfo<SystemResource> pageInfo, SystemResource resource) {

        PageInfo<SystemResource> result = this.resourceService.list(pageInfo, resource);
        DataGrid<SystemResource> dataGrid = new DataGrid<>();
        dataGrid.setTotal(result.getTotal());
        dataGrid.setRows(result.getList());
        return RestResult.success(dataGrid);
    }

    @GetMapping("/role/{roleId}")
    public RestResult<List<SystemResource>> findResourceByRoleId(@PathVariable("roleId") Long roleId) {

        return RestResult.success(this.resourceService.findResourceByRoleId(roleId));
    }

    @GetMapping("info/{resourceId}")
    public RestResult<SystemResource> info(@PathVariable("resourceId") Integer resourceId) {

        SystemResource resource = this.resourceService.selectByKey(resourceId);
        Optional.ofNullable(resource).map(SystemResource::getParentId)
                .filter(Objects::nonNull)
                .map(this.resourceService::selectByKey)
                .map(entity -> {
                    resource.setParentName(entity.getName());
                    return null;
                });
        return RestResult.success(resource);
    }
}
