package com.flowable.oa.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.BizTemplateFile;
import com.flowable.oa.core.service.BizTemplateFileService;
import com.flowable.oa.core.service.IBizInfoService;
import com.flowable.oa.core.util.WebUtil;
import com.flowable.oa.core.util.exception.ServiceException;
import com.flowable.oa.core.util.file.UploadHelper;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/13 11:10
 **/
@Service
public class BizTemplateFileServiceImplImpl extends BaseServiceImpl<BizTemplateFile> implements BizTemplateFileService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBizInfoService bizInfoService;

    @Autowired
    private Environment environment;

    @Autowired
    private UploadHelper uploadHelper;

    @Override
    public BizTemplateFile getBizTemplateFile(Map<String, String> params) {

        logger.info("params :{}", params);
        BizTemplateFile templateFile = new BizTemplateFile();
        templateFile.setFileName(params.get("fileName"));
        if (StringUtils.isNotBlank(params.get("bizId"))) {
            BizInfo bizInfo = this.bizInfoService.selectByKey(MapUtils.getInteger(params, "bizId"));
            templateFile.setFlowName(Optional.ofNullable(bizInfo).map(BizInfo::getBizType).orElse(null));
        }
        if (StringUtils.isNotEmpty(params.get("id"))) {
            templateFile.setId(MapUtils.getInteger(params, "id"));
        }
        return this.selectOne(templateFile);
    }

    @Override
    @Transactional
    public void saveOrUpdate(BizTemplateFile dataFile, MultipartFile file) {

        String fileName = file.getOriginalFilename();
        dataFile.setFileName(fileName);
        if (!this.check(dataFile)) {
            throw new ServiceException(" 相同名称模版已存在,请将原模板文件删除后再上传,所属流程+文件名唯一");
        }
        try (InputStream inputStream = file.getInputStream()) {
            String upload = this.uploadHelper.upload(inputStream, environment.getProperty("biz.file.path"), fileName);
            dataFile.setFilePath(upload);
            if (dataFile.getId() == null) {
                dataFile.setCreateUser(WebUtil.getLoginUserId());
                dataFile.setFullName(WebUtil.getLoginUser().getName());
                dataFile.setCreateTime(new Date());
                this.save(dataFile);
            } else {
                this.updateNotNull(dataFile);
            }
        } catch (IOException e) {
            logger.error("模板保存失败 :{}", e);
            throw new ServiceException("模板保存失败!");
        }
    }

    private boolean check(BizTemplateFile dataFile) {

        BizTemplateFile file = new BizTemplateFile();
        file.setFileName(dataFile.getFileName());
        file.setFlowName(dataFile.getFlowName());
        List<BizTemplateFile> list = this.select(file);
        return this.check(dataFile.getId(), list);
    }
}
