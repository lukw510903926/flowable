package com.flowable.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.core.bean.BizFile;
import com.flowable.core.dao.IBizFileDao;
import com.flowable.core.service.IBizFileService;

@Service
@Transactional(readOnly = true)
public class BizFileServiceImpl implements IBizFileService {

	@Autowired
	private IBizFileDao dao;

	@Transactional
	public void addBizFile(BizFile... beans) {
		if (beans == null) {
			return;
		}
		for (BizFile bean : beans) {
			dao.save(bean);
		}
	}

	@Transactional
	public void updateBizFile(BizFile... beans) {
		if (beans == null) {
			return;
		}
		for (BizFile bean : beans) {
			if (bean.getId() == null)
				continue;
			dao.update(bean);
		}
	}

	@Transactional
	public void deleteBizFile(BizFile... beans) {
		if (beans == null) {
			return;
		}
		for (BizFile bean : beans) {
			if (bean.getId() == null)
				continue;
			dao.delete(bean);
		}
	}

	@Transactional
	public void deleteBizFile(String... ids) {
		if (ids == null) {
			return;
		}
		for (String id : ids) {
			dao.deleteById(id);
		}
	}

	@Override
	public List<BizFile> loadBizFilesByBizId(String bizId, String taskId) {
		BizFile bizFile = new BizFile();
		bizFile.setBizId(bizId);
		bizFile.setTaskId(taskId);
		return dao.findBizFile(bizFile);
	}

	public BizFile getBizFileById(String id) {
		return dao.getById(id);
	}

	@Override
	public List<BizFile> findBizFile(BizFile bizFile) {

		return this.dao.findBizFile(bizFile);
	}

}
