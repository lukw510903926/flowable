package com.flowable.oa.api.controller;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.ProcessVariable;
import com.flowable.oa.core.service.IBizInfoService;
import com.flowable.oa.core.service.IProcessDefinitionService;
import com.flowable.oa.core.service.IProcessExecuteService;
import com.flowable.oa.core.service.IProcessVariableService;
import com.flowable.oa.core.util.Constants;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.PageUtil;
import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.util.WebUtil;
import com.flowable.oa.core.vo.BizInfoVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
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
@RestController
@RequestMapping("/workflow")
public class ProcessExecuteController {

    @Resource
    private IProcessExecuteService processExecuteService;

    @Resource
    private IProcessDefinitionService processDefinitionService;

    @Resource
    private IBizInfoService bizInfoService;

    @Resource
    private IProcessVariableService processVariableService;

    @GetMapping("/loadWorkLogInput")
    public RestResult<Map<String, Object>> loadWorkLogInput(Long logId) {
        return RestResult.success(processExecuteService.loadBizLogInput(logId));
    }

    /**
     * target取值如下<br>
     * myComplete : 待办工单<br>
     * myClaim : 待签任务<br>
     * query : 全局查询<br>
     * myCreate : 我创建的单<br>
     * myHandle : 我处理过的单<br>
     * myClose : 我创建并关闭的单
     *
     * @return
     */
    @GetMapping("/queryWorkOrder")
    public RestResult<DataGrid<BizInfo>> queryWorkOrder(@RequestBody BizInfoVo bizInfoVo) {

        PageInfo<BizInfo> helper = bizInfoService.findBizInfo(bizInfoVo, PageUtil.getPage(bizInfoVo));
        DataGrid<BizInfo> grid = new DataGrid<>();
        grid.setRows(helper.getList());
        grid.setTotal(helper.getTotal());
        return RestResult.success(grid);
    }

    /**
     * 创建工单
     *
     * @return
     */

    @PostMapping("/create/{key}")
    public RestResult<Map<String, Object>> create(@PathVariable("key") String key) {

        Map<String, Object> data = new HashMap<>();
        ProcessDefinition processDefinition = processDefinitionService.getLatestProcDefByKey(key);
        if (processDefinition != null) {
            String proDefId = processDefinition.getId();
            data.put("baseTempId", proDefId);
            ProcessVariable variable = new ProcessVariable();
            variable.setProcessDefinitionId(proDefId);
            variable.setTaskId(Constants.TASK_START);
            List<ProcessVariable> list = this.processVariableService.select(variable);
            data.put(Constants.SYS_BUTTON, processExecuteService.loadStartButtons(proDefId));
            data.put("processValBean", list);
            return RestResult.success(data);
        }
        return RestResult.fail(data, "流程【" + key + "】未找到!");
    }

    /**
     * 创建工单
     *
     * @param params
     * @param request
     * @return
     */
    @PostMapping("bizInfo/create")
    public RestResult<String> createBiz(@RequestParam Map<String, Object> params, MultipartHttpServletRequest request) {

        WebUtil.getLoginUser(request);
        boolean startProc = MapUtils.getBoolean(params, "startProc", false);
        BizInfo bean = processExecuteService.createBizDraft(params, request.getMultiFileMap(), startProc);
        String msg = "/biz/" + bean.getId();
        if (!startProc) {
            msg = "/biz/list/myWork";
        }
        return RestResult.success(msg);
    }

    /**
     * 工单处理，自动处理工单的各种状态的提交
     *
     * @param params
     * @param request
     * @return
     */
    @PostMapping("/submit")
    public RestResult<String> submit(@RequestParam Map<String, Object> params, MultipartHttpServletRequest request) {

        WebUtil.getLoginUser(request);
        processExecuteService.submit(params, request.getMultiFileMap());
        return RestResult.success();
    }


    @PostMapping("/bizInfo/delete")
    public RestResult<Object> deleteBizInfo(@RequestParam List<Serializable> ids) {

        bizInfoService.deleteByIds(ids);
        return RestResult.success();
    }
}
