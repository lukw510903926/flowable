package com.flowable.dao.flow;

import java.util.List;

import com.flowable.entity.BizLog;
import com.flowable.util.dao.IBaseDao;

public interface IBizLogDao extends IBaseDao<BizLog> {

    List<BizLog> loadLogByBizId(String bizId);

    /**
     * 获取指定处理结果的 日志
     *
     * @return
     */
    List<BizLog> findBizLogs(BizLog bizLog);
}
