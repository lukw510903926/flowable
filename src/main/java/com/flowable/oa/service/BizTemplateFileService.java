package com.flowable.oa.service;

import java.util.List;
import java.util.Map;

import com.flowable.oa.entity.BizTemplateFile;
import com.flowable.oa.util.mybatis.IBaseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface BizTemplateFileService extends IBaseService<BizTemplateFile> {

	PageInfo<BizTemplateFile> findTemplateFlies(PageInfo<BizTemplateFile> page, BizTemplateFile file, boolean islike);

    void deleteByIds(List<String> list);

    List<BizTemplateFile> findFileByIds(List<String> ids);

    void saveOrUpdate(BizTemplateFile dataFile, MultipartFile file);

    BizTemplateFile getBizTemplateFile(Map<String, String> params);
}
