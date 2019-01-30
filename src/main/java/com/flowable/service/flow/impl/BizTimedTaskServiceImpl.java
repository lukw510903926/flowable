package com.flowable.service.flow.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.dao.flow.BizTimedTaskDao;
import com.flowable.entity.BizInfo;
import com.flowable.entity.BizInfoConf;
import com.flowable.entity.BizTimedTask;
import com.flowable.service.flow.BizTimedTaskService;
import com.flowable.service.flow.IProcessExecuteService;
import com.flowable.util.DateUtils;
import com.flowable.util.service.BaseServiceImpl;

@Service
@Transactional(readOnly = true)
public class BizTimedTaskServiceImpl extends BaseServiceImpl<BizTimedTask> implements BizTimedTaskService {

	@Autowired
	private BizTimedTaskDao bizTimedTaskDao;

	@Autowired
	private IProcessExecuteService processExecuteService;

	private Logger logger = LoggerFactory.getLogger(BizTimedTaskServiceImpl.class);

	@Override
	@Transactional(readOnly = false)
	public void saveTimedTask(BizInfo bizInfo, BizInfoConf bizConf) {

		BizTimedTask bizTimedTask = this.buildTimeTask(bizInfo, bizConf);
		if (StringUtils.isNotBlank(bizTimedTask.getButtonId())) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, 3);
			bizTimedTask.setEndTime(DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd"));
			this.bizTimedTaskDao.save(bizTimedTask);
		}
	}

	@Override
	@Scheduled(cron = "0 20 23 * * ? ")
	@Transactional(readOnly = false)
	public void submitBizTimedTask() {

		BizTimedTask bizTimedTask = new BizTimedTask();
		Date date = new Date();
		bizTimedTask.setEndTime(DateUtils.formatDate(date, "yyyy-MM-dd"));
		List<BizTimedTask> list = this.bizTimedTaskDao.findBizTimedTask(bizTimedTask);
		if (!CollectionUtils.isEmpty(list)) {
			for (BizTimedTask bizTask : list) {
				try {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("base.buttonId", bizTask.getButtonId());
					params.put("base.workNumber", bizTask.getBizId());
					params.put("treatment", "确认");
					params.put("result", "好");
					params.put("handleMessage", "3天未处理,自动提交工单");
					params.put("base.handleName", "用户确认");
					params.put("base.handleResult", "提交");
					processExecuteService.submit(params, null);
					this.deleteTimedTask(bizTask.getId());
				} catch (Exception e) {
					logger.error("sumitBizTimedTask 工单id : {}, 异常信息 : {}", bizTask.getBizId(), e);
				}
			}
		}

	}

	@Transactional(readOnly = false)
	public void deleteTimedTask(String id) {

		this.bizTimedTaskDao.deleteTimedTask(id);
	}

	private BizTimedTask buildTimeTask(BizInfo bizInfo, BizInfoConf bizConf) {

		BizTimedTask bizTimedTask = new BizTimedTask();
		String taskDefKey = bizInfo.getTaskName();
		bizTimedTask.setTaskName(bizInfo.getTaskDefKey());
		bizTimedTask.setTaskDefKey(taskDefKey);
		bizTimedTask.setTaskId(bizConf.getTaskId());
		bizTimedTask.setBizId(bizInfo.getId());
		String buttonId = null;
		if ("userConfirm".equalsIgnoreCase(taskDefKey)) {
			buttonId = "flow11";
		} else if ("userConfirm2".equalsIgnoreCase(taskDefKey)) {
			buttonId = "flow31";
		}
		bizTimedTask.setButtonId(buttonId);
		return bizTimedTask;
	}

	@Override
	public List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask) {

		return this.bizTimedTaskDao.findBizTimedTask(bizTask);
	}
}
