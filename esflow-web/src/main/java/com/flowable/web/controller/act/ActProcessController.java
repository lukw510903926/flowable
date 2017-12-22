/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.flowable.web.controller.act;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.impl.persistence.entity.DeploymentEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flowable.common.utils.DataGrid;
import com.flowable.common.utils.DateUtils;
import com.flowable.common.utils.Json;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.service.IProcessDefinitionService;
import com.flowable.core.service.act.ActProcessService;

/**
 * 流程定义相关Controller
 * 
 * @author ThinkGem
 * @version 2013-11-03
 */
@Controller
@RequestMapping(value = "/act/process")
public class ActProcessController {

	@Autowired
	private ActProcessService actProcessService;
	
	@Autowired
	private IProcessDefinitionService processDefinitionService ;
	
	/**
	 * 流程定义列表
	 */
	@ResponseBody
	@RequestMapping(value = "list")
	public DataGrid processList(PageHelper<Object[]> page,@RequestParam Map<String, Object> params) {

		DataGrid grid = new DataGrid();
		try {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			page = actProcessService.processList(page, null);
			List<Object[]> tempResult = page.getList();
			if (CollectionUtils.isNotEmpty(tempResult)) {
				for (Object[] objects : tempResult) {
					ProcessDefinitionEntity process = (ProcessDefinitionEntity) objects[0];
					DeploymentEntity deployment = (DeploymentEntity) objects[1];
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("id", process.getId());
					item.put("key", process.getKey());
					item.put("name", process.getName());
					item.put("version", process.getVersion());
					item.put("deploymentId", process.getDeploymentId());
					item.put("resourceName", process.getResourceName());
					item.put("diagramResourceName", process.getDiagramResourceName());
					item.put("deploymentTime", DateUtils.formatDateTime(deployment.getDeploymentTime()));
					result.add(item);
				}
			}
			grid.setRows(result);
			grid.setTotal(page.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grid;
	}

	/**
	 * 流程所有任务列表
	 */
	@ResponseBody
	@RequestMapping(value = "processTaskList")
	public DataGrid processTaskList(@RequestParam Map<String, Object> params) {
		
		DataGrid grid = new DataGrid();
		try {
			String processId = (String) params.get("processId");
			List<Map<String, Object>> result = actProcessService.getAllTaskByProcessKey(processId);
			grid.setRows(result);
			grid.setTotal((long) result.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grid;
	}

	/**
	 * 运行中的实例列表
	 */
	@ResponseBody
	@RequestMapping(value = "running")
	public DataGrid runningList(PageHelper<ProcessInstance> page,String procInsId, String procDefKey) {
		
		DataGrid grid = new DataGrid();
		try {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			PageHelper<ProcessInstance> helper = actProcessService.runningList(page, procInsId, procDefKey);
			List<ProcessInstance> tempResult = helper.getList();
			if (CollectionUtils.isNotEmpty(tempResult)) {
				for (ProcessInstance processInstance : tempResult) {
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("id", processInstance.getId());
					item.put("processInstanceId", processInstance.getProcessInstanceId());
					item.put("processDefinitionId", processInstance.getProcessDefinitionId());
					item.put("activityId", processInstance.getActivityId());
					item.put("suspended", processInstance.isSuspended());
				}
			}
			grid.setRows(result);
			grid.setTotal((long) result.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grid;
	}

	/**
	 * 读取资源，通过部署ID
	 * 
	 * @param processDefinitionId
	 *            流程定义ID
	 * @param processInstanceId
	 *            流程实例ID
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "resource/read")
	public void resourceRead(String processDefinitionId, String processInstanceId, String type, HttpServletResponse response) throws Exception {
		InputStream resourceAsStream = actProcessService.resourceRead(processDefinitionId, processInstanceId, type);
		if (type.equals("image")) {
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
		} else {
			StringBuffer stringBuffer = new StringBuffer();
			LineIterator it = IOUtils.lineIterator(resourceAsStream, "utf-8");
			while (it.hasNext()) {
				try {
					String line = it.nextLine();
					stringBuffer.append(line);
					stringBuffer.append("\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			response.setContentType("text/plain;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println(stringBuffer.toString());
			out.close();
		}
	}

	/**
	 * 部署流程 - 保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deploy", method = RequestMethod.POST)
	public String deploy(MultipartHttpServletRequest request, RedirectAttributes redirectAttributes, Model model) throws Exception {
		
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multiRequest.getFile("file");
		String fileName = file.getOriginalFilename();
		String exportDir = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/deployments/";
		boolean result = false;
		String message = null;
		if (StringUtils.isBlank(fileName)) {
			message = "请选择要部署的流程文件";
		} else {
			String key = fileName.substring(0, fileName.indexOf("."));
			ProcessDefinition processDefinition = processDefinitionService.getLatestProcDefByKey(key);
			message = actProcessService.deploy(exportDir, null, file);
			processDefinitionService.copyVariables(processDefinition);
			result = true;
		}
		model.addAttribute("result", result);
		model.addAttribute("message", message);
		return "process/process_deploy";
	}
	
	/**
	 * 挂起、激活流程实例
	 */
	@ResponseBody
	@RequestMapping(value = "update/{state}")
	public Json updateState(@PathVariable("state") String state, @RequestParam String processDefinitionId ) {
		Json json = new Json();
		try {
			String message = actProcessService.updateState(state, processDefinitionId);
			json.setSuccess(true);
			json.setMsg(message);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("操作失败!");
		}
		return json;
	}

	/**
	 * 将部署的流程转换为模型
	 * 
	 * @param processDefinitionId
	 * @param redirectAttributes
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XMLStreamException
	 */
	@ResponseBody
	@RequestMapping(value = "convert")
	public Json convertToModel(@RequestParam String processDefinitionId )  {
		
		Json json = new Json();
		try {
			org.flowable.engine.repository.Model modelData = actProcessService.convertToModel(processDefinitionId);
			String message = "转换模型成功，模型ID=" + modelData.getId();
			json.setSuccess(true);
			json.setMsg(message);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("操作失败!");
		}
		return json;
	}

	/**
	 * 导出图片文件到硬盘
	 */
	@RequestMapping(value = "export/diagrams")
	@ResponseBody
	public List<String> exportDiagrams(@Value("#{APP_PROP['activiti.export.diagram.path']}") String exportDir) throws IOException {
		
		return actProcessService.exportDiagrams(exportDir);
	}

	/**
	 * 删除部署的流程，级联删除流程实例
	 * 
	 * @param deploymentId
	 *            流程部署ID
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public Json delete(String deploymentId) {
		Json json = new Json();
		try {
			actProcessService.deleteDeployment(deploymentId);
			json.setSuccess(true);
			json.setMsg("删除成功--" + deploymentId);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("操作失败!");
		}
		return json;
	}

	/**
	 * 删除流程实例
	 * 
	 * @param procInsId
	 *            流程实例ID
	 * @param reason
	 *            删除原因
	 */
	@ResponseBody
	@RequestMapping(value = "deleteProcIns")
	public Json deleteProcIns(String procInsId, String reason ) {
		Json json = new Json();
		try {
			if (StringUtils.isNotBlank(reason)) {
				actProcessService.deleteProcIns(procInsId, reason);
			}
			json.setSuccess(true);
			json.setMsg("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("操作失败!");
		}
		return json;
	}

}
