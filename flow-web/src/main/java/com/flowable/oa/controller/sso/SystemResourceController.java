package com.flowable.oa.controller.sso;

import com.flowable.oa.core.entity.auth.SystemResource;
import com.flowable.oa.core.service.auth.ISystemResourceService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * @since 19-2-17 下午7:34
 **/
@Controller
@RequestMapping("resource")
public class SystemResourceController {

    @Autowired
    private ISystemResourceService resourceService;

    @GetMapping("/index")
    public String index() {

        return "modules/sso/resource";
    }

    @ResponseBody
    @PostMapping("/save")
    public RestResult<SystemResource> save(SystemResource resource) {

        this.resourceService.saveOrUpdate(resource);
        return RestResult.success(resource);
    }

    @ResponseBody
    @PostMapping("/delete")
    public RestResult<Object> delete(@RequestBody List<Serializable> list) {

        this.resourceService.deleteByIds(list);
        return RestResult.success();
    }

    @ResponseBody
    @RequestMapping("/list")
    public DataGrid<SystemResource> list(PageInfo<SystemResource> pageInfo, SystemResource resource) {

        PageInfo<SystemResource> result = this.resourceService.list(pageInfo, resource);
        DataGrid<SystemResource> dataGrid = new DataGrid<>();
        dataGrid.setTotal(result.getTotal());
        dataGrid.setRows(result.getList());
        return dataGrid;
    }

    @ResponseBody
    @GetMapping("/role/{roleId}")
    public List<SystemResource> findResourceByRoleId(@PathVariable("roleId") Long roleId) {

        return this.resourceService.findResourceByRoleId(roleId);
    }

    @ResponseBody
    @GetMapping("info/{resourceId}")
    public SystemResource info(@PathVariable("resourceId") Long resourceId) {

        SystemResource resource = this.resourceService.selectByKey(resourceId);
        Optional.ofNullable(resource).map(SystemResource::getParentId)
                .map(this.resourceService::selectByKey)
                .map(entity -> {
                    resource.setParentName(entity.getName());
                    return null;
                });
        return resource;
    }
}
