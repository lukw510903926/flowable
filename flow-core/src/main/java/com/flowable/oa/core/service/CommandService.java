package com.flowable.oa.core.service;

import com.flowable.oa.core.entity.BizInfo;
import java.util.Map;


/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020/2/26 9:28 下午
 */
public interface CommandService {

    /**
     * 任意节点跳转
     *
     * @param params base.bizId 工单ID
     *               base.taskDefKey 目标节点定义id
     */
    BizInfo jumpCommand(Map<String, Object> params);
}
