package com.flowable.oa.core.service.act;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.flowable.oa.core.util.exception.ServiceException;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
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
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.multipart.MultipartFile;

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
public class ActProcessService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    public Set<String> loadProcessStatus(String processId) {

        Set<String> set = new HashSet<>();
        List<Map<String, Object>> result = this.getAllTaskByProcessKey(processId);
        if (CollectionUtils.isNotEmpty(result)) {
            result.forEach(entity -> set.add(MapUtils.getString(entity, "name")));
        }
        return set;
    }

    /**
     * 流程定义列表
     */
    public List<ProcessDefinition> findProcessDefinition(ProcessDefinition processDefinition) {

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (processDefinition != null) {
            if (StringUtils.isNotBlank(processDefinition.getName())) {
                processDefinitionQuery.processDefinitionName(processDefinition.getName());
            }
            if (StringUtils.isNotBlank(processDefinition.getKey())) {
                processDefinitionQuery.processDefinitionKey(processDefinition.getKey());
            }
        }
        processDefinitionQuery.latestVersion().orderByProcessDefinitionKey().asc();
        return processDefinitionQuery.list();
    }

    /**
     * 流程定义列表
     */
    public List<Object[]> processList() {

        List<ProcessDefinition> processDefinitionList = this.findProcessDefinition(null);
        List<Object[]> result = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            result.add(new Object[]{processDefinition, deployment});
        }
        return result;
    }

    /**
     * 流程定义列表
     */
    public PageInfo<ProcessInstance> runningList(PageInfo<ProcessInstance> page, String procInsId, String procDefKey) {

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        if (StringUtils.isNotBlank(procInsId)) {
            processInstanceQuery.processInstanceId(procInsId);
        }
        if (StringUtils.isNotBlank(procDefKey)) {
            processInstanceQuery.processDefinitionKey(procDefKey);
        }
        page.setTotal(processInstanceQuery.count());
        page.setList(processInstanceQuery.listPage(page.getStartRow(), page.getEndRow()));
        return page;
    }

    /**
     * 根据流程key得到
     *
     * @return
     */
    public List<Map<String, Object>> getAllTaskByProcessKey(String processId) {

        List<Map<String, Object>> result = new ArrayList<>();
        InputStream inputStream = resourceRead(processId, "xml");
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
            log.error("获取流程信息失败 ： {}", e);
        }
        return result;
    }

    /**
     * 递归得到所有的UserTask
     *
     * @param result
     */
    private void getAllUserTaskByFlowElements(Collection<FlowElement> flowElements, List<Map<String, Object>> result) {

        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                Map<String, Object> temp = new HashMap<>();
                temp.put("id", userTask.getId());
                temp.put("name", userTask.getName());
                result.add(temp);
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
    @Transactional
    public String deploy(String exportDir, String category, MultipartFile file) {

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
            } else if ("bpmn".equals(extension)) { // bpmn扩展名特殊处理，转换为bpmn20.xml
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
     * 设置流程分类
     */
    @Transactional
    public void updateCategory(String procDefId, String category) {
        repositoryService.setProcessDefinitionCategory(procDefId, category);
    }

    /**
     * 挂起、激活流程实例
     */
    @Transactional
    public String updateState(String state, String processDefinitionId) {

        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
        if (processDefinition.isSuspended() && state.equals("suspend")) {
            return "挂起ID为[" + processDefinitionId + "]的流程中断，流程已挂起。";
        } else if (!processDefinition.isSuspended() && state.equals("active")) {
            return "激活ID为[" + processDefinitionId + "]的流程中断，流程已激活。";
        }
        if (state.equals("active")) {
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            return "已激活ID为[" + processDefinitionId + "]的流程定义。";
        } else if (state.equals("suspend")) {
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            return "已挂起ID为[" + processDefinitionId + "]的流程定义。";
        }

        return "无操作";
    }

    /**
     * 将部署的流程转换为模型
     *
     * @param procDefId
     * @throws XMLStreamException
     */
    @Transactional
    public Model convertToModel(String procDefId) throws XMLStreamException {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
        InputStream resource = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(resource, StandardCharsets.UTF_8);
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        com.fasterxml.jackson.databind.node.ObjectNode modelNode = converter.convertToJson(bpmnModel);
        Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getResourceName());
        modelData.setCategory(processDefinition.getCategory());// .getDeploymentId());
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
    }

    /**
     * 导出图片文件到硬盘
     */
    public List<String> exportDiagrams(String exportDir) throws IOException {

        List<String> files = new ArrayList<>();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : list) {
            String diagramResourceName = processDefinition.getDiagramResourceName();
            String key = processDefinition.getKey();
            int version = processDefinition.getVersion();
            InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName);
            if (resourceAsStream != null) {
                String diagramDir = exportDir + "/" + key + "/" + version;
                File diagramDirFile = new File(diagramDir);
                if (!diagramDirFile.exists()) {
                    diagramDirFile.mkdirs();
                }
                String diagramPath = diagramDir + "/" + diagramResourceName;
                File file = new File(diagramPath);
                FileUtils.copyInputStreamToFile(resourceAsStream,file);
                files.add(diagramPath);
            }
        }
        return files;
    }

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    @Transactional
    public void deleteDeployment(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 删除部署的流程实例
     *
     * @param procInsId    流程实例ID
     * @param deleteReason 删除原因，可为空
     */
    @Transactional
    public void deleteProcIns(String procInsId, String deleteReason) {
        runtimeService.deleteProcessInstance(procInsId, deleteReason);
    }
}
