package com.flowable.oa.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowable.oa.dao.BizInfoMapper;
import com.flowable.oa.service.BizInfoConfService;
import com.flowable.oa.service.auth.ISystemUserService;
import com.flowable.oa.util.Constants;
import com.flowable.oa.util.DateUtils;
import com.flowable.oa.util.WebUtil;
import com.github.pagehelper.PageInfo;
import com.flowable.oa.util.exception.ServiceException;
import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.flowable.oa.entity.BizFile;
import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.entity.BizInfoConf;
import com.flowable.oa.entity.ProcessVariableInstance;
import com.flowable.oa.entity.auth.SystemUser;
import com.flowable.oa.service.IBizFileService;
import com.flowable.oa.service.IBizInfoService;
import com.flowable.oa.service.IVariableInstanceService;
import com.flowable.oa.service.act.ActProcessService;

@Service
public class BizInfoServiceImplImpl extends BaseServiceImpl<BizInfo> implements IBizInfoService {

	private Logger logger = LoggerFactory.getLogger("bizInfoServiceImpl");

	@Autowired
	private BizInfoMapper bizInfoDao;

	@Autowired
	private BizInfoConfService bizInfoConfService;

	@Autowired
	private IVariableInstanceService variableInstanceService;

	@Autowired
	private ISystemUserService roleService;

	@Autowired
	private IBizFileService bizFileService;

	@Autowired
	private ActProcessService actProcessService;

	@Override
	public List<BizInfo> getBizByParentId(String parentId) {

		BizInfo bizInfo = new BizInfo();
		bizInfo.setParentId(parentId);
		return this.findByModel(bizInfo, false);
	}

	@Override
	public List<String> loadBizInfoStatus(String processId) {

		List<String> list = new ArrayList<String>();
		list.add(Constants.BIZ_TEMP);
		list.add(Constants.BIZ_NEW);
		if (StringUtils.isNotBlank(processId)) {
			getProcessStatus(processId, list);
		} else {
			List<ProcessDefinition> processList = actProcessService.findProcessDefinition(null);
			for (ProcessDefinition processDefinition : processList) {
				processId = processDefinition.getId();
				getProcessStatus(processId, list);
			}
		}
		list.add(Constants.BIZ_END);
		return list;
	}

	private void getProcessStatus(String processId, List<String> list) {

		try {
			List<Map<String, Object>> result = actProcessService.getAllTaskByProcessKey(processId);
			for (Map<String, Object> map : result) {
				String status = (String) map.get("name");
				if (!list.contains(status)) {
					list.add(status);
				}
			}
		} catch (Exception e) {
			logger.error("获取工单状态失败 : {}", e);
			throw new ServiceException("获取工单状态失败!");
		}
	}

	@Override
	@Transactional
	public void addBizInfo(BizInfo... beans) {
		for (BizInfo bean : beans) {
			this.save(bean);
		}
	}

	@Override
	@Transactional
	public void updateBizInfo(BizInfo... beans) {

		for (BizInfo bean : beans) {
			if (bean.getId() != null) {
				this.updateNotNull(bean);
			}
		}
	}

	@Override
	public BizInfo copyBizInfo(String bizId, String processInstanceId, Map<String, Object> variables) {

		BizInfo oldBiz = this.selectByKey(bizId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", bizId);
		params.put("status", Constants.BIZ_NEW);
		List<BizInfo> list = this.findBizInfo(params, null).getList();
		BizInfo newBiz = oldBiz.clone();
		newBiz.setId(null);
		if (CollectionUtils.isEmpty(list)) {
			newBiz.setWorkNum(newBiz.getWorkNum() + "-00" + 1);
		} else {
			newBiz.setWorkNum(newBiz.getWorkNum() + "-00" + (list.size() + 1));
		}
		newBiz.setProcessInstanceId(processInstanceId);
		newBiz.setParentId(bizId);
		newBiz.setCreateTime(new Date());
		newBiz.setParentTaskName(oldBiz.getTaskName());
		newBiz.setStatus(Constants.BIZ_NEW);
		String username = WebUtil.getLoginUser().getUsername();
		newBiz.setCreateUser(username);
		this.addBizInfo(newBiz);
		this.copyProcessVarInstance(processInstanceId, oldBiz, newBiz);
		this.copyBizInfoConf(newBiz, oldBiz.getId());
		this.copyBizFile(bizId, oldBiz, newBiz, username);
		return newBiz;
	}

	private void copyProcessVarInstance(String processInstanceId, BizInfo oldBiz, BizInfo newBiz) {

		ProcessVariableInstance instance = new ProcessVariableInstance();
		instance.setBizId(oldBiz.getId());
		instance.setTaskId(Constants.TASK_START);
		List<ProcessVariableInstance> processInstances = variableInstanceService.findVariableInstances(instance, false);
		if (CollectionUtils.isNotEmpty(processInstances)) {
			processInstances.forEach(oldInstance -> {
				ProcessVariableInstance processVariableInstance = oldInstance.clone();
				processVariableInstance.setId(null);
				processVariableInstance.setBizId(newBiz.getId());
				processVariableInstance.setProcessInstanceId(processInstanceId);
				processVariableInstance.setCreateTime(new Date());
				variableInstanceService.save(processVariableInstance);
			});
		}
	}

	private void copyBizFile(String bizId, BizInfo oldBiz, BizInfo newBiz, String username) {

		List<BizFile> files = bizFileService.loadBizFilesByBizId(bizId, oldBiz.getTaskId());
		if (CollectionUtils.isNotEmpty(files)) {
			files.forEach(oldFile -> {
				BizFile bizFile = oldFile.clone();
				bizFile.setId(null);
				bizFile.setBizId(newBiz.getId());
				bizFile.setCreateDate(new Date());
				bizFile.setCreateUser(username);
				bizFileService.addBizFile(bizFile);
			});
		}
	}

	private void copyBizInfoConf(BizInfo newBiz, String bizId) {

		BizInfoConf example = new BizInfoConf();
		example.setBizId(bizId);
		List<BizInfoConf> list = this.bizInfoConfService.findByModel(example, false);
		if (CollectionUtils.isNotEmpty(list)) {
			list.forEach(bizConf -> {
				BizInfoConf newConf = bizConf.clone();
				newConf.setId(null);
				newConf.setBizId(newBiz.getId());
				newConf.setCreateTime(new Date());
				bizInfoConfService.save(newConf);
			});
		}
	}

	@Override
	@Transactional
	public void deleteBizInfo(BizInfo... beans) {

		for (BizInfo bean : beans) {
			this.deleteById(bean.getId());
		}
	}

	@Override
	@Transactional
	public void deleteBizInfo(String... ids) {

		for (String id : ids) {
			this.deleteById(id);
		}
	}

	@Override
	public BizInfo getByBizId(String id) {

		return this.selectByKey(id);
	}

	@Override
	@Transactional
	public void updateBizByIds(List<String> list) {

		list.forEach(id -> {
			if (StringUtils.isNotBlank(id)) {
				BizInfo bizInfo = this.selectByKey(id);
				if (bizInfo != null) {
					bizInfo.setStatus(Constants.BIZ_DELETE);
					this.updateBizInfo(bizInfo);
				}
			}
		});
	}

	@Override
	public PageInfo<BizInfo> findBizInfo(Map<String, Object> params, PageInfo<BizInfo> page) {

		logger.info("工单查询 params : " + params);
		String action = (String) params.get("action");
		String createTime = (String) params.get("createTime");
		String createTime2 = (String) params.get("createTime2");
		if (!(createTime == null && createTime2 == null)) {
			String key = createTime == null ? "createTime" : "createTime2";
			params.put(key, new Date());
		}
		Date dt1 = DateUtils.parseDate(createTime);
		Date dt2 = DateUtils.parseDate(createTime2);
		if (dt1 == null) {
			params.remove("createTime");
		} else {
			params.put("createTime", dt1);
		}
		if (dt2 == null) {
			params.remove("createTime2");
		} else {
			params.put("createTime2", dt2);
		}
		if ("myCreate".equalsIgnoreCase(action)) {
			params.remove("createUser");
		} else if ("myClose".equalsIgnoreCase(action)) {
			params.put("status", Constants.BIZ_END);
		} else if ("myTemp".equalsIgnoreCase(action)) {
			params.put("status", "草稿");
			params.remove("createUser");
		}
		Map<String, SystemUser> userCache = new HashMap<String, SystemUser>();
		if (page != null) {
			PageHelper.startPage(page.getPageNum(), page.getPageSize());
		}
		List<BizInfo> list = bizInfoDao.queryWorkOrder(params);
		if (CollectionUtils.isNotEmpty(list)) {
			for (BizInfo bizInfo : list) {
				bizInfo.setCreateUser(this.getUserNameCn(bizInfo.getCreateUser(), userCache));
				bizInfo.setTaskAssignee(this.getUserNameCn(bizInfo.getTaskAssignee(), userCache));
			}
		}
		userCache.clear();
		return new PageInfo<>(list);
	}

	private String getUserNameCn(String username, Map<String, SystemUser> userCache) {

		SystemUser loginUser = userCache.get(username);
		if (loginUser == null) {
			loginUser = this.roleService.getUserByUsername(username);
		}
		if (loginUser != null) {
			userCache.put(username, loginUser);
			return loginUser.getName();
		}
		return username;
	}
}
