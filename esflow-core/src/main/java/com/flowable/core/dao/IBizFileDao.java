package com.flowable.core.dao;

import java.util.List;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.BizFile;

public interface IBizFileDao extends IBaseDao<BizFile> {

	public List<BizFile> findBizFile(BizFile bizFile);
}
