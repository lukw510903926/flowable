package com.flowable.oa.api.controller;

import com.flowable.oa.core.service.IProcessDefinitionService;
import com.flowable.oa.core.service.act.ActProcessService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.vo.ProcessDefinitionEntityVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午11:10
 **/
@Controller
@RequestMapping("/act/process")
public class ActProcessController {

    @Autowired
    private ActProcessService actProcessService;

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    /**
     * 流程定义列表
     */
    @ResponseBody
    @RequestMapping("list")
    public DataGrid<ProcessDefinitionEntityVo> processList(@RequestParam Map<String, Object> params) {

        DataGrid<ProcessDefinitionEntityVo> grid = new DataGrid<>();
        Integer pageNum = MapUtils.getInteger(params, "page", 1);
        Integer rows = MapUtils.getInteger(params, "rows", 20);
        List<ProcessDefinitionEntityVo> tempResult = actProcessService.processList();
        if (CollectionUtils.isNotEmpty(tempResult)) {
            List<ProcessDefinitionEntityVo> list = tempResult.stream().skip((pageNum - 1) * rows).limit(rows).collect(Collectors.toList());
            grid.setRows(list);
            grid.setTotal(tempResult.size());
        }
        return grid;
    }

    /**
     * 流程所有任务列表
     */
    @ResponseBody
    @RequestMapping("processTaskList")
    public DataGrid<Map<String, Object>> processTaskList(@RequestParam Map<String, Object> params) {

        DataGrid<Map<String, Object>> grid = new DataGrid<>();
        String processId = (String) params.get("processId");
        List<Map<String, Object>> result = actProcessService.getAllTaskByProcessKey(processId);
        grid.setRows(result);
        grid.setTotal((long) result.size());
        return grid;
    }

    /**
     * 运行中的实例列表
     */
    @ResponseBody
    @RequestMapping("running")
    public DataGrid<ProcessInstance> runningList(PageInfo<ProcessInstance> page, String procInsId, String procDefKey) {

        DataGrid<ProcessInstance> grid = new DataGrid<>();
        PageInfo<ProcessInstance> helper = actProcessService.runningList(page, procInsId, procDefKey);
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

        InputStream resourceAsStream = actProcessService.resourceRead(processDefinitionId, type);
        if (resourceAsStream != null) {
            response.setHeader("Content-Disposition", "attachment;filename=" + type);
            IOUtils.copyLarge(resourceAsStream, response.getOutputStream());
        } else {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String("文件不存在".getBytes("gb2312"), StandardCharsets.ISO_8859_1));
            File file = File.createTempFile("FLOW_" + type, type);
            FileUtils.copyFile(file, response.getOutputStream());
        }
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
            message = actProcessService.deploy( null, file);
            processDefinitionService.copyVariables(processDefinition);
            result = true;
        }
        model.addAttribute("result", result);
        model.addAttribute("message", message);
        return "modules/process/process_deploy";
    }

    /**
     * 挂起、激活流程实例
     */
    @ResponseBody
    @RequestMapping("update/{state}")
    public RestResult<Object> updateState(@PathVariable("state") String state, @RequestParam String processDefinitionId) {
        actProcessService.updateState(state, processDefinitionId);
        return RestResult.success();
    }

    /**
     * 将部署的流程转换为模型
     *
     * @param processDefinitionId
     * @return
     */
    @ResponseBody
    @RequestMapping("convert")
    public RestResult<Object> convertToModel(@RequestParam String processDefinitionId) {

        org.flowable.engine.repository.Model modelData = actProcessService.convertToModel(processDefinitionId);
        String message = "转换模型成功，模型ID=" + modelData.getId();
        return RestResult.success(message);
    }

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    @ResponseBody
    @RequestMapping("delete")
    public RestResult<Object> delete(String deploymentId) {
        actProcessService.deleteDeployment(deploymentId);
        return RestResult.success();
    }

    /**
     * 删除流程实例
     *
     * @param procInsId 流程实例ID
     * @param reason    删除原因
     */
    @ResponseBody
    @RequestMapping("deleteProcIns")
    public RestResult<Object> deleteProcIns(String procInsId, String reason) {

        actProcessService.deleteProcIns(procInsId, reason);
        return RestResult.success();
    }

}
