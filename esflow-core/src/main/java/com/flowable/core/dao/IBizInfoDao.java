package com.flowable.core.dao;

import java.util.List;
import java.util.Map;

import com.flowable.common.dao.IBaseDao;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.BizInfo;

public interface IBizInfoDao extends IBaseDao<BizInfo> {

	 PageHelper<BizInfo> queryWorkOrder(Map<String, Object> params, PageHelper<BizInfo> page);


	/**
	 * 批量查询工单
	 * @param list
	 * @return
	 */
	 List<BizInfo> getBizInfos(List<String> list);

	/**
	 * 根据父单查询子单
	 * @param parentId
	 * @return
	 */
	 List<BizInfo> getBizByParentId(String parentId);

	 BizInfo getBizInfo(String id,String loginUser);

}
