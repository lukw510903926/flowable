package com.flowable.oa.service.impl;

import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flowable.oa.entity.ActBizInfoDelayTime;
import com.flowable.oa.service.ActBizInfoDelayTimeService;

@Service
public class ActBizInfoDelayTimeServiceImplImpl extends BaseServiceImpl<ActBizInfoDelayTime> implements ActBizInfoDelayTimeService {

    @Override
    @Transactional
    public void saveOrUpdate(ActBizInfoDelayTime actBizInfo) {

        if (StringUtils.isNotBlank(actBizInfo.getId())) {
            this.updateNotNull(actBizInfo);
        } else {
            this.save(actBizInfo);
        }
    }
}
