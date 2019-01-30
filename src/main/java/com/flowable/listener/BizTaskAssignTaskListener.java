package com.flowable.listener;

import java.util.logging.Logger;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * 获取当前任务处理 传递到下一环节 
 * 	event 设为 complete
 * @author 26223
 * @email ukw@eastcom-sw.com
 * 2016年8月4日
 * esflow
 * com.eastcom.esflow.listener
 */
@Component("bizTaskAssignTaskListener")
public class BizTaskAssignTaskListener implements TaskListener {
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger("bizTaskAssignTaskListener");

	@Override
	public void notify(DelegateTask delegateTask) {
		
		String handleUser = delegateTask.getAssignee();
		logger.info("handleUser : "+handleUser);
		delegateTask.setVariable("handleUser", handleUser);
	}
}
