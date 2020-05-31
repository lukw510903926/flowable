package com.flowable.oa.api.controller;

import com.flowable.oa.core.entity.BizTemplateFile;
import com.flowable.oa.core.service.BizTemplateFileService;
import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.util.WebUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
@RestController
@RequestMapping("/bizTemplateFile")
public class BizTemplateFileController {

    @Autowired
    private BizTemplateFileService bizTemplateFileService;

    @GetMapping("/list")
    public RestResult<Map<String, Object>> list(PageInfo<BizTemplateFile> page, BizTemplateFile file) {

        PageInfo<BizTemplateFile> helper = this.bizTemplateFileService.findByModel(page, file, true);
        Map<String, Object> data = new HashMap<>();
        data.put("total", helper.getTotal());
        data.put("rows", helper.getList());
        return RestResult.success(data);
    }

    @PostMapping("/upload")
    public RestResult<String> upload(@RequestParam MultipartFile file, HttpServletRequest request) {

        String flowName = request.getParameter("flowName");
        BizTemplateFile bizTemplateFile = new BizTemplateFile();
        bizTemplateFile.setCreateUser(WebUtil.getLoginUsername());
        bizTemplateFile.setFlowName(flowName);
        bizTemplateFileService.saveOrUpdate(bizTemplateFile, file);
        return RestResult.success();
    }

    @PostMapping("/download")
    public void downloadTemplate(@RequestParam Map<String, String> params, HttpServletResponse response) {

        try (OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/octet-stream;charset=UTF-8");
            BizTemplateFile templateFile = bizTemplateFileService.getBizTemplateFile(params);
            if (templateFile != null) {
                response.setHeader("Content-Disposition", "attachment;");
                File inputFile = new File(templateFile.getFilePath());
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

    @PostMapping("/remove")
    public RestResult<Object> remove(@RequestParam List<Serializable> ids) {

        bizTemplateFileService.deleteByIds(ids);
        return RestResult.success();
    }
}
