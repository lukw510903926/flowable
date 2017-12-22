package com.flowable.core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.flowable.common.exception.ServiceException;
import com.flowable.common.service.BaseServiceImpl;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.BizTemplateFile;
import com.flowable.core.dao.BizTemplateFileDao;
import com.flowable.core.service.BizTemplateFileService;
import com.flowable.core.service.IBizInfoService;

/**
 * @author 2622
 * @time 2016年8月5日
 * @email lukw@eastcom-sw.com
 */
@Service
@Transactional(readOnly = true)
public class BizTemplateFileServiceImpl extends BaseServiceImpl<BizTemplateFile> implements BizTemplateFileService {

	@Autowired
	private BizTemplateFileDao bizTemplateFileDao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IBizInfoService bizInfoService;

	@Value("${templateFilePath}")
	private String filePath;

	@Override
	public PageHelper<BizTemplateFile> findTemplateFlies(PageHelper<BizTemplateFile> page, BizTemplateFile file,
			boolean islike) {

		return this.bizTemplateFileDao.findTemplateFlies(page, file, islike);
	}

	public BizTemplateFile getBizTemplateFile(Map<String, String> params) {

		BizInfo bizInfo = null;
		logger.info("params :{}",params);
		BizTemplateFile templateFile = new BizTemplateFile();
		templateFile.setFileName(params.get("fileName"));
		String bizType = params.get("bizType");
		if (StringUtils.isNotBlank(bizType)) {
			templateFile.setFlowName("");
		}
		if (StringUtils.isNotBlank(params.get("bizId"))) {
			bizInfo = this.bizInfoService.get(params.get("bizId"));
			templateFile.setFlowName(bizInfo.getBizType());
		}
		PageHelper<BizTemplateFile> page = new PageHelper<BizTemplateFile>();
		page.setRows(-1);
		page.setPage(-1);
		List<BizTemplateFile> list = this.bizTemplateFileDao.findTemplateFlies(page, templateFile, false).getList();
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public List<BizTemplateFile> findFileByIds(List<String> ids) {

		List<BizTemplateFile> list = new ArrayList<BizTemplateFile>();
		for (String id : ids) {
			list.add(this.get(id));
		}
		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void saveOrUpdate(BizTemplateFile dataFile, MultipartFile file) {

		String fileName = file.getOriginalFilename();
		dataFile.setFileName(fileName);
		String dataId = null;
		if (!this.check(dataFile)){
			throw new ServiceException(" 相同名称模版已存在,请将原模板文件删除后再上传,所属流程+文件名唯一");
		}
		if (StringUtils.isBlank(dataFile.getId())) {
			dataFile.setCreateTime(new Timestamp(System.currentTimeMillis()));
			dataId = (String) this.bizTemplateFileDao.save(dataFile);
		}
		this.saveFile(file, filePath, fileName, dataId);
	}

	private boolean check(BizTemplateFile dataFile) {

		BizTemplateFile file = new BizTemplateFile();
		file.setFileName(dataFile.getFileName());
		file.setFlowName(dataFile.getFlowName());
		PageHelper<BizTemplateFile> page = new PageHelper<BizTemplateFile>();
		page.setPage(-1);
		page.setRows(-1);
		List<BizTemplateFile> list = this.findTemplateFlies(page, file, false).getList();
		if (list != null && !list.isEmpty())
			return false;
		return true;

	}

	private void saveFile(MultipartFile file, String filePath, String fileName, String dataId) {

		File path = new File(filePath);
		if (!path.exists()) {
			path.mkdir();
		}
		String suffix = "";
		if (fileName.lastIndexOf(".") != -1) {
			suffix = fileName.substring(fileName.lastIndexOf("."));
		}
		File newFile = new File(path, dataId + suffix);
		FileOutputStream out;
		try {
			out = new FileOutputStream(newFile);
			InputStream in = file.getInputStream();
			byte[] buff = new byte[1024];
			while (in.read(buff) != -1) {
				out.write(buff);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteByIds(List<String> list) {

		BizTemplateFile templateFile = null;
		for (String id : list) {
			if (StringUtils.isNotBlank(id)) {
				templateFile = this.get(id);
				if (templateFile != null) {
					String fileName = templateFile.getFileName();
					this.delete(templateFile);
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
