package com.flowable.oa.core.service.impl;

import com.flowable.oa.core.entity.ActBizInfoDelayTime;
import com.flowable.oa.core.service.ActBizInfoDelayTimeService;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActBizInfoDelayTimeServiceImplImpl extends BaseServiceImpl<ActBizInfoDelayTime> implements ActBizInfoDelayTimeService {

    @Override
    @Transactional
    public void saveOrUpdate(ActBizInfoDelayTime actBizInfo) {

        if (null != actBizInfo.getId()) {
            this.updateNotNull(actBizInfo);
        } else {
            this.save(actBizInfo);
        }
    }
}
