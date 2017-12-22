package com.flowable.core.bean;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.flowable.core.service.IBizLogService;


public class ActivitiEngineExpressionBean {

	@Autowired
	private IBizLogService bizLogService;

	/**
	 * 判断哪个TASK ID较晚完成;<br>
	 * 第0个为实例ID，其他为任务定义ID
	 * 
	 * @param taskID
	 * @return
	 */
	public String getFirstHistoryTaskID(String... taskID) {
		if (taskID == null || taskID.length <= 1) {
			return null;
		}
		if (taskID.length == 2) {
			return taskID[1];
		}
		List<BizLog> list = bizLogService.loadBizLogs(taskID[0]);
		if (list == null || list.size() <= 0) {
			return null;
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			BizLog bean = list.get(i);
			for (int j = 1; j < taskID.length; j++) {
				if (bean.getTaskID() == null) {
					continue;
				}
				if (bean.getTaskID().equals(taskID[j])) {
					return taskID[j];
				}
			}
		}
		return null;
	}
}
