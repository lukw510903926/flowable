package com.flowable.dao.flow;

import java.util.List;

import com.flowable.entity.BizFile;
import com.flowable.util.dao.IBaseDao;

public interface IBizFileDao extends IBaseDao<BizFile> {

	List<BizFile> findBizFile(BizFile bizFile);
}
