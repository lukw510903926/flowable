package com.flowable.core.service.impl;

import java.util.List;

import org.flowable.engine.ManagementService;
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
import com.flowable.core.service.IProcessExecuteService;
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
	private IProcessExecuteService processExecuteService;

	private Logger logger = LoggerFactory.getLogger(CommandServiceImpl.class);

	@Override
	@Transactional(readOnly = false)
	public void jumpCommand(String bizId, String taskId, String targetTaskDefKey) {

		try {
			BizInfo bizInfo = this.bizInfoService.get(bizId);
			if (bizInfo == null) {
				throw new ServiceException("工单不存在!工单id:{}" + bizId);
			}
			BizInfoConf conf = this.bizInfoConfService.getMyWork(bizId);
			if (conf == null) {
				throw new ServiceException("请确认是否有提交工单权限");
			}
			CommonJumpTaskCmd cmd = new CommonJumpTaskCmd(taskId, targetTaskDefKey);
			managementService.executeCommand(cmd);
			BizInfoConf example = new BizInfoConf();
			example.setBizInfo(bizInfo);
			example.setTaskId(taskId);
			List<BizInfoConf> confs = this.bizInfoConfService.findBizInfoConf(example);
			processExecuteService.updateBizTaskInfo(bizInfo, conf);
			this.bizInfoConfService.delete(confs);
		} catch (Exception e) {
			logger.error(" 流程跳转失败 : {}", e);
			throw new ServiceException("流程跳转失败!");
		}
	}
}
