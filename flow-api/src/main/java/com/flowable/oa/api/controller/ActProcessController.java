package com.flowable.oa.api.controller;

import com.flowable.oa.core.service.IProcessDefinitionService;
import com.flowable.oa.core.service.IProcessEngineService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.vo.ProcessDefinitionEntityVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RestController
@RequestMapping("/act/process")
public class ActProcessController {

    @Autowired
    private IProcessEngineService processEngineService;

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    /**
     * 流程定义列表
     */
    @RequestMapping("list")
    public RestResult<DataGrid<ProcessDefinitionEntityVo>> processList(ProcessDefinitionEntityVo processDefinitionEntity) {

        DataGrid<ProcessDefinitionEntityVo> grid = new DataGrid<>();
        PageInfo<ProcessDefinitionEntityVo> tempResult = processEngineService.processList(processDefinitionEntity);
        grid.setRows(tempResult.getList());
        grid.setTotal(tempResult.getTotal());
        return RestResult.success(grid);
    }

    /**
     * 流程所有任务列表
     */
    @RequestMapping("processTaskList")
    public RestResult<DataGrid<Map<String, Object>>> processTaskList(@RequestParam Map<String, Object> params) {

        DataGrid<Map<String, Object>> grid = new DataGrid<>();
        String processId = (String) params.get("processId");
        List<Map<String, Object>> result = processEngineService.getAllTaskByProcessKey(processId);
        grid.setRows(result);
        grid.setTotal(result.size());
        return RestResult.success(grid);
    }

    /**
     * 运行中的实例列表
     */
    @RequestMapping("running")
    public RestResult<DataGrid<ProcessInstance>> runningList(PageInfo<ProcessInstance> page, String procInsId, String procDefKey) {

        DataGrid<ProcessInstance> grid = new DataGrid<>();
        PageInfo<ProcessInstance> helper = processEngineService.runningList(page, procInsId, procDefKey);
        grid.setRows(helper.getList());
        grid.setTotal(helper.getTotal());
        return RestResult.success(grid);
    }

    /**
     * 部署流程 - 保存
     *
     * @return
     */
    @PostMapping("/deploy")
    public RestResult<Object> deploy(MultipartFile file) {

        String fileName = file.getOriginalFilename();
        String message;
        if (StringUtils.isBlank(fileName)) {
            message = "请选择要部署的流程文件";
            return RestResult.fail(null, message);
        } else {
            String key = fileName.substring(0, fileName.indexOf('.'));
            ProcessDefinition processDefinition = processDefinitionService.getLatestProcDefByKey(key);
            processEngineService.deploy(null, file);
            processDefinitionService.copyVariables(processDefinition);
        }
        return RestResult.success();
    }

    /**
     * 挂起、激活流程实例
     */
    @RequestMapping("update/{state}")
    public RestResult<Object> updateState(@PathVariable("state") String state, @RequestParam String processDefinitionId) {
        processEngineService.updateState(state, processDefinitionId);
        return RestResult.success();
    }

    /**
     * 将部署的流程转换为模型
     *
     * @param processDefinitionId
     * @return
     */
    @RequestMapping("convert")
    public RestResult<Object> convertToModel(@RequestParam String processDefinitionId) {

        Model modelData = processEngineService.convertToModel(processDefinitionId);
        String message = "转换模型成功，模型ID=" + modelData.getId();
        return RestResult.success(message);
    }

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    @RequestMapping("delete")
    public RestResult<Object> delete(String deploymentId) {
        processEngineService.deleteDeployment(deploymentId);
        return RestResult.success();
    }

    /**
     * 删除流程实例
     *
     * @param procInsId 流程实例ID
     * @param reason    删除原因
     */
    @RequestMapping("deleteProcIns")
    public RestResult<Object> deleteProcIns(String procInsId, String reason) {

        processEngineService.deleteProcIns(procInsId, reason);
        return RestResult.success();
    }

}
