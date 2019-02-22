package com.flowable.oa.core.service.impl;

import java.util.Calendar;
import java.util.List;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.BizInfoConf;
import com.flowable.oa.core.entity.BizTimedTask;
import com.flowable.oa.core.service.BizTimedTaskService;
import com.flowable.oa.core.util.DateUtils;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BizTimedTaskServiceImplImpl extends BaseServiceImpl<BizTimedTask> implements BizTimedTaskService {

	@Override
	@Transactional
	public void saveTimedTask(BizInfo bizInfo, BizInfoConf bizConf) {

		BizTimedTask bizTimedTask = this.buildTimeTask(bizInfo, bizConf);
		if (StringUtils.isNotBlank(bizTimedTask.getButtonId())) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, 3);
			bizTimedTask.setEndTime(DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd"));
			this.save(bizTimedTask);
		}
	}

	@Override
	@Transactional
	public void submitBizTimedTask() {


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
}
