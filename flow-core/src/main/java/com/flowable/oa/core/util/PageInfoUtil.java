package com.flowable.oa.core.util;

import com.flowable.oa.core.vo.BaseVo;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/2/22 22:55
 **/
public class PageInfoUtil{

    public static PageInfo<BaseVo> getPage(BaseVo baseVo) {

        PageInfo<BaseVo> pageInfo = new PageInfo<>();
        pageInfo.setPageNum(baseVo.getPageNum());
        pageInfo.setPageSize(baseVo.getPageSize());
        return pageInfo;

    }
}
