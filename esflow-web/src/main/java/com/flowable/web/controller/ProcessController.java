package com.flowable.web.controller;

import com.flowable.core.service.IProcessModelService;
import com.flowable.core.service.act.ActProcessService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProcessController{

	@Autowired
	private IProcessModelService processModelService;
	
	@Autowired
	private ActProcessService actProcessService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 模型管理视图
	 * @return
	 */
	@RequestMapping(value = "model")
	public String modelView() {
		return "process/model_list";
	}
	/**
	 * 创建模型视图
	 * @return
	 */
	@RequestMapping(value = "model/create", method = RequestMethod.GET)
	public String createModelView() {
		return "process/model_create";
	}
	/**
	 * 创建模型
	 */
	@RequestMapping(value = "model/create", method = RequestMethod.POST)
	public String createModelAction(String name, String key, String description, String category,Model model) {
		try {
			org.flowable.engine.repository.Model modelData = processModelService.create(name, key, description,category);
			model.addAttribute("message", "success");
			model.addAttribute("modelId", modelData.getId());
		} catch (Exception e) {
			logger.error("创建模型失败：", e);
		}
		return "process/model_create";
	}
	/**
	 * 流程管理视图
	 * @return
	 */
	@RequestMapping(value = "process")
	public String processView() {
		return "process/process_list";
	}
	/**
	 * 发布流程视图
	 * @return
	 */
	@RequestMapping(value = "process/deploy", method = RequestMethod.GET)
	public String deployProcessView() {
		return "process/process_deploy";
	}
	/**
	 * 部署流程 - 保存
	 * @return
	 */
	@RequestMapping(value = "process/deploy", method = RequestMethod.POST)
	public String deployProcess(MultipartHttpServletRequest request, RedirectAttributes redirectAttributes, Model model)
			throws Exception {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multiRequest.getFile("file");
		String fileName = file.getOriginalFilename();
		String exportDir = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/deployments/";
		if (StringUtils.isBlank(fileName)) {
			redirectAttributes.addFlashAttribute("message", "请选择要部署的流程文件");
		} else {
			String message = actProcessService.deploy(exportDir, null, file);
			redirectAttributes.addFlashAttribute("message", message);
			model.addAttribute("message", "success");
		}
		return "process/process_deploy";
	}
	/**
	 * 设置流程变量视图
	 * @return
	 */
	@RequestMapping(value = "process/variable", method = RequestMethod.GET)
	public String setProcessVariableView(String processDefinitionId, String version, String taskId, Model model) {
		model.addAttribute("processDefinitionId", processDefinitionId);
		model.addAttribute("version", version);
		model.addAttribute("taskId", taskId);
		return "process/variable_list";
	}
	@RequestMapping(value = "process/variable/edit", method = RequestMethod.GET)
	public String setProcessVariableEdit(String processDefinitionId, String version, String taskId, String vId,
			Model model) {
		model.addAttribute("processDefinitionId", processDefinitionId);
		model.addAttribute("version", version);
		model.addAttribute("taskId", taskId);
		model.addAttribute("vId", vId);
		return "process/variable_edit";
	}
	/**
	 * 设置流程任务视图
	 * @return
	 */
	@RequestMapping(value = "process/task/list", method = RequestMethod.GET)
	public String setTaskView(String processDefinitionId, String version, Model model) {
		model.addAttribute("processDefinitionId", processDefinitionId);
		model.addAttribute("version", version);
		return "process/task_list";
	}
}
