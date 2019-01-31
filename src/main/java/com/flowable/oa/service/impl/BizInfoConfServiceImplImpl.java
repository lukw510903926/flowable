package com.flowable.oa.service.impl;

import java.util.List;

import com.flowable.oa.dao.BizInfoConfMapper;
import com.flowable.oa.service.BizInfoConfService;
import com.flowable.oa.util.WebUtil;
import com.flowable.oa.util.LoginUser;
import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.oa.entity.BizInfoConf;

/**
 * 
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午3:12:55
 * @author : lukewei
 * @description :
 */
@Service
public class BizInfoConfServiceImplImpl extends BaseServiceImpl<BizInfoConf> implements BizInfoConfService {

	@Autowired
	private BizInfoConfMapper bizInfoConfDao;

	@Override
	@Transactional(readOnly = false)
	public void saveOrUpdate(BizInfoConf bizInfoConf) {

		BizInfoConf example = new BizInfoConf();
		example.setBizId(bizInfoConf.getBizId());
		example.setTaskAssignee(bizInfoConf.getTaskAssignee());
		example.setTaskId(bizInfoConf.getTaskId());
		this.deleteByModel(example);
		if (StringUtils.isNotBlank(bizInfoConf.getId())) {
			this.updateAll(bizInfoConf);
		} else {
			this.save(bizInfoConf);
		}
	}

	@Override
	public List<BizInfoConf> findBizInfoConf(BizInfoConf bizInfoConf) {

		return this.findByModel(bizInfoConf, false);
	}
	
	@Override
	public List<BizInfoConf> findByBizId(String bizId){
		
		BizInfoConf example = new BizInfoConf();
		example.setBizId(bizId);
		return this.findBizInfoConf(example);
	}
	
	@Override
	public void deleteByBizId(String bizId) {
		
		List<BizInfoConf> list = this.findByBizId(bizId);
		if(CollectionUtils.isNotEmpty(list)) {
			list.forEach(example ->this.deleteById(example.getId()));
		}
	}

	@Override
	@Transactional(readOnly = true)
	public BizInfoConf getMyWork(String bizId) {

		if (StringUtils.isNotBlank(bizId)) {
			BizInfoConf example = new BizInfoConf();
			LoginUser loginUser = WebUtil.getLoginUser();
			example.setLoginUser(loginUser.getUsername());
			example.setBizId(bizId);
			example.setRoles(loginUser.getRoles());
			List<BizInfoConf> list = this.bizInfoConfDao.getMyWork(example);
			return CollectionUtils.isEmpty(list) ? null : list.get(0);
		}
		return null;
	}
}
