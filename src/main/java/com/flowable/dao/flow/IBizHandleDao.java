package com.flowable.dao.flow;

import java.util.List;
import java.util.Map;

import com.flowable.util.PageHelper;
import com.flowable.util.dao.IBaseDao;

public interface IBizHandleDao extends IBaseDao<Map<String, Object>> {

    /**
     * 获取工单发起人列表
     *
     * @param page
     * @param params
     * @return
     */
    PageHelper<Map<String, Object>> findMember(PageHelper<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 获取部门选项列表
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> findSector(Map<String, Object> params);

}
