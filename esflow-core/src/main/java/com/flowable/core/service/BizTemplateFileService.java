package com.flowable.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.flowable.common.service.IBaseService;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.BizTemplateFile;

public interface BizTemplateFileService extends IBaseService<BizTemplateFile> {

    PageHelper<BizTemplateFile> findTemplateFlies(PageHelper<BizTemplateFile> page, BizTemplateFile file, boolean islike);

    void deleteByIds(List<String> list);

    List<BizTemplateFile> findFileByIds(List<String> ids);

    void saveOrUpdate(BizTemplateFile dataFile, MultipartFile file);

    BizTemplateFile getBizTemplateFile(Map<String, String> params);
}
