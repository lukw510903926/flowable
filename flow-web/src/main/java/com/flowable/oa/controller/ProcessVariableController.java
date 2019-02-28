package com.flowable.oa.controller;

import com.flowable.oa.core.entity.ProcessVariable;
import com.flowable.oa.core.service.IProcessVariableService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午11:09
 **/
@Controller
@RequestMapping("/processModelMgr")
public class ProcessVariableController {

    @Autowired
    private IProcessVariableService processValService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 得到流程全局变量列表 / 任务变量列表
     *
     * @param params
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("processValList")
    public DataGrid<ProcessVariable> processValList(@RequestParam Map<String, Object> params, PageInfo<ProcessVariable> page) {

        DataGrid<ProcessVariable> grid = new DataGrid<>();
        String processId = MapUtils.getString(params, "processId");
        String taskId = MapUtils.getString(params, "taskId");
        ProcessVariable variable = new ProcessVariable();
        variable.setProcessDefinitionId(processId);
        variable.setTaskId(taskId);
        PageInfo<ProcessVariable> processValBeans = this.processValService.findProcessVariables(variable, page);
        grid.setRows(processValBeans.getList());
        grid.setTotal(processValBeans.getTotal());
        return grid;
    }

    /**
     * 根据全局流程变量ID得到变量详情
     *
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping("getProcessValById")
    public RestResult<Object> getProcessValById(@RequestParam Map<String, Object> params) {

        logger.info("根据全局流程变量ID得到变量详情---getProcessValById");
        String processId = (String) params.get("processId");
        String taskId = (String) params.get("taskId");
        ProcessVariable processValAbs;
        if (StringUtils.isBlank(taskId)) {
            processValAbs = processValService.selectByKey(processId);
        } else {
            processValAbs = processValService.selectByKey(taskId);
        }
        return RestResult.success(processValAbs);
    }

    /**
     * 根据全局流程变量IDs删除变量详情
     *
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteProcessValById")
    public RestResult<Object> deleteProcessValById(@RequestParam Map<String, Object> params) {

        String ids = (String) params.get("valIds");
        String[] valIds = StringUtils.isNotBlank(ids) ? ids.split(",") : new String[]{};
        processValService.deleteVariable(Arrays.asList(valIds));
        return RestResult.success();
    }

    /**
     * 保存或者更新流程全局变量
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("saveOrUpdate")
    public RestResult<Object> saveOrUpdateProcessVal(ProcessVariable processValAbs) {

        processValService.saveOrUpdate(processValAbs);
        return RestResult.success();
    }
}
