package com.flowable.dao.flow;

import java.util.List;
import java.util.Map;

import com.flowable.entity.BizInfo;
import com.flowable.util.PageHelper;
import com.flowable.util.dao.IBaseDao;

public interface IBizInfoDao extends IBaseDao<BizInfo> {

    PageHelper<BizInfo> queryWorkOrder(Map<String, Object> params, PageHelper<BizInfo> page);

    /**
     * 根据父单查询子单
     *
     * @param parentId
     * @return
     */
    List<BizInfo> getBizByParentId(String parentId);

}
