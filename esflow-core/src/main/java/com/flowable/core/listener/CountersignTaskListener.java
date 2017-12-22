package com.flowable.core.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import com.flowable.core.util.Constants;

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
@Component("countersignTaskListener")
public class CountersignTaskListener implements TaskListener {

	private static final long serialVersionUID = 1L;

	public static Map<String, ArrayList<String>> membersCache = new HashMap<String, ArrayList<String>>();

	@Override
	public void notify(DelegateTask delegateTask) {

		@SuppressWarnings("unchecked")
		ArrayList<String> members = (ArrayList<String>) delegateTask.getVariable(Constants.COUNTER_SIGN);
		String key = delegateTask.getProcessDefinitionId() + ":" + delegateTask.getProcessInstanceId();
		if (!membersCache.containsKey(key)) {
			membersCache.put(key, members);
		}
		delegateTask.setVariable(Constants.COUNTER_SIGN, members);
	}
}
