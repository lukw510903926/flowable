package com.flowable.oa.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.flowable.oa.service.IBizInfoService;
import com.flowable.oa.service.act.ActProcessService;

@Controller
@RequestMapping("/office")
public class IndexController {

	@Autowired
	private IBizInfoService workService;
	
	@Autowired
	private ActProcessService actProcessService;
	
	@RequestMapping("/index")
	public String index(Model model){
		
		return "index";
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
}
