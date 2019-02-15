package com.flowable.oa.service.act;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.flowable.oa.service.IProcessDefinitionService;
import com.flowable.oa.util.exception.ServiceException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 流程定义相关Controller
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
@Service
public class ActProcessService {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IProcessDefinitionService processService;

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

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().latestVersion().orderByProcessDefinitionKey().asc();
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();
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
     * @throws Exception
     */
    public List<Map<String, Object>> getAllTaskByProcessKey(String processId) throws Exception {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        InputStream bpmnStream = resourceRead(processId, null, "xml");
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
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
                Map<String, Object> temp = new HashMap<String, Object>();
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
     * @param processInstanceId   流程实例ID
     * @param resourceType        资源类型(xml|image)
     */
    public InputStream resourceRead(String processDefinitionId, String processInstanceId, String resourceType) throws Exception {

        if (!StringUtils.isBlank(processInstanceId)) {
            ProcessInstance processInstance = processService.getProcessInstance(processInstanceId);
            processDefinitionId = processInstance.getProcessDefinitionId();
        }
        ProcessDefinition processDefinition = null;
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().latestVersion().orderByProcessDefinitionKey().asc();
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.list();
        if (CollectionUtils.isNotEmpty(processDefinitions)) {
            for (ProcessDefinition temp : processDefinitions) {
                // if (temp.getKey().equals(procDefId)) {
                if (temp.getId().equals(processDefinitionId)) {
                    processDefinition = temp;
                    break;
                }
            }
        }

        String resourceName = "";
        if (resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }

        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        return resourceAsStream;
    }

    /**
     * 部署流程 - 保存
     *
     * @param file
     * @return
     */
    @Transactional
    public String deploy(String exportDir, String category, MultipartFile file) {

        String message = "";

        String fileName = file.getOriginalFilename();

        try {
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;
            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else if (extension.equals("png")) {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if (fileName.indexOf("bpmn20.xml") != -1) {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if (extension.equals("bpmn")) { // bpmn扩展名特殊处理，转换为bpmn20.xml
                String baseName = FilenameUtils.getBaseName(fileName);
                deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
            } else {
                message = "不支持的文件类型：" + extension;
            }

            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

            // 设置流程分类
            for (ProcessDefinition processDefinition : list) {
                // ActUtils.exportDiagramToFile(repositoryService,
                // processDefinition, exportDir);
                repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
                message += "部署成功，流程ID=" + processDefinition.getId() + "<br/>";
            }

            if (list.size() == 0) {
                message = "部署失败，没有流程。";
            }

        } catch (Exception e) {
            throw new ServiceException("部署失败！", e);
        }
        return message;
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
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @Transactional
    public Model convertToModel(String procDefId) throws UnsupportedEncodingException, XMLStreamException {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
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

        repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

        return modelData;
    }

    /**
     * 导出图片文件到硬盘
     */
    public List<String> exportDiagrams(String exportDir) throws IOException {
        List<String> files = new ArrayList<String>();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();

        for (ProcessDefinition processDefinition : list) {
            String diagramResourceName = processDefinition.getDiagramResourceName();
            String key = processDefinition.getKey();
            int version = processDefinition.getVersion();
            String diagramPath = "";

            InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName);
            byte[] b = new byte[resourceAsStream.available()];

            @SuppressWarnings("unused")
            int len = -1;
            resourceAsStream.read(b, 0, b.length);
            // create file if not exist
            String diagramDir = exportDir + "/" + key + "/" + version;
            File diagramDirFile = new File(diagramDir);
            if (!diagramDirFile.exists()) {
                diagramDirFile.mkdirs();
            }
            diagramPath = diagramDir + "/" + diagramResourceName;
            File file = new File(diagramPath);
            // 文件存在退出
            if (file.exists()) {
                // 文件大小相同时直接返回否则重新创建文件(可能损坏)
                logger.debug("diagram exist, ignore... : {}", diagramPath);
                files.add(diagramPath);
            } else {
                file.createNewFile();
                logger.debug("export diagram to : {}", diagramPath);
                FileUtils.writeByteArrayToFile(file, b, true);
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
