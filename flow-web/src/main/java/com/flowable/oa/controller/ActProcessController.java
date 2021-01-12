package com.flowable.oa.controller;

import com.flowable.oa.core.service.IProcessDefinitionService;
import com.flowable.oa.core.service.IProcessEngineService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.vo.ProcessDefinitionEntityVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private IProcessDefinitionService processDefinitionService;

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
        List<UserTask> userTasks = processEngineService.getAllTaskByProcessKey(processId);
        grid.setRows(userTasks);
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
     * 部署流程 - 保存
     *
     * @return
     */
    @PostMapping("/deploy")
    public String deploy(MultipartHttpServletRequest request, Model model) {

        MultipartFile file = request.getFile("file");
        String fileName = file.getOriginalFilename();
        boolean result = false;
        String message;
        if (StringUtils.isBlank(fileName)) {
            message = "请选择要部署的流程文件";
        } else {
            String key = fileName.substring(0, fileName.indexOf("."));
            ProcessDefinition processDefinition = processDefinitionService.getLatestProcDefByKey(key);
            message = processEngineService.deploy(null, file);
            processDefinitionService.copyVariables(processDefinition);
            result = true;
        }
        model.addAttribute("result", result);
        model.addAttribute("message", message);
        return "modules/process/process_deploy";
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
