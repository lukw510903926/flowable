package com.flowable.core.dao;

import java.util.List;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.BizLog;

public interface IBizLogDao extends IBaseDao<BizLog> {

    List<BizLog> loadLogByBizId(String bizId);

    /**
     * 获取指定处理结果的 日志
     *
     * @return
     */
    List<BizLog> findBizLogs(BizLog bizLog);
}
