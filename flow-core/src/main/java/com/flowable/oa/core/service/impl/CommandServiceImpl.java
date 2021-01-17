package com.flowable.oa.core.service.impl;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.BizInfoConf;
import com.flowable.oa.core.service.BizInfoConfService;
import com.flowable.oa.core.service.CommandService;
import com.flowable.oa.core.service.IBizInfoService;
import com.flowable.oa.core.service.IProcessDefinitionService;
import com.flowable.oa.core.service.IProcessExecuteService;
import com.flowable.oa.core.util.exception.ServiceException;
import com.flowable.oa.core.util.flowable.CommonJumpTaskCmd;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.flowable.engine.ManagementService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


/**
 * <p> 节点跳转
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/9 18:46
 **/
@Slf4j
@Service
public class CommandServiceImpl implements CommandService {

    @Autowired
    private ManagementService managementService;

    @Autowired
    private BizInfoConfService bizInfoConfService;

    @Autowired
    private IBizInfoService bizInfoService;

    @Autowired
    private IProcessExecuteService processExecuteService;

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizInfo jumpCommand(Map<String, Object> params) {

        try {
            Long bizId = MapUtils.getLong(params, "base.bizId");
            BizInfo bizInfo = this.bizInfoService.selectByKey(bizId);
            if (bizInfo == null) {
                throw new ServiceException("工单不存在!工单id:{}" + bizId);
            }
            BizInfoConf conf = this.bizInfoConfService.getMyWork(bizId);
            if (conf == null) {
                throw new ServiceException("请确认是否有提交工单权限!");
            }
            String targetTaskDefKey = MapUtils.getString(params, "base.taskDefKey");
            String taskId = conf.getTaskId();
            Task task = processDefinitionService.getTaskBean(taskId);
            CommonJumpTaskCmd cmd = CommonJumpTaskCmd.buildJumpTaskCmd(taskId, targetTaskDefKey);
            managementService.executeCommand(cmd);
            processExecuteService.updateBizTaskInfo(bizInfo);
            processExecuteService.writeBizLog(bizInfo, task, new Date(), params);
            return bizInfo;
        } catch (Exception e) {
            log.error(" 流程跳转失败 : ", e);
            throw new ServiceException("流程跳转失败!");
        }
    }
}
