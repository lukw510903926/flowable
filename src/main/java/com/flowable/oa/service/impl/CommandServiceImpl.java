package com.flowable.oa.service.impl;

import java.util.Date;
import java.util.Map;

import com.flowable.oa.service.BizInfoConfService;
import com.flowable.oa.service.CommandService;
import com.flowable.oa.util.exception.ServiceException;
import org.flowable.engine.ManagementService;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.entity.BizInfoConf;
import com.flowable.oa.service.IBizInfoService;
import com.flowable.oa.service.IProcessDefinitionService;
import com.flowable.oa.service.IProcessExecuteService;
import com.flowable.oa.util.flowable.CommonJumpTaskCmd;

/**
 * 
 * @project : esflow-core
 * @createTime : 2017年12月26日 : 下午3:03:47
 * @author : lukewei
 * @description :
 */
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
			String bizId = (String) params.get("base.bizId");
			BizInfo bizInfo = this.bizInfoService.selectByKey(bizId);
			if (bizInfo == null) {
				throw new ServiceException("工单不存在!工单id:{}" + bizId);
			}
			BizInfoConf conf = this.bizInfoConfService.getMyWork(bizId);
			if (conf == null) {
				throw new ServiceException("请确认是否有提交工单权限!");
			}
			String targetTaskDefKey = (String)params.get("base.taskDefKey");
			String taskId = conf.getTaskId();
			Task task = processDefinitionService.getTaskBean(taskId);
			CommonJumpTaskCmd cmd = new CommonJumpTaskCmd(taskId, targetTaskDefKey);
			managementService.executeCommand(cmd);
			processExecuteService.updateBizTaskInfo(bizInfo, conf);
			processExecuteService.writeBizLog(bizInfo, task, new Date(), params);
			this.bizInfoService.updateBizInfo(bizInfo);
			this.bizInfoConfService.saveOrUpdate(conf);
			return bizInfo;
		} catch (Exception e) {
			logger.error(" 流程跳转失败 : {}", e);
			throw new ServiceException("流程跳转失败!");
		}
	}
}
