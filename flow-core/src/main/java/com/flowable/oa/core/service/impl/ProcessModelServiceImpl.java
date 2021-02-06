package com.flowable.oa.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.oa.core.service.IProcessModelService;
import com.flowable.oa.core.util.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 流程模型相关
 *
 * @author : lukewei
 * @createTime : 2018年1月31日 : 下午4:13:32
 * @description :
 */
@Slf4j
@Service
public class ProcessModelServiceImpl implements IProcessModelService {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 创建模型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Model create(String name, String key, String description, String category) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.set("stencilset", stencilSetNode);
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
        repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));
        return modelData;
    }

    /**
     * 根据Model部署流程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deploy(String id) {

        String processDefinitionId = "";
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
            log.info("=========" + processName + "============" + modelData.getName());
            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
                    .addInputStream(processName, in).deploy();

            //设置流程分类
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId()).list();
            if (CollectionUtils.isNotEmpty(list)) {
                for (ProcessDefinition processDefinition : list) {
                    repositoryService.setProcessDefinitionCategory(processDefinition.getId(), modelData.getCategory());
                    processDefinitionId = processDefinition.getId();
                }
            }
        } catch (Exception e) {
            log.error("设计模型图不正确，检查模型正确性 :", e);
            throw new ServiceException("设计模型图不正确，检查模型正确性，模型ID=" + id, e);
        }
        return processDefinitionId;
    }

    /**
     * 导出model的xml文件
     */
    @Override
    public void export(String id, HttpServletResponse response) {

        try {
            Model modelData = repositoryService.getModel(id);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper()
                    .readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            log.error(" 导出model的xml文件失败 :", e);
            throw new ServiceException("导出model的xml文件失败，模型ID=" + id);
        }

    }

}
