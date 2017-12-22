package com.flowable.core.service;

import java.util.List;

import com.flowable.core.bean.BizFile;

/**
 * 工单附件
 */
public interface IBizFileService {

	/**
	 * 添加工单附件
	 * @param beans
	 * @
	 */
	public void addBizFile(BizFile... beans) ;

	/**
	 * 更新工单系信息
	 * @param beans
	 * @
	 */
	public void updateBizFile(BizFile... beans) ;

	/**
	 * 删除工单信息
	 * @param beans
	 * @
	 */
	public void deleteBizFile(BizFile... beans) ;

	/**
	 * 删除工单信息
	 * @param ids
	 * @
	 */
	public void deleteBizFile(String... ids) ;

	/**
	 * 根据工单对象ID获取该工单的附件列表
	 * @param workID
	 * @return
	 * @
	 */
	public List<BizFile> loadBizFilesByBizId(String bizId, String taskId);

	/**
	 * 根据ID获取附件对象
	 * @param id
	 * @return
	 * @
	 */
	public BizFile getBizFileById(String id) ;

	public List<BizFile> findBizFile(BizFile bizFile);

}
