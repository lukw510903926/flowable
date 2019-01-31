package com.flowable.oa.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flowable.oa.util.LoginUser;
import com.flowable.oa.util.WebUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.flowable.oa.entity.BizFile;
import com.flowable.oa.service.IBizFileService;
import com.flowable.oa.service.IBizInfoService;
import com.flowable.oa.service.IProcessExecuteService;
import com.flowable.oa.service.act.ActProcessService;

@Controller
@RequestMapping
public class BizInfoController {

	@Autowired
	private ActProcessService actProcessService;

	@Autowired
	private IProcessExecuteService processExecuteService;

	@Autowired
	private IBizInfoService workService;

	@Autowired
	private IBizFileService bizFileService;

	private Logger logger = LoggerFactory.getLogger(BizInfoController.class);

	@ResponseBody
	@RequestMapping("/biz/process/status")
	public List<String> getProcessStatus(ProcessDefinition processDefinition) {

		if (StringUtils.isBlank(processDefinition.getName())) {
			return this.workService.loadBizInfoStatus(null);
		}
		List<ProcessDefinition> list = actProcessService.findProcessDefinition(processDefinition);
		if (CollectionUtils.isNotEmpty(list)) {
			return this.workService.loadBizInfoStatus(list.get(0).getId());
		}
		return new ArrayList<String>();
	}

	/**
	 * 工单管理视图
	 * 
	 * @return
	 */
	@RequestMapping(value = "biz/list/{action}")
	public String queryView(@PathVariable("action") String action, Model model) {

		model.addAttribute("action", action);
		List<String> processList = new ArrayList<String>();
		List<ProcessDefinition> list = actProcessService.findProcessDefinition(null);
		if (CollectionUtils.isNotEmpty(list)) {
			for (ProcessDefinition processDefinition : list) {
				processList.add(processDefinition.getName());
			}
		}
		List<String> statusList = workService.loadBizInfoStatus(null);
		model.addAttribute("statusList", JSONObject.toJSON(statusList));
		model.addAttribute("processList", JSONObject.toJSON(processList));
		return "modules/biz/biz_list";
	}

	/**
	 * 工单管理视图
	 * 
	 * @return
	 */
	@RequestMapping(value = "biz/create/{key}")
	public String createView(@PathVariable String key, String bizId, Model model, HttpServletRequest request,
			HttpServletResponse response) {

		model.addAttribute("key", key);
		WebUtil.getLoginUser(request);
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("username", "admin");
		userMap.put("fullname", "管理员");
		model.addAttribute("createUser", JSONObject.toJSON(userMap));
		model.addAttribute("bizId", bizId);
		return "modules/biz/biz_create";
	}

	@RequestMapping(value = "biz/{id}", method = RequestMethod.GET)
	public String detailView(@PathVariable String id, Model model, HttpServletRequest request,
			HttpServletResponse response) {

		model.addAttribute("id", id);
		LoginUser createUser = WebUtil.getLoginUser(request);
		model.addAttribute("currentUser", JSONObject.toJSON(createUser));
		return "modules/biz/biz_detail";
	}

	@ResponseBody
	@RequestMapping("biz/download")
	public void download(String id, HttpServletResponse response) {
		try {
			BizFile bizFile = bizFileService.getBizFileById(id);
			response.setContentType("application/octet-stream;charset=UTF-8");
			File file = new File("/home/ipnet/esflowFilePath/" + bizFile.getPath());
			if (file.exists()) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + new String(bizFile.getName().getBytes("gb2312"), "ISO-8859-1"));
				FileUtils.copyFile(file, response.getOutputStream());
			} else {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + new String("文件不存在".getBytes("gb2312"), "ISO-8859-1"));
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
	public Map<String, Object> getDraftBiz(@PathVariable("bizId") String bizId, HttpServletRequest request, HttpServletResponse response) {
		WebUtil.getLoginUser(request);
		return processExecuteService.queryWorkOrder(bizId);
	}

}
