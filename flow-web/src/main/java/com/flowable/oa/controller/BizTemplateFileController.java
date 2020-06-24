package com.flowable.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.flowable.oa.core.entity.BizTemplateFile;
import com.flowable.oa.core.service.BizTemplateFileService;
import com.flowable.oa.core.service.IProcessEngineService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.util.WebUtil;
import com.flowable.oa.core.vo.ProcessDefinitionEntityVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
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
@Controller
@RequestMapping("/bizTemplateFile")
public class BizTemplateFileController {

    @Autowired
    private BizTemplateFileService bizTemplateFileService;

    @Autowired
    private IProcessEngineService processEngineService;

    @RequestMapping("/index")
    public String index(Model model) {

        PageInfo<ProcessDefinitionEntityVo> tempResult = processEngineService.processList(new ProcessDefinitionEntityVo());
        model.addAttribute("processList", tempResult.getList());
        return "modules/template/bizTemplateFileList";
    }

    @ResponseBody
    @RequestMapping("/list")
    public DataGrid<BizTemplateFile> list(PageInfo<BizTemplateFile> page, BizTemplateFile file) {

        PageInfo<BizTemplateFile> helper = bizTemplateFileService.findByModel(page, file, true);
        return DataGrid.getGrid(helper.getList(),helper.getTotal());
    }

    @ResponseBody
    @RequestMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file, HttpServletRequest request) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        String flowName = request.getParameter("flowName");
        BizTemplateFile bizTemplateFile = new BizTemplateFile();
        bizTemplateFile.setCreateUser(WebUtil.getLoginUsername());
        bizTemplateFile.setFlowName(flowName);
        bizTemplateFileService.saveOrUpdate(bizTemplateFile, file);
        return new ResponseEntity<>(JSONObject.toJSONString(RestResult.success()), responseHeaders, HttpStatus.OK);
    }

    @PostMapping("/download")
    public void downloadTemplate(@RequestBody BizTemplateFile templateFile, HttpServletResponse response) {

        try (OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/octet-stream;charset=UTF-8");
            BizTemplateFile bizTemplateFile = bizTemplateFileService.getBizTemplateFile(templateFile);
            if (bizTemplateFile != null) {
                response.setHeader("Content-Disposition", "attachment;");
                File inputFile = new File(bizTemplateFile.getFilePath());
                if (inputFile.exists() && inputFile.isFile()) {
                    FileUtils.copyFile(inputFile, outputStream);
                } else {
                    FileUtils.copyFile(File.createTempFile("文件不存在!", ".txt"), outputStream);
                }
            } else {
                FileUtils.copyFile(File.createTempFile("文件不存在!", ".txt"), outputStream);
            }
        } catch (Exception e) {
            log.error("文件不存在 !", e);
        }
    }

    @ResponseBody
    @RequestMapping("/remove")
    public RestResult<Object> remove(@RequestParam List<Serializable> ids) {

        bizTemplateFileService.deleteByIds(ids);
        return RestResult.success();
    }
}
