package com.flowable.oa.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.flowable.oa.util.ReflectionUtils;
import com.flowable.oa.util.RestResult;
import com.github.pagehelper.PageInfo;
import com.flowable.oa.util.DataGrid;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.flowable.oa.service.IProcessDefinitionService;
import com.flowable.oa.service.act.ActProcessService;

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
    public DataGrid<ProcessInstance> runningList(PageInfo<ProcessInstance> page, String procInsId, String procDefKey) {

        DataGrid<ProcessInstance> grid = new DataGrid<>();
        try {
            PageInfo<ProcessInstance> helper = actProcessService.runningList(page, procInsId, procDefKey);
            grid.setRows(helper.getList());
            grid.setTotal(helper.getTotal());
        } catch (Exception e) {
            logger.error("获取运行中的实例列表失败 : {}", e);
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
    public void resourceRead(String processDefinitionId, String processInstanceId, String type, HttpServletResponse response) throws Exception {

        InputStream resourceAsStream = actProcessService.resourceRead(processDefinitionId, processInstanceId, type);
        if (type.equals("image")) {
            byte[] b = new byte[1024];
            int len;
            while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        } else {
            StringBuilder builder = new StringBuilder();
            LineIterator it = IOUtils.lineIterator(resourceAsStream, "utf-8");
            while (it.hasNext()) {
                try {
                    String line = it.nextLine();
                    builder.append(line);
                    builder.append("\n");
                } catch (Exception e) {
                    logger.error("读取资源失败 : {}", e);
                }
            }
            response.setContentType("text/plain;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println(builder.toString());
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
