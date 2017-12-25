package com.flowable.web.controller;

import com.flowable.common.utils.DataGrid;
import com.flowable.common.utils.Json;
import com.flowable.common.utils.PageHelper;
import com.flowable.common.utils.ReflectionUtils;
import com.flowable.core.service.IProcessDefinitionService;
import com.flowable.core.service.act.ActProcessService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.impl.persistence.entity.DeploymentEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程定义相关Controller
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
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
    @RequestMapping(value = "list")
    public DataGrid processList(PageHelper<Object[]> page, @RequestParam Map<String, Object> params) {

        DataGrid grid = new DataGrid();
        try {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            page = actProcessService.processList(page, null);
            List<Object[]> tempResult = page.getList();
            for (Object[] objects : tempResult) {
                ProcessDefinitionEntity process = (ProcessDefinitionEntity) objects[0];
                DeploymentEntity deployment = (DeploymentEntity) objects[1];
                Map<String, Object> item = ReflectionUtils.beanToMap(process);
                item.put("deploymentTime", deployment.getDeploymentTime());
                result.add(item);
            }
            grid.setRows(result);
            grid.setTotal(page.getCount());
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
    public DataGrid processTaskList(@RequestParam Map<String, Object> params) {

        DataGrid grid = new DataGrid();
        try {
            String processId = (String) params.get("processId");
            List<Map<String, Object>> result = actProcessService.getAllTaskByProcessKey(processId);
            grid.setRows(result);
            grid.setTotal((long) result.size());
        } catch (Exception e) {
            logger.error("流程所有任务列表失败 :{}", e);
        }
        return grid;
    }

    /**
     * 运行中的实例列表
     */
    @ResponseBody
    @RequestMapping(value = "running")
    public DataGrid runningList(PageHelper<ProcessInstance> page, String procInsId, String procDefKey) {

        DataGrid grid = new DataGrid();
        try {
            PageHelper<ProcessInstance> helper = actProcessService.runningList(page, procInsId, procDefKey);
            grid.setRows(helper.getList());
            grid.setTotal(helper.getCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grid;
    }

    /**
     * 读取资源，通过部署ID
     *
     * @param processDefinitionId 流程定义ID
     * @param processInstanceId   流程实例ID
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "resource/read")
    public void resourceRead(String processDefinitionId, String processInstanceId, String type,
                             HttpServletResponse response) throws Exception {
        InputStream resourceAsStream = actProcessService.resourceRead(processDefinitionId, processInstanceId, type);
        if (type.equals("image")) {
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            LineIterator it = IOUtils.lineIterator(resourceAsStream, "utf-8");
            while (it.hasNext()) {
                try {
                    String line = it.nextLine();
                    stringBuffer.append(line);
                    stringBuffer.append("\n");
                } catch (Exception e) {
                    logger.error("读取资源失败 : {}", e);
                }
            }
            response.setContentType("text/plain;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println(stringBuffer.toString());
            out.close();
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
        String message = null;
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
        return "process/process_deploy";
    }

    /**
     * 挂起、激活流程实例
     */
    @ResponseBody
    @RequestMapping(value = "update/{state}")
    public Json updateState(@PathVariable("state") String state, @RequestParam String processDefinitionId) {
        Json json = new Json();
        try {
            String message = actProcessService.updateState(state, processDefinitionId);
            json.setSuccess(true);
            json.setMsg(message);
        } catch (Exception e) {
            logger.error("流程挂起,激活失败 : {}", e);
            json.setSuccess(false);
            json.setMsg("操作失败!");
        }
        return json;
    }

    /**
     * 将部署的流程转换为模型
     *
     * @param processDefinitionId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "convert")
    public Json convertToModel(@RequestParam String processDefinitionId) {

        Json json = new Json();
        try {
            org.flowable.engine.repository.Model modelData = actProcessService.convertToModel(processDefinitionId);
            String message = "转换模型成功，模型ID=" + modelData.getId();
            json.setSuccess(true);
            json.setMsg(message);
        } catch (Exception e) {
            logger.error("模型转换失败 : {}", e);
            json.setSuccess(false);
            json.setMsg("操作失败!");
        }
        return json;
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
    public Json delete(String deploymentId) {
        Json json = new Json();
        try {
            actProcessService.deleteDeployment(deploymentId);
            json.setSuccess(true);
            json.setMsg("删除成功--" + deploymentId);
        } catch (Exception e) {
            logger.error("流程删除失败 : {}", e);
            json.setSuccess(false);
            json.setMsg("操作失败!");
        }
        return json;
    }

    /**
     * 删除流程实例
     *
     * @param procInsId 流程实例ID
     * @param reason    删除原因
     */
    @ResponseBody
    @RequestMapping(value = "deleteProcIns")
    public Json deleteProcIns(String procInsId, String reason) {

        Json json = new Json();
        try {
            actProcessService.deleteProcIns(procInsId, reason);
            json.setSuccess(true);
            json.setMsg("操作成功");
        } catch (Exception e) {
            logger.error("实例删除失败 : {}", e);
            json.setSuccess(false);
            json.setMsg("操作失败!");
        }
        return json;
    }

}
