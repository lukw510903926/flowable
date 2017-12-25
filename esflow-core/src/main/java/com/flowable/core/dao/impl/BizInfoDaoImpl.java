package com.flowable.core.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.common.utils.LoginUser;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.auth.SystemRole;
import com.flowable.core.bean.auth.SystemUser;
import com.flowable.core.dao.BizInfoConfDao;
import com.flowable.core.dao.IBizInfoDao;
import com.flowable.core.dao.auth.ISystemUserDao;
import com.flowable.core.util.Constants;
import com.flowable.core.util.WebUtil;

@Repository
public class BizInfoDaoImpl extends BaseDaoImpl<BizInfo> implements IBizInfoDao {

	@Autowired
	private BizInfoConfDao bizInfoConfDao;

	@Autowired
	private ISystemUserDao systemUserDao;

	private Logger logger = LoggerFactory.getLogger("bizInfoDaoImpl");

	@Override
	public List<BizInfo> getBizByParentId(String parentId) {

		if (StringUtils.isBlank(parentId))
			return null;
		StringBuilder hql = new StringBuilder(" from BizInfo a where a.parentId = ? ");
		return this.find(hql.toString(), new Object[] { parentId });
	}

	@Override
	public BizInfo getBizInfo(String id, String loginUser) {

		StringBuilder sql = this.buildSql();
		List<Object> list = new ArrayList<Object>();
		String taskAssignee = this.bizInfoConfDao.getTaskAssignee(id);
		sql.append(" WHERE BEAN.ID = ? AND EXISTS (SELECT C.BIZ_ID FROM ESFLOW.ACT_BIZ_INFO_CONF C WHERE C.BIZ_ID= ? ");
		list.add(id);
		list.add(id);
		if (StringUtils.isNotBlank(taskAssignee) && StringUtils.isNotBlank(loginUser)) {
			SystemUser systemUser = this.systemUserDao.getUserByUsername(loginUser);
			sql.append(" AND (C.TASK_ASSIGNEE = ? ");
			list.add(loginUser);
			if (systemUser != null) {
				Set<SystemRole> roles = systemUser.getRoles();
				for (SystemRole role : roles) {
					sql.append("OR C.TASK_ASSIGNEE LIKE ? ");
					list.add("%" + role.getNameCn() + "%");
				}
			}
			sql.append(" )");
		}
		sql.append(" AND BEAN.ID = C.BIZ_ID )");
		List<BizInfo> result = this.findBySql(sql.toString(), list.toArray(), BizInfo.class);
		return result.size() > 0 ? result.get(0) : null;
	}

	@Override
	public List<BizInfo> getBizInfos(List<String> list) {

		StringBuilder hql = new StringBuilder("FROM BizInfo bean WHERE bean.bizId in (");
		List<String> params = new ArrayList<String>();
		for (String id : list) {
			hql.append(" ? ,");
			params.add(id);
		}
		hql = hql.deleteCharAt(hql.length() - 1);
		hql.append(")");
		return this.find(hql.toString(), params.toArray());
	}

	@Override
	public PageHelper<BizInfo> queryWorkOrder(Map<String, Object> params, PageHelper<BizInfo> page) {

		StringBuilder sql = this.buildSql();
		String loginUser = this.getLoginUser(params);
		List<Object> args = new ArrayList<Object>();
		sql.append(" WHERE BEAN.BIZ_STATUS <> ? ");
		args.add(Constants.BIZ_DELETE);
		String bizId = (String) params.get("bizId");
		if (StringUtils.isNotEmpty(bizId)) {
			sql.append(" AND BEAN.WORK_NUM LIKE ?");
			args.add("%" + bizId + "%");
		}
		String title = (String) params.get("title");
		if (StringUtils.isNotEmpty(title)) {
			sql.append(" and bean.title like ?");
			args.add("%" + title + "%");
		}
		String createUser = (String) params.get("createUser");
		if (StringUtils.isNotEmpty(createUser)) {
			List<SystemUser> list = this.systemUserDao.findSystemUser(new SystemUser(null, createUser));
			if (CollectionUtils.isNotEmpty(list)) {
				sql.append(" AND BEAN.CREATE_USER IN (");
				for (SystemUser systemUser : list) {
					String username = systemUser.getUsername();
					if (StringUtils.isNotBlank(username)) {
						sql.append("?,");
						args.add(username);
					}
				}
				sql = sql.deleteCharAt(sql.length() - 1).append(")");
			} else {
				sql.append(" AND BEAN.CREATE_USER LIKE ? ");
				args.add("%" + createUser + "%");
			}
		}
		String parentId = (String) params.get("parentId");
		if (StringUtils.isNotEmpty(parentId)) {
			sql.append(" AND BEAN.PARENT_ID = ?");
			args.add(parentId);
		}
		String parentTaskName = (String) params.get("parentTaskName");
		if (StringUtils.isNotEmpty(parentTaskName)) {
			sql.append(" AND BEAN.PARENT_TASKNAME = ?");
			args.add(parentTaskName);
		}
		String bizType = (String) params.get("bizType");
		if (StringUtils.isNotEmpty(bizType)) {
			sql.append(" AND BEAN.BIZ_TYPE = ?");
			args.add(bizType);
		}
		String status = (String) params.get("status");
		if (StringUtils.isNotEmpty(status)) {
			sql.append(" AND BEAN.BIZ_STATUS = ? ");
			args.add(status);
		}
		String action = (String) params.get("action");
		if ("myHandle".equalsIgnoreCase(action)) {
			sql.append(" AND EXISTS (SELECT LOG.BIZ_ID FROM");
			sql.append(" ACT_BIZ_LOG LOG WHERE LOG.HANDLE_USER =? AND BEAN.ID =LOG.BIZ_ID)");
			args.add(loginUser);
		}
		if ("myWork".equalsIgnoreCase(action)) {
			sql.append(" AND BEAN.BIZ_STATUS <> ? ");
			args.add(Constants.BIZ_TEMP);
			sql.append(" and EXISTS (SELECT c.biz_id FROM esflow.act_biz_info_conf c WHERE c.task_assignee = ? ");
			args.add(loginUser);
			SystemUser user = this.systemUserDao.getUserByUsername(loginUser);
			if (user != null) {
				Set<SystemRole> roles = user.getRoles();
				for (SystemRole role : roles) {
					sql.append("OR C.TASK_ASSIGNEE LIKE ? ");
					args.add("%," + role.getNameCn() + ",%");
				}
			}
			sql.append(" AND BEAN.ID = C.BIZ_ID )");
		}
		String taskAssignee = (String) params.get("taskAssignee");
		if (StringUtils.isNotEmpty(taskAssignee)) {
			sql.append(" AND EXISTS (SELECT C.BIZ_ID FROM ESFLOW.ACT_BIZ_INFO_CONF C WHERE ");
			sql.append(" C.TASK_ASSIGNEE LIKE ? ");
			args.add("%" + taskAssignee + "%");
			List<SystemUser> list = this.systemUserDao.findSystemUser(new SystemUser(null, taskAssignee));
			if (CollectionUtils.isNotEmpty(list)) {
				sql.append(" OR C.TASK_ASSIGNEE IN (");
				for (SystemUser systemUser : list) {
					String username = systemUser.getUsername();
					if (StringUtils.isNotBlank(username)) {
						sql.append("?,");
						args.add(username);
					}
				}
				sql = sql.deleteCharAt(sql.length() - 1).append(")");
			}
			sql.append(" AND BEAN.ID = C.BIZ_ID )");
		}
		if ("myTemp".equalsIgnoreCase(action) || "myCreate".equalsIgnoreCase(action)) {
			sql.append(" AND BEAN.CREATE_USER = ? ");
			args.add(loginUser);
		}
		String taskDefKey = (String) params.get("taskDefKey");
		if (StringUtils.isNotEmpty(taskDefKey)) {
			sql.append(" AND BEAN.TASK_DEF_KEY = ?");
			args.add(taskDefKey);
		}
		if (params.get("createTime") != null && params.get("createTime2") != null) {
			sql.append(" AND BEAN.CREATE_TIME BETWEEN ? AND ? ");
			args.add(params.get("createTime"));
			args.add(params.get("createTime2"));
		}
		sql.append(" ORDER BY BEAN.CREATE_TIME DESC ");
		logger.info("args : " + args);
		return this.findBySql(page, sql.toString().toUpperCase(), args.toArray(), BizInfo.class);
	}

	private String getLoginUser(Map<String, Object> params) {

		LoginUser loginUser = WebUtil.getLoginUser();
		return loginUser == null ? (String) params.get("loginUser") : loginUser.getUsername();
	}

	private StringBuilder buildSql() {

		StringBuilder sql = new StringBuilder(" SELECT BEAN.ID AS ID,BEAN.WORK_NUM ,BEAN.TITLE AS TITLE,");
		sql.append(" BEAN.BIZ_TYPE,BEAN.PROCESS_DEFINITION_ID,BEAN.PROCESS_INSTANCE_ID,");
		sql.append(" BEAN.TASK_ASSIGNEE,BEAN.TASK_ID, BEAN.TASK_NAME,BEAN.TASK_DEF_KEY,");
		sql.append(" BEAN.CREATE_USER,BEAN.LIMIT_TIME,BEAN.CREATE_TIME,BEAN.BIZ_STATUS,");
		sql.append(" BEAN.SOURCE,BEAN.PARENT_ID,BEAN.PARENT_TASKNAME FROM ESFLOW.ACT_BIZ_INFO BEAN ");
		return sql;
	}
}
