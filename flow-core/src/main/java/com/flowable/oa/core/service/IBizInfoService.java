package com.flowable.oa.core.service;

import java.util.List;
import java.util.Map;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.util.mybatis.IBaseService;
import com.flowable.oa.core.vo.BaseVo;
import com.flowable.oa.core.vo.BizInfoVo;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/13 18:17
 **/
public interface IBizInfoService extends IBaseService<BizInfo> {

    /**
     * 复制工单
     *
     * @param bizId
     * @param processInstanceId
     * @param variables
     * @return
     */
    BizInfo copyBizInfo(Integer bizId, String processInstanceId, Map<String, Object> variables);

    /**
     * 分页查询指定用户创建的工单
     *
     * @return
     */
    PageInfo<BizInfo> findBizInfo(BizInfoVo bizInfoVo, PageInfo<BaseVo> page);

    List<BizInfo> getBizByParentId(Integer parentId);

}
