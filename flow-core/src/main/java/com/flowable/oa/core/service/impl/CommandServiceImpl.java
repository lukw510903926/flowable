package com.flowable.oa.core.service.impl;

import java.util.Date;
import java.util.Map;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.BizInfoConf;
import com.flowable.oa.core.service.*;
import com.flowable.oa.core.util.exception.ServiceException;
import com.flowable.oa.core.util.flowable.CommonJumpTaskCmd;
import org.apache.commons.collections.MapUtils;
import org.flowable.engine.ManagementService;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p> 节点跳转
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/9 18:46
 **/
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

	private Logger logger = LoggerFactory.getLogger(CommandServiceImpl.class);

	@Override
	@Transactional
	public BizInfo jumpCommand(Map<String,Object> params) {

		try {
			String bizId = MapUtils.getString(params,"base.bizId");
			BizInfo bizInfo = this.bizInfoService.selectByKey(bizId);
			if (bizInfo == null) {
				throw new ServiceException("工单不存在!工单id:{}" + bizId);
			}
			BizInfoConf conf = this.bizInfoConfService.getMyWork(bizId);
			if (conf == null) {
				throw new ServiceException("请确认是否有提交工单权限!");
			}
			String targetTaskDefKey = MapUtils.getString(params,"base.taskDefKey");
			String taskId = conf.getTaskId();
			Task task = processDefinitionService.getTaskBean(taskId);
			CommonJumpTaskCmd cmd = new CommonJumpTaskCmd(taskId, targetTaskDefKey);
			managementService.executeCommand(cmd);
			processExecuteService.updateBizTaskInfo(bizInfo);
			processExecuteService.writeBizLog(bizInfo, task, new Date(), params);
			return bizInfo;
		} catch (Exception e) {
			logger.error(" 流程跳转失败 : {}", e);
			throw new ServiceException("流程跳转失败!");
		}
	}
}
