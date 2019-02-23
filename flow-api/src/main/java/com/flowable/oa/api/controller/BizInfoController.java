package com.flowable.oa.api.controller;

import com.flowable.oa.core.entity.BizFile;
import com.flowable.oa.core.service.IBizFileService;
import com.flowable.oa.core.service.IProcessExecuteService;
import com.flowable.oa.core.service.act.ActProcessService;
import com.flowable.oa.core.util.Constants;
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
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
    public Map<String, Object> getDraftBiz(@PathVariable("bizId") String bizId) {
        return processExecuteService.queryWorkOrder(bizId);
    }
}
