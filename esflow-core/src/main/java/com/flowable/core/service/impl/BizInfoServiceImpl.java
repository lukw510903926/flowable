package com.flowable.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.common.service.BaseServiceImpl;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.BizFile;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.BizInfoConf;
import com.flowable.core.bean.ProcessVariableInstance;
import com.flowable.core.bean.auth.SystemUser;
import com.flowable.core.dao.BizInfoConfDao;
import com.flowable.core.dao.IBizInfoDao;
import com.flowable.core.dao.IProcessVarInstanceDao;
import com.flowable.core.service.IBizFileService;
import com.flowable.core.service.IBizInfoService;
import com.flowable.core.service.act.ActProcessService;
import com.flowable.core.service.auth.ISystemUserService;
import com.flowable.core.util.Constants;
import com.flowable.core.util.WebUtil;

@Service
@Transactional(readOnly = true)
public class BizInfoServiceImpl extends BaseServiceImpl<BizInfo> implements IBizInfoService {

	private Logger logger = LoggerFactory.getLogger("bizInfoServiceImpl");

	@Autowired
	private IBizInfoDao dao;

	@Autowired
	private BizInfoConfDao bizInfoConfDao;

	@Autowired
	private IProcessVarInstanceDao processInstanceDao;

	@Autowired
	private ISystemUserService roleService;

	@Autowired
	private IBizFileService bizFileService;

	@Autowired
	private ActProcessService actProcessService;

	@Override
	public List<BizInfo> getBizByParentId(String parentId) {

		return this.dao.getBizByParentId(parentId);
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
			e.printStackTrace();
		}
	}

	@Transactional
	public void addBizInfo(BizInfo... beans) {
		for (BizInfo bean : beans) {
			dao.save(bean);
		}
	}

	@Transactional
	public void updateBizInfo(BizInfo... beans) {
		for (BizInfo bean : beans) {
			if (bean.getId() == null)
				continue;
			dao.update(bean);
		}
	}

	@Override
	public BizInfo copyBizInfo(String bizId, String processInstanceId, Map<String, Object> variables) {

		BizInfo oldBiz = this.get(bizId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", bizId);
		params.put("status", Constants.BIZ_NEW);
		params.put("parentTaskName", oldBiz.getTaskName());
		PageHelper<BizInfo> page = new PageHelper<BizInfo>();
		page.setPage(-1);
		page.setRows(-1);
		List<BizInfo> list = this.getBizInfoList(params, page).getList();
		BizInfo newBiz = oldBiz.clone();
		newBiz.setId(null);
		if (list == null) {
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
		BizInfoConf bizInfoConf = new BizInfoConf();
		bizInfoConf.setBizInfo(oldBiz);
		this.copyBizInfoConf(newBiz, bizInfoConf);
		this.copyBizFile(bizId, oldBiz, newBiz, username);
		return newBiz;
	}

	private void copyProcessVarInstance(String processInstanceId, BizInfo oldBiz, BizInfo newBiz) {
		
		List<ProcessVariableInstance> processInstances = processInstanceDao
				.loadProcessInstances(oldBiz.getProcessInstanceId());
		if (CollectionUtils.isNotEmpty(processInstances)) {
			processInstances.forEach(oldInstance -> {
				ProcessVariableInstance newInstance = oldInstance.clone();
				newInstance.setId(null);
				newInstance.setBizId(newBiz.getId());
				newInstance.setProcessInstanceId(processInstanceId);
				newInstance.setCreateTime(new Date());
				processInstanceDao.save(newInstance);
			});
		}
	}

	private void copyBizFile(String bizId, BizInfo oldBiz, BizInfo newBiz, String username) {

		List<BizFile> files = bizFileService.loadBizFilesByBizId(bizId, oldBiz.getTaskId());
		if (CollectionUtils.isNotEmpty(files)) {
			files.forEach(oldFile -> {
				BizFile bizFile = oldFile.clone();
				bizFile.setId(null);
				bizFile.setBizInfo(newBiz);
				bizFile.setCreateDate(new Date());
				bizFile.setCreateUser(username);
				bizFileService.addBizFile(bizFile);
			});
		}
	}

	private void copyBizInfoConf(BizInfo newBiz, BizInfoConf bizInfoConf) {

		List<BizInfoConf> bizInfoConfs = this.bizInfoConfDao.findBizInfoConf(bizInfoConf);
		if (CollectionUtils.isNotEmpty(bizInfoConfs)) {
			bizInfoConfs.forEach(bizConf -> {
				BizInfoConf newConf = bizConf.clone();
				newConf.setId(null);
				newConf.setBizInfo(newBiz);
				newConf.setCreateTime(new Date());
				bizInfoConfDao.save(newConf);
			});
		}
	}

	@Transactional
	public void deleteBizInfo(BizInfo... beans) {

		for (BizInfo bean : beans) {
			if (bean.getId() == null)
				continue;
			dao.delete(bean);
		}
	}

	@Transactional
	public void deleteBizInfo(String... ids) {

		for (String id : ids) {
			dao.deleteById(id);
		}
	}

	@Override
	public BizInfo getBizInfo(String id, String loginUser) {

		return dao.getBizInfo(id, loginUser);
	}

	@Override
	public BizInfo getByBizId(String id) {

		return this.get(id);
	}

	@Override
	@Transactional
	public void updateBizByIds(List<String> list) {

		list.forEach(id -> {
			if (StringUtils.isNotBlank(id)) {
				BizInfo bizInfo = this.get(id);
				if (bizInfo != null) {
					bizInfo.setStatus(Constants.BIZ_DELETE);
					this.updateBizInfo(bizInfo);
				}
			}
		});
	}

	@Override
	public PageHelper<BizInfo> getBizInfoList(Map<String, Object> params, PageHelper<BizInfo> page) {

		logger.info("工单查询 params : " + params);
		List<BizInfo> result = new ArrayList<BizInfo>();
		Map<String, SystemUser> userCache = new HashMap<String, SystemUser>();
		Object ct1 = params.get("createTime");
		Object ct2 = params.get("createTime2");
		if (!(ct1 == null && ct2 == null)) {
			if (ct1 == null) {
				params.put("createTime", new Date());
			} else if (ct2 == null) {
				params.put("createTime2", new Date());
			}
		}

		PageHelper<BizInfo> pageHelper = dao.queryWorkOrder(params, page);
		List<BizInfo> list = pageHelper.getList();
		if (CollectionUtils.isNotEmpty(list)) {
			for (BizInfo bizInfo : list) {
				bizInfo.setCreateUser(this.getUserNameCn(bizInfo.getCreateUser(), userCache));
				bizInfo.setTaskAssignee(this.getUserNameCn(bizInfo.getTaskAssignee(), userCache));
				result.add(bizInfo);
			}
		}
		pageHelper.setList(result);
		userCache.clear();
		return pageHelper;
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
		return null;
	}

	@Override
	public void sendEmail(List<String> bizIds) {

	}

	@Override
	public List<BizInfo> getBizInfos(List<String> list) {

		return this.dao.getBizInfos(list);
	}

}
