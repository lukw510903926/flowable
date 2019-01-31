package com.flowable.oa.controller;

import com.flowable.oa.service.BizTimedTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flowable.oa.entity.BizTimedTask;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2018-02-07 14:21
 **/
@Controller
public class TemplateController {

	@Autowired
	private BizTimedTaskService bizTimedTaskService;

	@RequestMapping("/template")
	public String template(Model model) {

		model.addAttribute("test", "自定义标签处理");
		return "template";
	}

	@RequestMapping("/template/delete")
	public String delete() {

		BizTimedTask task = new BizTimedTask();
		task.setBizId("bizId");
		this.bizTimedTaskService.deleteByModel(task);
		return "template";
	}
}
