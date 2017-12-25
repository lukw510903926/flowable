package com.flowable.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.flowable.common.dao.BaseDaoImpl;
import com.flowable.core.bean.BizFile;
import com.flowable.core.dao.IBizFileDao;

@Repository
public class BizFileDaoImpl extends BaseDaoImpl<BizFile> implements IBizFileDao {

	@Override
	public List<BizFile> findBizFile(BizFile bizFile){
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(" from BizFile file join fetch file.bizInfo biz where 1=1 ");
		if(StringUtils.isNotBlank(bizFile.getTaskId())){
			hql.append(" and file.taskId = ? ");
			list.add(bizFile.getTaskId());
		}
		if(StringUtils.isNotBlank(bizFile.getBizId())){
			hql.append(" and biz.id = ? ");
			list.add(bizFile.getBizId());
		}
		if(StringUtils.isNotBlank(bizFile.getFileCatalog())){
			hql.append(" and file.fileCatalog = ? ");
			list.add(bizFile.getFileCatalog());
		}
		return this.find(hql.toString(),list.toArray());
	}

}
