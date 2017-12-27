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
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.auth.SystemRole;
import com.flowable.core.bean.auth.SystemUser;
import com.flowable.core.dao.IBizInfoDao;
import com.flowable.core.dao.auth.ISystemUserDao;
import com.flowable.core.util.Constants;
import com.flowable.core.util.WebUtil;

@Repository
public class BizInfoDaoImpl extends BaseDaoImpl<BizInfo> implements IBizInfoDao {

    @Autowired
    private ISystemUserDao systemUserDao;

    private Logger logger = LoggerFactory.getLogger(BizInfoDaoImpl.class);

    @Override
    public List<BizInfo> getBizByParentId(String parentId) {

        if (StringUtils.isBlank(parentId)) {
            return null;
        }
        String hql = " from BizInfo a where a.parentId = ? ";
        return this.find(hql, new Object[]{parentId});
    }

    @Override
    public PageHelper<BizInfo> queryWorkOrder(Map<String, Object> params, PageHelper<BizInfo> page) {

        String loginUser = WebUtil.getLoginUser().getUsername();
        StringBuffer hql = new StringBuffer(" from BizInfo bean where bean.status <> ? ");
        List<Object> list = new ArrayList<Object>();
        list.add(Constants.BIZ_DELETE);
        String bizId = (String) params.get("bizId");
        if (StringUtils.isNotEmpty(bizId)) {
            hql.append(" AND bean.workNum like ?");
            list.add("%" + bizId + "%");
        }
        String title = (String) params.get("title");
        if (StringUtils.isNotEmpty(title)) {
            hql.append(" and bean.title like ?");
            list.add("%" + title + "%");
        }
        String createUser = (String) params.get("createUser");
        if (StringUtils.isNotEmpty(createUser)) {
            List<SystemUser> systemUsers = this.systemUserDao.findSystemUser(new SystemUser(null, createUser));
            if (CollectionUtils.isNotEmpty(list)) {
                hql.append(" AND bean.createUser in (");
                for (SystemUser systemUser : systemUsers) {
                    String username = systemUser.getUsername();
                    if (StringUtils.isNotBlank(username)) {
                        hql.append("?,");
                        list.add(username);
                    }
                }
                hql = hql.deleteCharAt(hql.length() - 1).append(")");
            } else {
                hql.append(" AND bean.createUser like ? ");
                list.add("%" + createUser + "%");
            }
        }
        String parentId = (String) params.get("parentId");
        if (StringUtils.isNotEmpty(parentId)) {
            hql.append(" AND bean.parentId = ?");
            list.add(parentId);
        }
        String parentTaskName = (String) params.get("parentTaskName");
        if (StringUtils.isNotEmpty(parentTaskName)) {
            hql.append(" AND bean.parentTaskName = ?");
            list.add(parentTaskName);
        }
        String bizType = (String) params.get("bizType");
        if (StringUtils.isNotEmpty(bizType)) {
            hql.append(" AND bean.bizType = ?");
            list.add(bizType);
        }
        String status = (String) params.get("status");
        if (StringUtils.isNotEmpty(status)) {
            hql.append(" AND bean.status = ? ");
            list.add(status);
        }
        String action = (String) params.get("action");
        if ("myHandle".equalsIgnoreCase(action)) {
            hql.append(" AND EXISTS (FROM BizLog log WHERE log.handleUser =? AND bean.id =log.bizInfo.id)");
            list.add(loginUser);
        }
        if ("myWork".equalsIgnoreCase(action)) {
            hql.append(" AND bean.status <> ? ");
            list.add(Constants.BIZ_TEMP);
            hql.append(" and EXISTS (FROM BizInfoConf c WHERE c.taskAssignee = ? ");
            list.add(loginUser);
            SystemUser user = this.systemUserDao.getUserByUsername(loginUser);
            if (user != null) {
                Set<SystemRole> roles = user.getRoles();
                for (SystemRole role : roles) {
                    hql.append("OR c.taskAssignee like ? ");
                    list.add("%," + role.getNameCn() + ",%");
                }
            }
            hql.append(" or c.taskAssignee is null AND bean.id = c.bizInfo.id )");
        }
        String taskAssignee = (String) params.get("taskAssignee");
        if (StringUtils.isNotEmpty(taskAssignee)) {
            hql.append(" AND EXISTS (FROM BizInfoConf c WHERE c.taskAssignee LIKE ? ");
            list.add("%" + taskAssignee + "%");
            List<SystemUser> systemUsers = this.systemUserDao.findSystemUser(new SystemUser(null, taskAssignee));
            if (CollectionUtils.isNotEmpty(list)) {
                hql.append(" OR C.taskAssignee in (");
                for (SystemUser systemUser : systemUsers) {
                    String username = systemUser.getUsername();
                    if (StringUtils.isNotBlank(username)) {
                        hql.append("?,");
                        list.add(username);
                    }
                }
                hql = hql.deleteCharAt(hql.length() - 1).append(")");
            }
            hql.append(" AND bean.id = c.bizInfo.id )");
        }
        if ("myTemp".equalsIgnoreCase(action) || "myCreate".equalsIgnoreCase(action)) {
            hql.append(" and bean.createUser = ? ");
            list.add(loginUser);
        }
        String taskDefKey = (String) params.get("taskDefKey");
        if (StringUtils.isNotEmpty(taskDefKey)) {
            hql.append(" AND bean.taskDefKey = ?");
            list.add(taskDefKey);
        }
        if (params.get("createTime") != null && params.get("createTime2") != null) {
            hql.append(" AND bean.createTime BETWEEN ? AND ? ");
            list.add(params.get("createTime"));
            list.add(params.get("createTime2"));
        }
        hql.append(" ORDER BY bean.createTime DESC ");
        logger.info("args : {}", list);
        return this.find(page, hql.toString(), list.toArray());
    }
}
