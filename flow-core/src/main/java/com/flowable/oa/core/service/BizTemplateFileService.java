package com.flowable.oa.core.service;

import com.flowable.oa.core.entity.BizTemplateFile;
import com.flowable.oa.core.util.mybatis.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020/2/26 9:28 下午
 */
public interface BizTemplateFileService extends IBaseService<BizTemplateFile> {

    /**
     * 保存模版
     *
     * @param templateFile
     * @param file
     */
    void saveOrUpdate(BizTemplateFile templateFile, MultipartFile file);

    /**
     * 获取模版
     *
     * @param params
     * @return
     */
    BizTemplateFile getBizTemplateFile(Map<String, String> params);
}
