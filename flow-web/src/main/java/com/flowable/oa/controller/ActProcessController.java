package com.flowable.oa.controller;

import com.flowable.oa.core.service.IProcessEngineService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.vo.ProcessDefinitionEntityVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午11:10
 **/
@Slf4j
@Controller
@RequestMapping("/act/process")
public class ActProcessController {

    @Autowired
    private IProcessEngineService processEngineService;

    /**
     * 流程定义列表
     */
    @ResponseBody
    @RequestMapping("list")
    public DataGrid<ProcessDefinitionEntityVo> processList(ProcessDefinitionEntityVo processDefinitionEntityVo) {

        DataGrid<ProcessDefinitionEntityVo> grid = new DataGrid<>();
        PageInfo<ProcessDefinitionEntityVo> pageInfo = processEngineService.processList(processDefinitionEntityVo);
        grid.setRows(pageInfo.getList());
        grid.setTotal(pageInfo.getTotal());
        return grid;
    }

    /**
     * 流程所有任务列表
     */
    @ResponseBody
    @GetMapping("taskList/{processId}")
    public DataGrid<UserTask> processTaskList(@PathVariable("processId") String processId) {

        DataGrid<UserTask> grid = new DataGrid<>();
        grid.setRows(processEngineService.getAllTaskByProcessKey(processId));
        return grid;
    }

    /**
     * 运行中的实例列表
     */
    @ResponseBody
    @RequestMapping("running")
    public DataGrid<ProcessInstance> runningList(PageInfo<ProcessInstance> page, String procInsId, String procDefKey) {

        DataGrid<ProcessInstance> grid = new DataGrid<>();
        PageInfo<ProcessInstance> helper = processEngineService.runningList(page, procInsId, procDefKey);
        grid.setRows(helper.getList());
        grid.setTotal(helper.getTotal());
        return grid;
    }

    /**
     * 读取资源，通过部署ID
     *
     * @param processDefinitionId 流程定义ID
     * @param response
     * @throws Exception
     */
    @RequestMapping("resource/read")
    public void resourceRead(String processDefinitionId, String type, HttpServletResponse response) throws Exception {

        InputStream resourceAsStream = processEngineService.resourceRead(processDefinitionId, type);
        if (type.equals("xml")) {
            response.setContentType("text/plain;charset=utf-8");
        }
        IOUtils.copy(resourceAsStream, response.getOutputStream());
    }

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    @ResponseBody
    @RequestMapping("delete")
    public RestResult<Object> delete(String deploymentId) {
        processEngineService.deleteDeployment(deploymentId);
        return RestResult.success();
    }
}
