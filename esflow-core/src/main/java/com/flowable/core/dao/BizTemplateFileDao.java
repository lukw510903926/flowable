package com.flowable.core.dao;

import com.flowable.common.dao.IBaseDao;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.BizTemplateFile;

public interface BizTemplateFileDao extends IBaseDao<BizTemplateFile>{

	public PageHelper<BizTemplateFile> findTemplateFlies(PageHelper<BizTemplateFile> page,BizTemplateFile file,boolean islike);
}
