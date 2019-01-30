package com.flowable.dao.flow;

import com.flowable.entity.BizTemplateFile;
import com.flowable.util.PageHelper;
import com.flowable.util.dao.IBaseDao;

public interface BizTemplateFileDao extends IBaseDao<BizTemplateFile>{

	PageHelper<BizTemplateFile> findTemplateFlies(PageHelper<BizTemplateFile> page,BizTemplateFile file,boolean islike);
}
