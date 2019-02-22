package com.flowable.oa.core.dao;

import com.flowable.oa.core.entity.BizInfoConf;
import com.flowable.oa.core.util.mybatis.MyMapper;

import java.util.List;


/**
 * 
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午4:34:11
 * @author : lukewei
 * @description :
 */
public interface BizInfoConfMapper extends MyMapper<BizInfoConf> {

	/**
	 * 当前工单我的待办
	 *
	 * @param bizInfoConf
	 * @return
	 */
	List<BizInfoConf> getMyWork(BizInfoConf bizInfoConf);
}
