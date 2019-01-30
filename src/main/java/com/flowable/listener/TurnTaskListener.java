package com.flowable.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.TaskServiceImpl;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.flowable.util.Constants;
import com.flowable.util.ContextFactory;

/**
 * 转派处理 人/角色
 * 
 */
@Component("turnTaskListener")
public class TurnTaskListener implements TaskListener {

	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger("turnTaskListener");

	@Override
	public void notify(DelegateTask delegateTask) {

		String serviceVendor = (String) delegateTask.getVariable("serviceVendor");

		String handleUser = (String) delegateTask.getVariable("handleUser");
		TaskService taskService = (TaskService) ContextFactory.getBeanByType(TaskServiceImpl.class);
		log.info(handleUser);
		if (StringUtils.isNotBlank(handleUser)) {

			List<IdentityLink> links = taskService.getIdentityLinksForTask(delegateTask.getId());
			List<String> result = new ArrayList<String>();
			if (links != null && !links.isEmpty()) {
				for (IdentityLink il : links) {
					if ("candidate".equals(il.getType())) {
						String groupName = il.getGroupId();
						if (StringUtils.isNotEmpty(groupName)) {
							result.add(groupName);
						}
					}
				}
			}

			for (String group : result) {
				taskService.deleteCandidateGroup(delegateTask.getId(), group);
			}
			if (handleUser.startsWith(Constants.BIZ_GROUP)) {
				String[] group = handleUser.split("\\:");
				if (group.length > 1 && StringUtils.isNotBlank(group[1])) {
					delegateTask.addCandidateGroup(group[1]);
					log.info("group handle.....................................");
				}
			} else {
				delegateTask.setAssignee(handleUser);
				log.info("user handle.....................................");
			}
		}
		if (StringUtils.isNotBlank(serviceVendor)) {
			delegateTask.addCandidateGroup(serviceVendor);
		}
	}
}
