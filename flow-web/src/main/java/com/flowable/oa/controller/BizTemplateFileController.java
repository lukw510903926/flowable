package com.flowable.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.flowable.oa.core.entity.BizTemplateFile;
import com.flowable.oa.core.service.BizTemplateFileService;
import com.flowable.oa.core.service.act.ActProcessService;
import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.util.WebUtil;
import com.flowable.oa.core.vo.ProcessDefinitionEntityVo;
import com.github.pagehelper.PageInfo;
import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午11:10
 **/
@Controller
@RequestMapping("/bizTemplateFile")
public class BizTemplateFileController {

    @Value("${biz.file.path}")
    private String bizFileRootPath;

    @Autowired
    private BizTemplateFileService bizTemplateFileService;

    @Autowired
    private ActProcessService actProcessService;

    private Logger logger = LoggerFactory.getLogger(BizTemplateFileController.class);

    @RequestMapping("/index")
    public String index(Model model) {

        List<ProcessDefinitionEntityVo> tempResult = actProcessService.processList();
        model.addAttribute("processList", tempResult);
        return "modules/template/bizTemplateFileList";
    }

    @ResponseBody
    @RequestMapping("/list")
    public Map<String, Object> list(PageInfo<BizTemplateFile> page, BizTemplateFile file) {

        PageInfo<BizTemplateFile> helper = bizTemplateFileService.findByModel(page, file, true);
        Map<String, Object> data = new HashMap<>();
        data.put("total", helper.getTotal());
        data.put("rows", helper.getList());
        return data;
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

    @ResponseBody
    @RequestMapping("/download")
    public void downloadTemplate(@RequestParam Map<String, String> params, HttpServletResponse response) {

        try (OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/octet-stream;charset=UTF-8");
            BizTemplateFile templateFile = bizTemplateFileService.getBizTemplateFile(params);
            if (templateFile != null) {
                String fileName = templateFile.getFileName();
                String suffix = "";
                if (fileName.lastIndexOf(".") != -1) {
                    suffix = fileName.substring(fileName.lastIndexOf("."));
                }
                response.setHeader("Content-Disposition", "attachment;");
                File inputFile = new File(bizFileRootPath + File.separator + templateFile.getId() + suffix);
                if (inputFile.exists() && inputFile.isFile()) {
                    FileUtils.copyFile(inputFile, outputStream);
                } else {
                    FileUtils.copyFile(File.createTempFile("文件不存在!", ".txt"), outputStream);
                }
            } else {
                FileUtils.copyFile(File.createTempFile("文件不存在!", ".txt"), outputStream);
            }
        } catch (Exception e) {
            logger.error("文件不存在 !{}", e);
        }
    }

    @ResponseBody
    @RequestMapping("/remove")
    public RestResult<Object> remove(@RequestParam List<Serializable> ids) {

        bizTemplateFileService.deleteByIds(ids);
        return RestResult.success();
    }
}
