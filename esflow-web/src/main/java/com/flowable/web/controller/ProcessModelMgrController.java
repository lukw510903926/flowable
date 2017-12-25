package com.flowable.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flowable.common.utils.DataGrid;
import com.flowable.common.utils.Json;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.AbstractVariable;
import com.flowable.core.bean.ProcessVariable;
import com.flowable.core.bean.ProcessVariableInstance;
import com.flowable.core.bean.TaskVariable;
import com.flowable.core.service.IProcessDefinitionService;
import com.flowable.core.service.IProcessVariableService;

@Controller
@RequestMapping("/processModelMgr")
public class ProcessModelMgrController{

	@Autowired
	private IProcessVariableService processValService;
	
	@Autowired
	private IProcessDefinitionService processDefinitionService ;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 得到流程全局变量列表 / 任务变量列表
	 * @param params
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "processValList")
	public DataGrid processValList(@RequestParam Map<String, Object> params,PageHelper<TaskVariable> page) {
	
		DataGrid grid = new DataGrid();
		try {
			String processId = (String) params.get("processId");
			String version = (String) params.get("version");
			if(StringUtils.isEmpty(version)) {
				version = "1";
			}
			String taskId = (String) params.get("taskId");
			if (StringUtils.isBlank(taskId)) {
				PageHelper<ProcessVariable> processPage = new  PageHelper<ProcessVariable>();
				processPage.setPage(page.getPage());
				processPage.setRows(page.getRows());
				PageHelper<ProcessVariable> processValBeans = processValService.loadProcessVariables(processId, Integer.parseInt(version),processPage);
				grid.setRows(processValBeans.getList());
				grid.setTotal((long) processValBeans.getCount());
			} else {
				PageHelper<TaskVariable> processTaskValBeans = processValService.loadTaskVariables(processId, Integer.parseInt(version),page, taskId);
				grid.setRows(processTaskValBeans.getList());
				grid.setTotal(processTaskValBeans.getCount());
			}
		} catch (Exception e) {
			logger.error("操作失败 : {}",e);
		}
		return grid;
	}

	/**
	 * 
	 * 根据全局流程变量ID得到变量详情
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getProcessValById")
	public Json getProcessValById(@RequestParam Map<String, Object> params) {
		
		logger.info("根据全局流程变量ID得到变量详情---getProcessValById");
		String processId = (String) params.get("processId");
		String taskId = (String) params.get("taskId");
		Json ajaxJson = new Json();
		try {
			AbstractVariable processValAbs = null;
			if (StringUtils.isBlank(taskId)) {
				processValAbs = processValService.getVariableById(processId);
			} else {
				processValAbs = processValService.getVariableById(taskId);
			}
			ajaxJson.setObj(processValAbs);
			ajaxJson.setSuccess(true);
		} catch (Exception e) {
			logger.error("操作失败 : {}",e);
			ajaxJson.setMsg("查询失败");
			ajaxJson.setSuccess(false);
		}
		return ajaxJson;
	}

	/**
	 * 根据全局流程变量IDs删除变量详情
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "deleteProcessValById")
	@ResponseBody
	public Json deleteProcessValById(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("根据全局流程变量IDs删除变量详情---deleteProcessValById");
		String[] valIds = ((String) params.get("valIds")).split(",");
		Json json = new Json();
		try {
			for (int i = 0; i < valIds.length; i++) {
				AbstractVariable processValAbs = processValService.getVariableById(valIds[i]);
				if (processValAbs != null) {
					processValService.deleteVariable(processValAbs);
				}
			}
			json.setSuccess(true);
			json.setMsg("删除成功!");
		} catch (Exception e) {
			logger.error("操作失败 : {}",e);
			json.setSuccess(false);
			json.setMsg("删除失败!");
		}
		return json;
	}

	/**
	 * 保存或者更新流程全局变量
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveOrUpdateProcessVal")
	public Json saveOrUpdateProcessVal(HttpServletRequest request, @RequestParam Map<String, Object> reqParams) {
		logger.info("保存或者更新流程全局变量---saveOrUpdateProcessVal");
		Json json = new Json();
		try {
			boolean isUpdate = true;
			boolean isTask = false;
			String id = (String) reqParams.get("id");
			String taskId = (String) reqParams.get("taskId");
			AbstractVariable processValAbs = processValService.getVariableById(id);
			if (StringUtils.isNotBlank(taskId)) {
				isTask = true;
			}
			if (processValAbs == null) {
				if (isTask) {
					processValAbs = new TaskVariable();
				} else {
					processValAbs = new ProcessVariable();
				}
				processValAbs.setId(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
				isUpdate = false;
			}
			processValAbs.setProcessDefinitionId((String) reqParams.get("processId"));
			processValAbs.setVersion(Integer.parseInt((String) reqParams.get("version")));
			processValAbs.setName((String) reqParams.get("name"));
			processValAbs.setAlias((String) reqParams.get("alias"));
			processValAbs.setRefVariable((String) reqParams.get("refVariable"));
			processValAbs.setRefParam((String) reqParams.get("refParam"));
			String temp = (String) reqParams.get("nameOrder");
			Integer temp2 = null;
			try {
				temp2 = Integer.parseInt(temp);
			} catch (Exception e) {
			}
			if (temp2 == null) {
				// 查询当前最大序号
				temp2 = processValService.getProcessOrder(processValAbs);
				temp2 = temp2 == null ? 1 : temp2 + 1;
			}
			processValAbs.setOrder(temp2);
			processValAbs.setRequired(Boolean.parseBoolean((String) reqParams.get("required")));
			processValAbs.setGroupName((String) reqParams.get("groupName"));

			temp = (String) reqParams.get("groupOrder");
			temp2 = null;
			try {
				temp2 = Integer.parseInt(temp);
			} catch (Exception e) {
			}
			if (temp2 == null) {
				temp2 = 0;
			}
			processValAbs.setGroupOrder(temp2);

			// 页面组件特殊处理
			String viewComponent = (String) reqParams.get("viewComponent");
			String viewComponentVal = (String) reqParams.get("viewDatas");
			processValAbs.setViewComponent(viewComponent);
			processValAbs.setViewDatas(viewComponentVal);
			processValAbs.setViewParams((String) reqParams.get("viewParams"));

			processValAbs.setProcessVariable(Boolean.parseBoolean((String) reqParams.get("isprocVal")));

			if (isTask) {
				String variableGroup = (String) reqParams.get("variableGroup");
				((TaskVariable) processValAbs).setVariableGroup(variableGroup);
				((TaskVariable) processValAbs).setTaskId(taskId);
			}
			if (isUpdate) {
				processValService.updateVariable(processValAbs);
			} else {
				processValService.addVariable(processValAbs);
			}
			json.setSuccess(true);
			json.setMsg("操作成功");
		} catch (Exception e) {
			logger.error("操作失败 : {}",e);
			json.setSuccess(false);
			json.setMsg("操作失败!");
		}
		return json;
	}
	
	/**
	 * 得到流程全局变量编辑分组标签
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "processLabel")
	public List<Map<String,Object>> processLabel(@RequestParam Map<String, Object> params) {

		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			String processId = (String) params.get("processId");
			String version = (String) params.get("version");
			String taskId = (String) params.get("taskId");
			Set<String> grops = new HashSet<String>(); 
			if (StringUtils.isBlank(taskId)) {
				List<ProcessVariable> processValBeans = processValService.loadVariables(processId, Integer.parseInt(version));				
				for(ProcessVariable process:processValBeans){
					grops.add(process.getGroupName().trim());
				}
				for(String grop:grops){
					List<ProcessVariable> processes = new ArrayList<ProcessVariable>();
					for(ProcessVariable process:processValBeans){
						if(grop==process.getGroupName()||(grop!=null&&grop.equals(process.getGroupName().trim()))){
							processes.add(process);
						}
					}
					Map<String,Object> data = new HashMap<String,Object>(); 
					data.put("grop", grop);
					data.put("list", processes);
					list.add(data);
				}
			} else {
				List<TaskVariable> processTaskValBeans = processValService.loadTaskVariables(processId, Integer.parseInt(version), taskId);
				for(TaskVariable task:processTaskValBeans){
					grops.add(task.getGroupName().trim());
				}
				for(String grop:grops){
					List<TaskVariable> tasks = new ArrayList<TaskVariable>();
					for(TaskVariable task:processTaskValBeans){
						if(grop==task.getGroupName()||(grop!=null&&grop.equals(task.getGroupName().trim()))){
							tasks.add(task);
						}
					}
					Map<String,Object> data = new HashMap<String,Object>(); 
					data.put("grop", grop);
					data.put("list", tasks);
					list.add(data);
				}
			}
		} catch (Exception e) {
			logger.error("操作失败 : {}",e);
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping("/getVarInstanceInstance")
	public ProcessVariableInstance	getVarInstanceInstance(@RequestParam Map<String,String> params){
		
		List<ProcessVariableInstance> list = this.processValService.getProcessVariableInstances(params);
		ProcessVariableInstance instance = null;
		if(list != null && !list.isEmpty()){
			instance = list.get(0);
		}
		return instance;
	}
	
	@ResponseBody
	@RequestMapping("/getProcessId/{key}")
	public String getProcessId(@PathVariable("key")String key){
		
		ProcessDefinition pd = processDefinitionService.getLatestProcDefByKey(key);
		if(pd != null)
			return pd.getId();
		else
			return null;
	}
}
