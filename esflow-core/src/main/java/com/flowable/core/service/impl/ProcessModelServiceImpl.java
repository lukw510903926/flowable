/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.flowable.core.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.flowable.common.exception.ServiceException;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.service.IProcessModelService;

/**
 * 流程模型相关Controller
 * 
 * @author ThinkGem
 * @version 2013-11-03
 */
@Service
@Transactional(readOnly = true)
public class ProcessModelServiceImpl implements IProcessModelService {

	@Autowired
	private RepositoryService repositoryService;

	private Logger logger = LoggerFactory.getLogger(ProcessModelServiceImpl.class);

	/**
	 * 流程模型列表
	 */
	@Override
	public PageHelper<Model> modelList(PageHelper<Model> page, String category) {
		ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();
		if (StringUtils.isNotEmpty(category)) {
			modelQuery.modelCategory(category);
		}
		page.setCount(modelQuery.count());
		page.setList(modelQuery.listPage(page.getFirstRow(), page.getMaxRow()));
		return page;
	}

	/**
	 * 创建模型
	 * 
	 */
	@Override
	@Transactional(readOnly = false)
	public Model create(String name, String key, String description, String category) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();

			description = StringUtils.defaultString(description);
			modelData.setKey(StringUtils.defaultString(key));
			modelData.setName(name);
			modelData.setCategory(category);
			Integer version = Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count() + 1));
			modelData.setVersion(version);
			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			modelData.setMetaInfo(modelObjectNode.toString());
			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
			return modelData;
		} catch (UnsupportedEncodingException e) {
			logger.error("模型创建失败 : {}", e);
			throw new ServiceException("模型创建失败 !");
		}

	}

	/**
	 * 根据Model部署流程
	 */
	@Override
	@Transactional(readOnly = false)
	public String deploy(String id) {

		String procdefId = "";
		try {
			Model modelData = repositoryService.getModel(id);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new com.fasterxml.jackson.databind.ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			String processName = modelData.getName();
			if (!StringUtils.endsWith(processName, ".bpmn20.xml")) {
				processName += ".bpmn20.xml";
			}
			logger.info("========="+processName+"============"+modelData.getName());
			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
					.addInputStream(processName, in).deploy();
			// .addString(processName, new String(bpmnBytes)).deploy();

			//设置流程分类
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
					.deploymentId(deployment.getId()).list();
			for (ProcessDefinition processDefinition : list) {
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), modelData.getCategory());
				procdefId = processDefinition.getId();
			}
			if (CollectionUtils.isEmpty(list)) {
				procdefId = "";
			}
		} catch (Exception e) {
			logger.error("设计模型图不正确，检查模型正确性 :",e);
			throw new ServiceException("设计模型图不正确，检查模型正确性，模型ID=" + id, e);
		}
		return procdefId;
	}

	/**
	 * 导出model的xml文件
	 * 
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	@Override
	public void export(String id, HttpServletResponse response) {
		
		try {
			Model modelData = repositoryService.getModel(id);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new com.fasterxml.jackson.databind.ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			IOUtils.copy(in, response.getOutputStream());
			String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.flushBuffer();
		} catch (Exception e) {
			logger.error(" 导出model的xml文件失败 :",e);
			throw new ServiceException("导出model的xml文件失败，模型ID=" + id, e);
		}

	}

	/**
	 * 更新Model分类
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateCategory(String id, String category) {
		
		Model modelData = repositoryService.getModel(id);
		modelData.setCategory(category);
		repositoryService.saveModel(modelData);
	}

	/**
	 * 删除模型
	 * 
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(String id) {
		repositoryService.deleteModel(id);
	}

}
