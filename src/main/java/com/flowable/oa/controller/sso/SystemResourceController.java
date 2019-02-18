package com.flowable.oa.controller.sso;

import com.flowable.oa.entity.auth.SystemResource;
import com.flowable.oa.service.auth.ISystemResourceService;
import com.flowable.oa.util.DataGrid;
import com.flowable.oa.util.RestResult;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public RestResult<Object> delete(@RequestBody List<String> list) {

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
    public List<SystemResource> findResourceByRoleId(@PathVariable("roleId") String roleId) {

        return this.resourceService.findResourceByRoleId(roleId);
    }

    @ResponseBody
    @GetMapping("info/{resourceId}")
    public SystemResource info(@PathVariable("resourceId") String resourceId) {

        SystemResource resource = this.resourceService.selectByKey(resourceId);
        Optional.ofNullable(resource).map(SystemResource::getParentId)
                .filter(StringUtils::isNotBlank)
                .map(this.resourceService::selectByKey)
                .map(entity -> {
                    resource.setParentName(entity.getName());
                    return null;
                });
        return resource;
    }
}
