package com.flowable.oa.api.controller;

import com.flowable.oa.core.entity.ProcessVariable;
import com.flowable.oa.core.service.IProcessVariableService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午11:09
 **/
@Slf4j
@RestController
@RequestMapping("/processModelMgr")
public class ProcessVariableController {

    @Autowired
    private IProcessVariableService processValService;

    /**
     * 得到流程全局变量列表 / 任务变量列表
     *
     * @param params
     * @param page
     * @return
     */
    @GetMapping("processValList")
    public RestResult<DataGrid<ProcessVariable>> processValList(@RequestParam Map<String, Object> params, PageInfo<ProcessVariable> page) {

        DataGrid<ProcessVariable> grid = new DataGrid<>();
        String processId = MapUtils.getString(params, "processId");
        String taskId = MapUtils.getString(params, "taskId");
        ProcessVariable variable = new ProcessVariable();
        variable.setProcessDefinitionId(processId);
        variable.setTaskId(taskId);
        PageInfo<ProcessVariable> processValBeans = this.processValService.findProcessVariables(variable, page);
        grid.setRows(processValBeans.getList());
        grid.setTotal(processValBeans.getTotal());
        return RestResult.success(grid);
    }

    /**
     * 根据全局流程变量ID得到变量详情
     *
     * @return
     */
    @GetMapping("getProcessValById/{variableId}")
    public RestResult<Object> getProcessValById(@PathVariable("variableId") Integer variableId) {

        log.info("根据全局流程变量ID得到变量详情---getProcessValById");
        return RestResult.success(processValService.selectByKey(variableId));
    }

    /**
     * 根据全局流程变量IDs删除变量详情
     *
     * @param list
     * @return
     */
    @PostMapping("deleteProcessValById")
    public RestResult<Object> deleteProcessValById(@RequestParam List<Long> list) {

        processValService.deleteVariable(list);
        return RestResult.success();
    }

    /**
     * 保存或者更新流程全局变量
     *
     * @return
     */
    @PostMapping("saveOrUpdate")
    public RestResult<Object> saveOrUpdateProcessVal(@RequestBody ProcessVariable processValAbs) {

        processValService.saveOrUpdate(processValAbs);
        return RestResult.success();
    }
}
