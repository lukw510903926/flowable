package com.flowable.oa.core.service.impl;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.BizTemplateFile;
import com.flowable.oa.core.service.BizTemplateFileService;
import com.flowable.oa.core.service.IBizInfoService;
import com.flowable.oa.core.util.WebUtil;
import com.flowable.oa.core.util.exception.ServiceException;
import com.flowable.oa.core.util.file.UploadHelper;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/13 11:10
 **/
@Slf4j
@Service
public class BizTemplateFileServiceImpl extends BaseServiceImpl<BizTemplateFile> implements BizTemplateFileService {

    @Autowired
    private IBizInfoService bizInfoService;

    @Autowired
    private Environment environment;

    @Autowired
    private UploadHelper uploadHelper;

    @Override
    public BizTemplateFile getBizTemplateFile(BizTemplateFile templateFile) {

        Long bizId = templateFile.getBizId();
        if (bizId != null) {
            BizInfo bizInfo = this.bizInfoService.selectByKey(bizId);
            templateFile.setFlowName(Optional.ofNullable(bizInfo).map(BizInfo::getBizType).orElse(null));
        }
        templateFile.setId(templateFile.getId());
        return this.selectOne(templateFile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
            log.error("模板保存失败 :", e);
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
