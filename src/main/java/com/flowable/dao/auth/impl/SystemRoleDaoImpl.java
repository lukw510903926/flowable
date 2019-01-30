package com.flowable.dao.auth.impl;

import org.springframework.stereotype.Repository;

import com.flowable.dao.auth.ISystemRoleDao;
import com.flowable.entity.auth.SystemRole;
import com.flowable.util.dao.BaseDaoImpl;

@Repository
public class SystemRoleDaoImpl extends BaseDaoImpl<SystemRole> implements ISystemRoleDao {

}
