package com.flowable.oa.controller;

import com.flowable.oa.core.service.IProcessDefinitionService;
import com.flowable.oa.core.service.act.ActProcessService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.ReflectionUtils;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.impl.persistence.entity.DeploymentEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
@RequestMapping(value = "/act/process")
public class ActProcessController {

    @Autowired
    private ActProcessService actProcessService;

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    private Logger logger = LoggerFactory.getLogger(ActProcessController.class);

    /**
     * 流程定义列表
     */
    @ResponseBody
    @RequestMapping("list")
    public DataGrid<Map<String, Object>> processList(@RequestParam Map<String, Object> params) {

        DataGrid<Map<String, Object>> grid = new DataGrid<>();
        try {
            Integer pageNum = MapUtils.getInteger(params, "page", 1);
            Integer rows = MapUtils.getInteger(params, "rows", 20);
            List<Map<String, Object>> result = new ArrayList<>();
            List<Object[]> tempResult = actProcessService.processList();
            if (CollectionUtils.isNotEmpty(tempResult)) {
                List<Object[]> list = tempResult.stream().skip((pageNum - 1) * rows).limit(rows).collect(Collectors.toList());
                for (Object[] objects : list) {
                    ProcessDefinitionEntityImpl process = (ProcessDefinitionEntityImpl) objects[0];
                    DeploymentEntity deployment = (DeploymentEntity) objects[1];
                    Map<String, Object> item = ReflectionUtils.beanToMap(process);
                    item.put("deploymentTime", deployment.getDeploymentTime());
                    result.add(item);
                }
                grid.setRows(result);
                grid.setTotal(tempResult.size());
            }

        } catch (Exception e) {
            logger.error(" 流程列表获取失败 : {}", e);
        }
        return grid;
    }

    /**
     * 流程所有任务列表
     */
    @ResponseBody
    @RequestMapping(value = "processTaskList")
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
    @RequestMapping(value = "running")
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
    @RequestMapping(value = "resource/read")
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
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    public String deploy(MultipartHttpServletRequest request, Model model) throws Exception {

        MultipartFile file = request.getFile("file");
        String fileName = file.getOriginalFilename();
        String exportDir = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/deployments/";
        boolean result = false;
        String message;
        if (StringUtils.isBlank(fileName)) {
            message = "请选择要部署的流程文件";
        } else {
            String key = fileName.substring(0, fileName.indexOf("."));
            ProcessDefinition processDefinition = processDefinitionService.getLatestProcDefByKey(key);
            message = actProcessService.deploy(exportDir, null, file);
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
    @RequestMapping(value = "update/{state}")
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
    @RequestMapping(value = "convert")
    public RestResult<Object> convertToModel(@RequestParam String processDefinitionId) {

        try {
            org.flowable.engine.repository.Model modelData = actProcessService.convertToModel(processDefinitionId);
            String message = "转换模型成功，模型ID=" + modelData.getId();
            return RestResult.success(message);
        } catch (Exception e) {
            logger.error("模型转换失败 : {}", e);
            return RestResult.fail(null, "模型转换失败");
        }
    }

    /**
     * 导出图片文件到硬盘
     */
    @ResponseBody
    @RequestMapping(value = "export/diagrams")
    public List<String> exportDiagrams(String exportDir) {

        try {
            return actProcessService.exportDiagrams(exportDir);
        } catch (IOException e) {
            logger.error("图片导出失败");
        }
        return null;
    }

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    @ResponseBody
    @RequestMapping(value = "delete")
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
    @RequestMapping(value = "deleteProcIns")
    public RestResult<Object> deleteProcIns(String procInsId, String reason) {

        actProcessService.deleteProcIns(procInsId, reason);
        return RestResult.success();
    }

}
