package com.flowable.oa.core.service;

import java.util.Map;

import com.flowable.oa.core.entity.BizTemplateFile;
import com.flowable.oa.core.util.mybatis.IBaseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface BizTemplateFileService extends IBaseService<BizTemplateFile> {

    void saveOrUpdate(BizTemplateFile dataFile, MultipartFile file);

    BizTemplateFile getBizTemplateFile(Map<String, String> params);
}
