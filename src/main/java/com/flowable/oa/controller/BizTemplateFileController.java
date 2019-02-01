package com.flowable.oa.controller;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;
import com.flowable.oa.util.Json;
import com.flowable.oa.util.LoginUser;
import com.flowable.oa.util.WebUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.flowable.oa.entity.BizTemplateFile;
import com.flowable.oa.service.BizTemplateFileService;

@Controller
@RequestMapping("/bizTemplateFile")
public class BizTemplateFileController {

    @Autowired
    private Environment environment;

    @Autowired
    private BizTemplateFileService bizTemplateFileService;

    private Logger logger = LoggerFactory.getLogger("bizTemplateFileController");

    @RequestMapping("/index")
    public String index() {

        return "modules/process/config/bizTemplateFileList";
    }

    @ResponseBody
    @RequestMapping("/list")
    public Map<String, Object> list(PageInfo<BizTemplateFile> page, BizTemplateFile file) {

        PageInfo<BizTemplateFile> helper = bizTemplateFileService.findTemplateFlies(page, file, true);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", helper.getTotal());
        data.put("rows", helper.getList());
        return data;
    }

    @ResponseBody
    @RequestMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file, HttpServletRequest request) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        try {
            LoginUser loginUser = WebUtil.getLoginUser(request);
            String username = loginUser.getUsername();
            String flowName = request.getParameter("flowName");
            BizTemplateFile bizTemplateFile = new BizTemplateFile();
            bizTemplateFile.setCreateUser(username);
            bizTemplateFile.setFlowName(flowName);
            bizTemplateFileService.saveOrUpdate(bizTemplateFile, file);
        } catch (Exception e) {
            logger.error("上传失败 : ", e);
            return new ResponseEntity<>(JSONObject.toJSONString(Json.fail("上传失败")), responseHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>(JSONObject.toJSONString(Json.success()), responseHeaders, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/downloadTemplate")
    public void downloadTemplate(@RequestParam Map<String, String> params, HttpServletResponse response) {

        try {
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/octet-stream;charset=UTF-8");
            BizTemplateFile templateFile = bizTemplateFileService.getBizTemplateFile(params);
            String headfilename = null;
            if (templateFile != null) {
                String fileName = templateFile.getFileName();
                String suffix = "";
                if (fileName.lastIndexOf(".") != -1) {
                    suffix = fileName.substring(fileName.lastIndexOf("."));
                }
                String templateFilePath = environment.getProperty("templateFilePath");
                File inputFile = new File(templateFilePath + File.separator + templateFile.getId() + suffix);
                if (inputFile.exists() && inputFile.isFile()) {
                    headfilename = new String((templateFile.getFileName()).getBytes("gb2312"), "ISO-8859-1");
                    response.setHeader("Content-Disposition", "attachment;filename=" + headfilename);
                    FileUtils.copyFile(inputFile, response.getOutputStream());
                } else {
                    headfilename = new String("错误报告.txt".getBytes("gb2312"), "ISO-8859-1");
                    response.setHeader("Content-Disposition", "attachment;filename=" + headfilename);
                    outputStream.write("文件不存在!".getBytes());
                }
            } else {
                logger.info(" templateFile is null ");
                headfilename = new String("错误报告.txt".getBytes("gb2312"), "ISO-8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + headfilename);
                outputStream.write("文件不存在!请检查文件参数配置是否正确!".getBytes());
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            logger.error("文件不存在 !{}", e);
        }
    }

    @ResponseBody
    @RequestMapping("/remove")
    public Json remove(@RequestParam List<String> ids) {

        bizTemplateFileService.deleteByIds(ids);
        return Json.success();
    }
}
