package com.flowable.oa.controller;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flowable.oa.util.*;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.entity.ProcessVariable;
import com.flowable.oa.service.IBizInfoService;
import com.flowable.oa.service.IProcessDefinitionService;
import com.flowable.oa.service.IProcessExecuteService;
import com.flowable.oa.service.IProcessVariableService;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午11:10
 **/
@Controller
@RequestMapping("/workflow")
public class ProcessExecuteController {

    @Autowired
    private IProcessExecuteService processExecuteService;

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    @Autowired
    private IBizInfoService bizInfoService;

    @Autowired
    private IProcessVariableService processVariableService;

    private Logger logger = LoggerFactory.getLogger("processExecuteController");

    @ResponseBody
    @RequestMapping(value = "/loadWorkLogInput")
    public Map<String, Object> loadWorkLogInput(String logId) {
        return processExecuteService.loadBizLogInput(logId);
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
    @ResponseBody
    @RequestMapping(value = "/queryWorkOrder")
    public DataGrid<BizInfo> queryWorkOrder(@RequestParam Map<String, Object> params, PageInfo<BizInfo> page) {

        WebUtil.getLoginUser();
        PageInfo<BizInfo> helper = bizInfoService.findBizInfo(params, page);
        DataGrid<BizInfo> grid = new DataGrid<>();
        grid.setRows(helper.getList());
        grid.setTotal(helper.getTotal());
        return grid;
    }

    /**
     * 创建工单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create/{key}", method = RequestMethod.GET)
    public Map<String, Object> create(@PathVariable("key") String key, HttpServletRequest request) {

        Map<String, Object> data = new HashMap<>();
        WebUtil.getLoginUser(request);
        ProcessDefinition processDefinition = processDefinitionService.getLatestProcDefByKey(key);
        if (processDefinition != null) {
            String proDefId = processDefinition.getId();
            data.put("baseTempId", proDefId);
            ProcessVariable variable = new ProcessVariable();
            variable.setProcessDefinitionId(proDefId);
            variable.setTaskId(Constants.TASK_START);
            List<ProcessVariable> list = this.processVariableService.findProcessVariables(variable);
            data.put("SYS_BUTTON", processExecuteService.loadStartButtons(proDefId));
            data.put("processValBean", list);
            data.put("result", true);
        } else {
            data.put("result", false);
            data.put("msg", "流程【" + key + "】未找到!");
        }
        return data;
    }

    /**
     * 显示某个工单信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/display/{id}")
    public Map<String, Object> display(@PathVariable("id") String id) {

        return processExecuteService.queryWorkOrder(id);
    }

    /**
     * 创建工单
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "bizInfo/create")
    public ResponseEntity<String> createBiz(@RequestParam Map<String, Object> params, MultipartHttpServletRequest request) {

        WebUtil.getLoginUser(request);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.TEXT_PLAIN);
        Boolean startProc = MapUtils.getBoolean(params, "startProc");
        BizInfo bean = processExecuteService.createBizDraft(params, request.getMultiFileMap(), startProc);
        String msg = "/biz/" + bean.getId();
        if (!startProc) {
            msg = "/biz/list/myWork";
        }
        return new ResponseEntity<>(JSONObject.toJSONString(RestResult.success(msg)), header, HttpStatus.OK);
    }

    /**
     * 重新提交
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "bizInfo/updateBiz")
    public ResponseEntity<String> updateBiz(@RequestParam Map<String, Object> params, MultipartHttpServletRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        WebUtil.getLoginUser(request);
        processExecuteService.updateBiz(params, request.getMultiFileMap());
        return new ResponseEntity<>(JSONObject.toJSONString(RestResult.success()), headers, HttpStatus.OK);
    }

    /**
     * 工单处理，自动处理工单的各种状态的提交
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/submit")
    public ResponseEntity<String> submit(@RequestParam Map<String, Object> params, MultipartHttpServletRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        WebUtil.getLoginUser(request);
        processExecuteService.submit(params, request.getMultiFileMap());
        return new ResponseEntity<>(JSONObject.toJSONString(RestResult.success()), headers, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/bizInfo/delete")
    public RestResult<Object> deleteBizInfo(@RequestParam List<String> ids) {

        bizInfoService.deleteByIds(ids);
        return RestResult.success();
    }

    @ResponseBody
    @RequestMapping(value = "/download")
    public void downloadFile(String action, String id, HttpServletResponse response) {

        Object[] result = processExecuteService.downloadFile(action, id);
        if (result[1] == null) {
            return;
        }
        InputStream inputStream = (InputStream) result[1];
        String fileType = (String) result[0];
        String fileName = (String) result[3];
        try {
            if ("IMAGE".equalsIgnoreCase(fileType)) {
                response.setContentType("image/PNG;charset=GB2312");
            } else {
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                response.setContentType("application/x-download");
                response.setHeader("Content-disposition", "attachment; filename=" + fileName);
                IOUtils.copy(inputStream, response.getOutputStream());
            }
        } catch (Exception e) {
            logger.error("文件下载失败 : {}", e);
        }
    }
}
