package com.flowable.oa.core.service;

import com.flowable.oa.core.entity.BizTemplateFile;
import com.flowable.oa.core.util.mybatis.IBaseService;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020/2/26 9:28 下午
 */
public interface BizTemplateFileService extends IBaseService<BizTemplateFile> {

    void saveOrUpdate(BizTemplateFile dataFile, MultipartFile file);

    BizTemplateFile getBizTemplateFile(Map<String, String> params);
}
