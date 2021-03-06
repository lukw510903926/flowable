package com.flowable.oa.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.oa.core.service.IProcessEngineService;
import com.flowable.oa.core.util.PageUtil;
import com.flowable.oa.core.util.exception.ServiceException;
import com.flowable.oa.core.vo.BaseVo;
import com.flowable.oa.core.vo.ProcessDefinitionEntityVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SubProcess;
import org.flowable.bpmn.model.UserTask;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipInputStream;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午10:02
 **/
@Slf4j
@Service
public class ProcessEngineServiceImpl implements IProcessEngineService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public Set<String> loadProcessStatus(String processId) {

        Set<String> set = new HashSet<>();
        List<UserTask> result = this.getAllTaskByProcessKey(processId);
        if (CollectionUtils.isNotEmpty(result)) {
            result.forEach(entity -> set.add(entity.getName()));
        }
        return set;
    }

    /**
     * 流程定义列表
     */
    @Override
    public PageInfo<ProcessDefinitionEntityVo> processList(ProcessDefinitionEntityVo processDefinition) {

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (processDefinition != null) {
            processDefinitionQuery.processDefinitionName(processDefinition.getName());
            processDefinitionQuery.processDefinitionKey(processDefinition.getKey());
        }
        PageInfo<BaseVo> page = PageUtil.getPage(processDefinition);
        processDefinitionQuery.latestVersion().orderByProcessDefinitionKey().asc();
        long count = processDefinitionQuery.count();

        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(page.getStartRow(), page.getEndRow());
        List<ProcessDefinitionEntityVo> result = new ArrayList<>();
        for (ProcessDefinition definition : processDefinitionList) {
            ProcessDefinitionEntityImpl definitionEntity = (ProcessDefinitionEntityImpl) definition;
            String deploymentId = definitionEntity.getDeploymentId();
            ProcessDefinitionEntityVo definitionEntityVo = new ProcessDefinitionEntityVo();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            BeanUtils.copyProperties(definitionEntity, definitionEntityVo);
            definitionEntityVo.setDeploymentTime(deployment.getDeploymentTime());
            result.add(definitionEntityVo);
        }
        return PageUtil.getResult(result, count);
    }

    /**
     * 流程定义列表
     */
    @Override
    public PageInfo<ProcessInstance> runningList(PageInfo<ProcessInstance> page, String procInsId, String procDefKey) {

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        processInstanceQuery.processInstanceId(procInsId);
        processInstanceQuery.processDefinitionKey(procDefKey);
        page.setTotal(processInstanceQuery.count());
        page.setList(processInstanceQuery.listPage(page.getStartRow(), page.getEndRow()));
        return page;
    }

    /**
     * 根据流程key得到
     *
     * @return
     */
    @Override
    public List<UserTask> getAllTaskByProcessKey(String processId) {

        List<UserTask> result = new ArrayList<>();
        InputStream inputStream = this.resourceRead(processId, "xml");
        if (inputStream == null) {
            return result;
        }
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        try {
            XMLStreamReader xtr = xif.createXMLStreamReader(in);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
            List<Process> processes = bpmnModel.getProcesses();
            if (CollectionUtils.isNotEmpty(processes)) {
                for (Process process : processes) {
                    Collection<FlowElement> flowElements = process.getFlowElements();
                    if (CollectionUtils.isNotEmpty(flowElements)) {
                        getAllUserTaskByFlowElements(flowElements, result);
                    }
                }
            }
        } catch (XMLStreamException e) {
            log.error("获取流程信息失败 ： ", e);
        }
        return result;
    }

    /**
     * 递归得到所有的UserTask
     *
     * @param result
     */
    private void getAllUserTaskByFlowElements(Collection<FlowElement> flowElements, List<UserTask> result) {

        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                result.add(userTask);
            } else if (flowElement instanceof SubProcess) {
                SubProcess subProcess = (SubProcess) flowElement;
                getAllUserTaskByFlowElements(subProcess.getFlowElements(), result);
            }
        }
    }

    /**
     * 读取资源，通过部署ID
     *
     * @param processDefinitionId 流程定义ID
     * @param resourceType        资源类型(xml|image)
     */
    @Override
    public InputStream resourceRead(String processDefinitionId, String resourceType) {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        if (processDefinition == null) {
            return null;
        }
        String resourceName = "";
        if (resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        return repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
    }

    /**
     * 部署流程 - 保存
     *
     * @param file
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deploy(String category, MultipartFile file) {

        StringBuilder builder = new StringBuilder();
        String fileName = file.getOriginalFilename();
        try (InputStream fileInputStream = file.getInputStream()) {
            Deployment deployment = null;
            String extension = FilenameUtils.getExtension(fileName);
            if ("zip".equals(extension) || "bar".equals(extension)) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else if ("png".equals(extension)) {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if (StringUtils.isNotBlank(fileName) && fileName.contains("bpmn20.xml")) {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if ("bpmn".equals(extension)) {
                // bpmn扩展名特殊处理，转换为bpmn20.xml
                String baseName = FilenameUtils.getBaseName(fileName);
                deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
            } else {
                builder.append("不支持的文件类型：").append(extension);
            }
            List<ProcessDefinition> list = Optional.ofNullable(deployment).map(entity -> repositoryService.createProcessDefinitionQuery().deploymentId(entity.getId()).list()).orElse(null);

            if (CollectionUtils.isEmpty(list)) {
                builder.append("部署失败，没有流程。");
            } else {
                list.forEach(entity -> {
                    repositoryService.setProcessDefinitionCategory(entity.getId(), category);
                    builder.append("部署成功，流程ID=").append(entity.getId()).append("<br/>");
                });
            }
        } catch (Exception e) {
            throw new ServiceException("部署失败！", e);
        }
        return builder.toString();
    }

    /**
     * 将部署的流程转换为模型
     *
     * @param procDefId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Model convertToModel(String procDefId) {

        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
            InputStream resource = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
            XMLInputFactory xif = XMLInputFactory.newInstance();
            InputStreamReader in = new InputStreamReader(resource, StandardCharsets.UTF_8);
            XMLStreamReader xtr = xif.createXMLStreamReader(in);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
            BpmnJsonConverter converter = new BpmnJsonConverter();
            ObjectNode modelNode = converter.convertToJson(bpmnModel);
            Model modelData = repositoryService.newModel();
            modelData.setKey(processDefinition.getKey());
            modelData.setName(processDefinition.getResourceName());
            modelData.setCategory(processDefinition.getCategory());
            modelData.setDeploymentId(processDefinition.getDeploymentId());
            modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count() + 1)));

            ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
            modelData.setMetaInfo(modelObjectNode.toString());

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes(StandardCharsets.UTF_8));
            return modelData;
        } catch (XMLStreamException e) {
            log.error("将部署的流程转换为模型失败 : ", e);
            throw new ServiceException("将部署的流程转换为模型失败");
        }
    }

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDeployment(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 删除部署的流程实例
     *
     * @param procInsId    流程实例ID
     * @param deleteReason 删除原因，可为空
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProcIns(String procInsId, String deleteReason) {
        runtimeService.deleteProcessInstance(procInsId, deleteReason);
    }
}
