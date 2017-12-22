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
		sql.append(" left join (select c.biz_id as bizId,group_concat(c.task_assignee) as task_Assignee,");
		sql.append(" group_concat(c.task_id) as task_Id	from esflow.act_biz_info_conf c where c.biz_id = ? ");
		list.add(id);
		if (StringUtils.isNotBlank(taskAssignee) && StringUtils.isNotBlank(loginUser)) {
			sql.append(" and (c.task_assignee = ? ");
			list.add(loginUser);
			SystemUser user = this.systemUserDao.getUserByUsername(loginUser);
			if (user != null) {
				Set<SystemRole> roles = user.getRoles();
				for (SystemRole role : roles) {
					sql.append("or c.task_assignee like ? ");
					list.add("%," + role.getNameCn() + ",%");
				}
			}
			sql.append(" )");
		}
		sql.append(" group by c.biz_id limit 0,1 ) conf on bean.id = conf.bizId where 1 = 1 and bean.id = ?");
		list.add(id);
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
		sql.append(" join (select c.biz_id as bizId,group_concat(c.task_assignee) as task_Assignee,");
		sql.append(" group_concat(c.task_id) as task_Id from esflow.act_biz_info_conf c where 1=1 ");
		String taskAssignee = (String) params.get("taskAssignee");
		if (params.containsKey("checkAssignee")) {
			sql.append(" and ( c.task_assignee = ? ");
			args.add(loginUser);
			SystemUser user = this.systemUserDao.getUserByUsername(loginUser);
			if (user != null) {
				Set<SystemRole> roles = user.getRoles();
				for (SystemRole role : roles) {
					sql.append("or c.task_assignee like ? ");
					args.add("%," + role.getNameCn() + ",%");
				}
			}
			sql.append(")");
		}
		if (StringUtils.isNotEmpty(taskAssignee)) {
			List<SystemUser> list = this.systemUserDao.findSystemUser(new SystemUser(null, taskAssignee));
			if (CollectionUtils.isNotEmpty(list)) {
				sql.append(" and  c.task_assignee in (");
				for (SystemUser systemUser : list) {
					String username = systemUser.getUsername();
					if (StringUtils.isNotBlank(username)) {
						sql.append("?,");
						args.add(username);
					}
				}
				sql = sql.deleteCharAt(sql.length() - 1).append(")");
			} else {
				sql.append(" and c.task_assignee like ? ");
				args.add("%" + taskAssignee + "%");
			}
		}
		sql.append(" group by c.biz_id) conf on bean.id = conf.bizId ");
		sql.append(" where bean.biz_status <> ? ");
		args.add(Constants.BIZ_DELETE);
		String bizId = (String) params.get("bizId");
		if (StringUtils.isNotEmpty(bizId)) {
			sql.append(" and bean.work_num like ?");
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
				sql.append(" and bean.create_user in (");
				for (SystemUser systemUser : list) {
					String username = systemUser.getUsername();
					if (StringUtils.isNotBlank(username)) {
						sql.append("?,");
						args.add(username);
					}
				}
				sql = sql.deleteCharAt(sql.length() - 1).append(")");
			} else {
				sql.append(" and bean.create_user like ? ");
				args.add("%" + createUser + "%");
			}
		}
		String parentId = (String) params.get("parentId");
		if (StringUtils.isNotEmpty(parentId)) {
			sql.append(" and bean.parent_id = ?");
			args.add(parentId);
		}
		String parentTaskName = (String) params.get("parentTaskName");
		if (StringUtils.isNotEmpty(parentTaskName)) {
			sql.append(" and bean.parent_taskname = ?");
			args.add(parentTaskName);
		}
		String bizType = (String) params.get("bizType");
		if (StringUtils.isNotEmpty(bizType)) {
			sql.append(" and bean.biz_type = ?");
			args.add(bizType);
		}
		String status = (String) params.get("status");
		if (StringUtils.isNotEmpty(status)) {
			sql.append(" and bean.biz_status = ? ");
			args.add(status);
		}
		String action = (String) params.get("action");
		if ("myHandle".equalsIgnoreCase(action)) {
			sql.append(" and bean.id in (");
			sql.append("select distinct bean.work_num FROM act_biz_log bean WHERE bean.handle_user =? )");
			args.add(loginUser);
		}
		if ("myWork".equalsIgnoreCase(action)) {
			sql.append(" and (bean.biz_status <> ? )");
			args.add(Constants.BIZ_TEMP);
		}
		if ("myTemp".equalsIgnoreCase(action) || "myCreate".equalsIgnoreCase(action)) {
			sql.append(" and bean.create_user = ? ");
			args.add(loginUser);
		}
		String taskDefKey = (String) params.get("taskDefKey");
		if (StringUtils.isNotEmpty(taskDefKey)) {
			sql.append(" and bean.task_def_key = ?");
			args.add(taskDefKey);
		}
		if (params.get("createTime") != null && params.get("createTime2") != null) {
			if (StringUtils.isNotBlank(params.get("createTime").toString())
					&& StringUtils.isNotBlank(params.get("createTime2").toString())) {
				sql.append(" and bean.create_time between ? and ? ");
				args.add(params.get("createTime"));
				args.add(params.get("createTime2"));
			}
		}
		sql.append(" order by bean.create_time desc ");
		logger.info("args : " + args);
		return this.findBySql(page, sql.toString(), args.toArray(), BizInfo.class);
	}

	private String getLoginUser(Map<String, Object> params) {

		LoginUser user = WebUtil.getLoginUser();
		String loginUser = user == null ? (String) params.get("loginUser") : user.getUsername();
		return loginUser;
	}

	private StringBuilder buildSql() {

		StringBuilder sql = new StringBuilder(" select bean.id as id,bean.work_num ,bean.title as title,");
		sql.append(
				" bean.biz_Type,bean.process_Definition_Id,bean.process_Instance_Id,conf.task_Id,conf.task_Assignee,");
		sql.append(
				" bean.task_Name,bean.task_Def_Key,bean.press_Count,bean.create_User,bean.limit_Time,bean.create_Time,");
		sql.append(" bean.biz_status,bean.source,bean.parent_Id,bean.parent_TaskName from esflow.act_biz_info bean ");
		return sql;
	}

}
