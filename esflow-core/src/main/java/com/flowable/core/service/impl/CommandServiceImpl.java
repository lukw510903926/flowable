package com.flowable.core.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.ManagementService;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.common.exception.ServiceException;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.BizInfoConf;
import com.flowable.core.service.BizInfoConfService;
import com.flowable.core.service.CommandService;
import com.flowable.core.service.IBizInfoService;
import com.flowable.core.service.IProcessDefinitionService;
import com.flowable.core.util.flowable.CommonJumpTaskCmd;

/**
 * 
 * @author 26223
 *
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
	private IProcessDefinitionService processDefinitionService;

	private Logger logger = LoggerFactory.getLogger(CommandServiceImpl.class);

	@Override
	@Transactional(readOnly = false)
	public void jumpCommand(String bizId, String taskId, String targetTaskDefKey) {

		try {
			CommonJumpTaskCmd cmd = new CommonJumpTaskCmd(taskId, targetTaskDefKey);
			managementService.executeCommand(cmd);
			this.updateBizInfo(bizId, targetTaskDefKey);
		} catch (Exception e) {
			logger.error(" 流程跳转失败 : {}", e);
			throw new ServiceException("流程跳转失败!");
		}
	}

	private void updateBizInfo(String bizId, String targetTaskDefKey) {

		BizInfo bizInfo = this.bizInfoService.get(bizId);
		if(bizInfo == null){
			return;
		}
		BizInfoConf example = new BizInfoConf();
		example.setBizInfo(bizInfo);
		List<BizInfoConf> confs = this.bizInfoConfService.findBizInfoConf(example);
		this.bizInfoConfService.delete(confs);
		List<Task> list = processDefinitionService.getNextTaskInfo(bizInfo.getProcessInstanceId());
		if (!CollectionUtils.isEmpty(list)) {
			String taskIds = "";
			String assignee = "";
			for (Task task : list) {
				BizInfoConf conf = new BizInfoConf();
				conf.setTaskAssignee(task.getAssignee());
				conf.setBizInfo(bizInfo);
				conf.setCreateTime(new Date());
				conf.setTaskId(task.getId());
				this.bizInfoConfService.save(conf);
				taskIds = taskIds + task.getId() + ",";
				if (StringUtils.isNotBlank(task.getAssignee())) {
					assignee = assignee + task.getAssignee() + ",";
				}
			}
			bizInfo.setTaskId(taskIds);
			assignee = StringUtils.isBlank(assignee) ? null : assignee.substring(0, assignee.lastIndexOf(","));
			bizInfo.setTaskAssignee(assignee);
			bizInfo.setTaskDefKey(targetTaskDefKey);
			bizInfo.setStatus(list.get(0).getName());
			bizInfo.setTaskName(list.get(0).getName());
			this.bizInfoService.update(bizInfo);
		}
	}
}
