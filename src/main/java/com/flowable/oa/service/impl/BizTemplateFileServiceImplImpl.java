package com.flowable.oa.service.impl;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.flowable.oa.util.WebUtil;
import com.github.pagehelper.PageInfo;
import com.flowable.oa.util.exception.ServiceException;
import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.entity.BizTemplateFile;
import com.flowable.oa.service.BizTemplateFileService;
import com.flowable.oa.service.IBizInfoService;

/**
 * @author 2622
 * @time 2016年8月5日
 * @email lukw@eastcom-sw.com
 */
@Service
public class BizTemplateFileServiceImplImpl extends BaseServiceImpl<BizTemplateFile> implements BizTemplateFileService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBizInfoService bizInfoService;

    @Value("${templateFilePath}")
    private String filePath = "/home/file";

    @Override
    public PageInfo<BizTemplateFile> findTemplateFlies(PageInfo<BizTemplateFile> page, BizTemplateFile file, boolean isLike) {

        if (page != null) {
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
        }
        return new PageInfo<>(this.findByModel(file, isLike));
    }

    @Override
    public BizTemplateFile getBizTemplateFile(Map<String, String> params) {

        logger.info("params :{}", params);
        BizTemplateFile templateFile = new BizTemplateFile();
        templateFile.setFileName(params.get("fileName"));
        if (StringUtils.isNotBlank(params.get("bizId"))) {
            BizInfo bizInfo = this.bizInfoService.selectByKey(params.get("bizId"));
            templateFile.setFlowName(Optional.ofNullable(bizInfo).map(BizInfo::getBizType).orElse(null));
        }
        if (StringUtils.isNotEmpty(params.get("id"))) {
            templateFile.setId(params.get("id"));
        }
        List<BizTemplateFile> list = this.findTemplateFlies(null, templateFile, false).getList();
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public List<BizTemplateFile> findFileByIds(List<String> ids) {

        List<BizTemplateFile> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            ids.forEach(id -> {
                if (StringUtils.isNotBlank(id)) {
                    BizTemplateFile file = this.selectByKey(id);
                    if (file != null) {
                        list.add(file);
                    }
                }
            });
        }
        return list;
    }

    @Override
    @Transactional
    public void saveOrUpdate(BizTemplateFile dataFile, MultipartFile file) {

        String fileName = file.getOriginalFilename();
        dataFile.setFileName(fileName);
        if (!this.check(dataFile)) {
            throw new ServiceException(" 相同名称模版已存在,请将原模板文件删除后再上传,所属流程+文件名唯一");
        }
        if (StringUtils.isBlank(dataFile.getId())) {
            dataFile.setCreateUser(WebUtil.getLoginUserId());
            dataFile.setFullName(WebUtil.getLoginUser().getName());
            dataFile.setCreateTime(new Timestamp(System.currentTimeMillis()));
            this.save(dataFile);
        }
        this.saveFile(file, filePath, fileName, dataFile.getId());
    }

    private boolean check(BizTemplateFile dataFile) {

        BizTemplateFile file = new BizTemplateFile();
        file.setFileName(dataFile.getFileName());
        file.setFlowName(dataFile.getFlowName());
        List<BizTemplateFile> list = this.findTemplateFlies(null, file, false).getList();
        return this.check(dataFile.getId(), list);

    }

    private void saveFile(MultipartFile file, String filePath, String fileName, String dataId) {

        String suffix = "";
        if (fileName.lastIndexOf(".") != -1) {
            suffix = fileName.substring(fileName.lastIndexOf("."));
        }
        try {
            File newFile = new File(filePath, dataId + suffix);
            FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
        } catch (Exception e) {
            logger.error("模板保存失败 :{}", e);
            throw new ServiceException("模板保存失败!");
        }
    }

    @Override
    @Transactional
    public void deleteByIds(List<String> list) {

        BizTemplateFile templateFile;
        for (String id : list) {
            if (StringUtils.isNotBlank(id)) {
                templateFile = this.selectByKey(id);
                if (templateFile != null) {
                    String fileName = templateFile.getFileName();
                    this.deleteById(templateFile.getId());
                    String suffix = "";
                    if (fileName.lastIndexOf(".") != -1) {
                        suffix = fileName.substring(fileName.lastIndexOf("."));
                    }
                    File file = new File(filePath + File.separator + id + suffix);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
    }

}
