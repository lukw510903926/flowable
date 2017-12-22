package com.flowable.core.listener;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import com.flowable.core.bean.Countersign;
import com.flowable.core.service.CountersignService;
import com.flowable.core.service.impl.CountersignServiceImpl;
import com.flowable.core.util.Constants;
import com.flowable.core.util.context.ContextFactory;

/**
 * 会签 Title: esflow <br>
 * Description: <br>
 * Copyright: eastcom Copyright (C) 2009 <br>
 * 
 * @author <a href="mailto:lukw@eastcom-sw.com">lukw</a><br>
 * @email:lukw@eastcom-sw.com <br>
 * @version 1.0 <br>
 * @creatdate 2016年4月23日 上午11:44:42 <br>
 *
 */
@Component("completeCountersignTaskListener")
public class CompleteCountersignTaskListener implements TaskListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegateTask) {

		CountersignService countersignService = (CountersignServiceImpl) ContextFactory
				.getBeanByType(CountersignServiceImpl.class);
		String handleResult = (String) delegateTask.getVariable("handleResult");

		String key = delegateTask.getProcessDefinitionId() + ":" + delegateTask.getProcessInstanceId();
		Integer nrOfInstances = (Integer) delegateTask.getVariable("nrOfInstances");
		Integer nrOfCompletedInstances = (Integer) delegateTask.getVariable("nrOfCompletedInstances");
		ArrayList<String> members = CountersignTaskListener.membersCache.get(key);
		if (nrOfInstances != null && nrOfCompletedInstances != null) {
			delegateTask.setVariable(Constants.COUNTER_SIGN, members);
			if (!(nrOfCompletedInstances < nrOfInstances - 1)) {
				CountersignTaskListener.membersCache.remove(key);
			}
		}

		if (StringUtils.isNotBlank(handleResult)) {

			Countersign countersign = new Countersign();
			countersign.setBizId(delegateTask.getVariable(Constants.SYS_BIZ_ID) + "");
			countersign.setProcessDefinitionId(delegateTask.getProcessDefinitionId());
			countersign.setProcessInstanceId(delegateTask.getProcessInstanceId());
			countersign.setTaskAssignee(delegateTask.getAssignee());
			countersign.setTaskId(delegateTask.getId());
			if ("同意".equals(handleResult)) {
				countersign.setResultType(Constants.MEET_YES);
			} else {
				countersign.setResultType(Constants.MEET_NO);
			}
			countersignService.save(countersign);
		}
	}
}
