package com.flowable.oa.controller;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flowable.oa.util.Constants;
import com.flowable.oa.util.LoginUser;
import com.flowable.oa.util.WebUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.flowable.oa.entity.BizFile;
import com.flowable.oa.service.IBizFileService;
import com.flowable.oa.service.IProcessExecuteService;
import com.flowable.oa.service.act.ActProcessService;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午10:56
 **/
@Controller
@RequestMapping
public class BizInfoController {

    @Autowired
    private ActProcessService actProcessService;

    @Autowired
    private IProcessExecuteService processExecuteService;

    @Autowired
    private IBizFileService bizFileService;

    @Value("${biz.file.path}")
    private String bizFileRootPath;

    private Logger logger = LoggerFactory.getLogger(BizInfoController.class);

    @ResponseBody
    @RequestMapping("/biz/process/status")
    public List<String> getProcessStatus(ProcessDefinitionEntityImpl processDefinition) {

        List<ProcessDefinition> processList = actProcessService.findProcessDefinition(null);
        return this.getProcessStatus(processList, processDefinition);
    }

    /**
     * 工单管理视图
     *
     * @return
     */
    @RequestMapping(value = "biz/list/{action}")
    public String queryView(@PathVariable("action") String action, Model model) {

        model.addAttribute("action", action);
        List<String> processList = new ArrayList<>();
        List<String> status = new ArrayList<>();
        List<ProcessDefinition> processDefinitionList = actProcessService.findProcessDefinition(null);
        if (CollectionUtils.isNotEmpty(processDefinitionList)) {
            processDefinitionList.forEach(processDefinition -> processList.add(processDefinition.getName()));
        }
        getProcessStatus(processDefinitionList, new ProcessDefinitionEntityImpl());
        model.addAttribute("statusList", JSONObject.toJSON(status));
        model.addAttribute("processList", JSONObject.toJSON(processList));
        return "modules/biz/biz_list";
    }

    private List<String> getProcessStatus(List<ProcessDefinition> list, ProcessDefinition processDefinition) {

        List<String> status = new ArrayList<>();
        status.add(Constants.BIZ_TEMP);
        status.add(Constants.BIZ_NEW);
        Set<String> sets = new HashSet<>();
        if (StringUtils.isBlank(processDefinition.getName())) {
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(entity -> sets.addAll(actProcessService.loadProcessStatus(entity.getId())));
            }
            status.addAll(sets);
            status.add(Constants.BIZ_END);
        } else {
            list = actProcessService.findProcessDefinition(processDefinition);
            if (CollectionUtils.isNotEmpty(list)) {
                sets.addAll(this.actProcessService.loadProcessStatus(list.get(0).getId()));
            }
        }
        status.add(Constants.BIZ_END);
        return status;
    }

    /**
     * 工单管理视图
     *
     * @return
     */
    @RequestMapping(value = "biz/create/{key}")
    public String createView(@PathVariable String key, String bizId, Model model) {

        model.addAttribute("key", key);
        model.addAttribute("createUser", WebUtil.getLoginUser());
        model.addAttribute("bizId", bizId);
        return "modules/biz/biz_create";
    }

    @RequestMapping(value = "biz/{id}", method = RequestMethod.GET)
    public String detailView(@PathVariable String id, Model model, HttpServletRequest request) {

        model.addAttribute("id", id);
        LoginUser createUser = WebUtil.getLoginUser(request);
        model.addAttribute("currentUser", JSONObject.toJSON(createUser));
        return "modules/biz/biz_detail";
    }

    @RequestMapping(value = "bizInfo/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable String id, Model model) {

        model.addAttribute("id", id);
        model.addAttribute("currentUser", JSONObject.toJSON(WebUtil.getLoginUser()));
        return "modules/biz/bizInfo_detail";
    }

    @ResponseBody
    @RequestMapping("biz/download")
    public void download(String id, HttpServletResponse response) {
        try {
            BizFile bizFile = bizFileService.selectByKey(id);
            response.setContentType("application/octet-stream;charset=UTF-8");
            File file = new File(bizFileRootPath + bizFile.getPath());
            if (file.exists()) {
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String(bizFile.getName().getBytes("gb2312"), StandardCharsets.ISO_8859_1));
                FileUtils.copyFile(file, response.getOutputStream());
            } else {
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("文件不存在".getBytes("gb2312"), StandardCharsets.ISO_8859_1));
            }
        } catch (Exception e) {
            logger.error(" 下载失败 : {}", e);
        }
    }

    @ResponseBody
    @RequestMapping("/biz/getBizFile")
    public List<BizFile> getBizFile(@RequestBody BizFile bizFile) {

        return this.bizFileService.findBizFile(bizFile);
    }

    @ResponseBody
    @RequestMapping(value = "biz/workInfo/{bizId}")
    public Map<String, Object> getDraftBiz(@PathVariable("bizId") String bizId, HttpServletRequest request) {
        WebUtil.getLoginUser(request);
        return processExecuteService.queryWorkOrder(bizId);
    }
}
