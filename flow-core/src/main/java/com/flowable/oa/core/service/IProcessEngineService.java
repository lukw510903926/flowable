package com.flowable.oa.core.service;

import com.flowable.oa.core.vo.ProcessDefinitionEntityVo;
import com.github.pagehelper.PageInfo;
import org.flowable.engine.repository.Model;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午10:02
 **/
public interface IProcessEngineService {

    /**
     * 获取流程节点
     *
     * @param processId
     * @return
     */
    Set<String> loadProcessStatus(String processId);

    /**
     * 流程定义列表
     *
     * @param processDefinition
     * @return
     */
    PageInfo<ProcessDefinitionEntityVo> processList(ProcessDefinitionEntityVo processDefinition);

    /**
     * 流程定义列表
     *
     * @param page
     * @param procInsId
     * @param procDefKey
     * @return
     */
    PageInfo<ProcessInstance> runningList(PageInfo<ProcessInstance> page, String procInsId, String procDefKey);

    /**
     * 根据流程key得到
     *
     * @param processId
     * @return
     */
    List<Map<String, Object>> getAllTaskByProcessKey(String processId);

    /**
     * 读取资源，通过部署ID
     *
     * @param processDefinitionId 流程定义ID
     * @param resourceType        资源类型(xml|image)
     * @return
     */
    InputStream resourceRead(String processDefinitionId, String resourceType);

    /**
     * 部署流程 - 保存
     *
     * @param category
     * @param file
     * @return
     */
    String deploy(String category, MultipartFile file);

    /**
     * 将部署的流程转换为模型
     *
     * @param procDefId
     * @return
     */
    Model convertToModel(String procDefId);

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    void deleteDeployment(String deploymentId);

    /**
     * 删除部署的流程实例
     *
     * @param procInsId    流程实例ID
     * @param deleteReason 删除原因，可为空
     */
    void deleteProcIns(String procInsId, String deleteReason);
}
