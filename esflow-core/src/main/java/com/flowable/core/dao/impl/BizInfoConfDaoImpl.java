package com.flowable.core.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.common.utils.LoginUser;
import com.flowable.core.bean.BizInfoConf;
import com.flowable.core.dao.BizInfoConfDao;
import com.flowable.core.util.WebUtil;

/**
 * @author 2622
 * @time 2016年5月28日
 * @email lukw@eastcom-sw.com
 */
@Repository
public class BizInfoConfDaoImpl extends BaseDaoImpl< BizInfoConf> implements BizInfoConfDao {
	
	@Override
	public List<BizInfoConf> findBizInfoConf(BizInfoConf bizInfoConf){
		
		List<Object> list = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" from BizInfoConf b where 1=1 ");
		if(StringUtils.isNotBlank(bizInfoConf.getTaskAssignee())){
			hql.append(" and b.taskAssignee = ? ");
			list.add(bizInfoConf.getTaskAssignee());
		}
		if(StringUtils.isNotBlank(bizInfoConf.getTaskId())){
			hql.append(" and b.taskId = ? ");
			list.add(bizInfoConf.getTaskId());
		}
		if(bizInfoConf.getBizInfo()!=null && StringUtils.isNotBlank(bizInfoConf.getBizInfo().getId())){
			hql.append(" and b.bizInfo.id = ? ");
			list.add(bizInfoConf.getBizInfo().getId());
		}
		return this.find(hql.toString(), list.toArray());
	}
	
	@Override
	public BizInfoConf getMyWork(String bizId){
		
		List<Object> list = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" from BizInfoConf b where b.bizInfo.id = ? ");
		list.add(bizId);
		LoginUser loginUser = WebUtil.getLoginUser();
		if(loginUser != null){
			hql.append(" AND ( b.taskAssignee = ? or b.taskAssignee is null");
			list.add(loginUser.getUsername());
			Set<String> roles = loginUser.getRoles();
			for (String role : roles) {
				hql.append("or b.taskAssignee like ? ");
				list.add("%," + role + ",%");
			}
			hql.append(")");
		}
		List<BizInfoConf> result = this.find(hql.toString(), list.toArray());
		return CollectionUtils.isNotEmpty(result) ? result.get(0) : null;
	}
	
	@Override
	public List<BizInfoConf> getBizInfoConf(String bizId){
		
		String hql = " from BizInfoConf b where b.bizInfo.id = ? ";
		return this.find(hql.toString(), new Object[]{bizId});
	}
	
	@Override
	public void turnTask(Map<String,Object> map){
		
		String updateBizInfo = "UPDATE ACT_BIZ_INFO_CONF CONF SET TASK_ASSIGNEE =? WHERE CONF.BIZ_ID = ? ";
		StringBuffer updateTask = new StringBuffer(" UPDATE ACT_RU_TASK SET ASSIGNEE_ =? WHERE ID_ IN ");
		updateTask.append(" (SELECT TASK_ID AS TASK_ID FROM ACT_BIZ_INFO_CONF WHERE BIZ_ID =? )");
		Object object[] = new Object[]{map.get("username"),map.get("bizId")};
		this.executeBySql(updateBizInfo, object);
		this.executeBySql(updateTask.toString(),object);
	}
}
