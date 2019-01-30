package com.flowable.web.controller;

import java.io.File;
import java.io.OutputStream;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.flowable.entity.BizTemplateFile;
import com.flowable.service.flow.BizTemplateFileService;
import com.flowable.util.Json;
import com.flowable.util.LoginUser;
import com.flowable.util.PageHelper;
import com.flowable.util.WebUtil;

@Controller
@RequestMapping("/bizTemplateFile")
public class BizTemplateFileController {

	@Value("${templateFilePath}")
	private String path;

	@Autowired
	private BizTemplateFileService bizTemplateFileService;

	private Logger logger = LoggerFactory.getLogger("bizTemplateFileController");

	@RequestMapping("/index")
	public String index() {

		return "process/config/bizTemplateFileList";
	}

	@ResponseBody
	@RequestMapping("/list")
	public Map<String, Object> list(PageHelper<BizTemplateFile> page, BizTemplateFile file) {

		PageHelper<BizTemplateFile> helper = bizTemplateFileService.findTemplateFlies(page, file, true);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("total", helper.getCount());
		data.put("rows", helper.getList());
		return data;
	}

	@ResponseBody
	@RequestMapping("/upload")
	public ResponseEntity<String> upload(@RequestParam MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.TEXT_PLAIN);
		Json json = new Json();
		WebUtil.getLoginUser(request, response);
		LoginUser loginUser = WebUtil.getLoginUser();
		String username = loginUser.getUsername();
		String flowName = request.getParameter("flowName");
		BizTemplateFile bizTemplateFile = new BizTemplateFile();
		bizTemplateFile.setCreateUser(username);
		bizTemplateFile.setFullName(loginUser.getName());
		bizTemplateFile.setFlowName(flowName);
		bizTemplateFileService.saveOrUpdate(bizTemplateFile, file);
		json.setSuccess(true);
		json.setMsg("上传成功");
		return new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping("/downloadTemplate")
	public void downloadTemplate(@RequestParam Map<String, String> params, HttpServletRequest request,
			HttpServletResponse response) {

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
				File inputFile = new File(path + File.separator + templateFile.getId() + suffix);
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

		Json json = new Json();
		bizTemplateFileService.deleteByIds(ids);
		json.setSuccess(true);
		json.setMsg("删除成功");
		return json;
	}
}
