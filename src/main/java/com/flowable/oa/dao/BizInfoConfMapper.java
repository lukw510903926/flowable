package com.flowable.oa.dao;

import java.util.List;

import com.flowable.oa.util.mybatis.MyMapper;
import com.flowable.oa.entity.BizInfoConf;

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
