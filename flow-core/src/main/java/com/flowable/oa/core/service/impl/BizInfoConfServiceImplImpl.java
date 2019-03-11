package com.flowable.oa.core.service.impl;

import java.util.List;
import com.flowable.oa.core.entity.BizInfoConf;
import com.flowable.oa.core.service.BizInfoConfService;
import com.flowable.oa.core.util.Constants;
import com.flowable.oa.core.util.LoginUser;
import com.flowable.oa.core.util.WebUtil;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


/**
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午3:12:55
 * @description :
 */
@Service
public class BizInfoConfServiceImplImpl extends BaseServiceImpl<BizInfoConf> implements BizInfoConfService {

    @Override
    @Transactional
    public void saveOrUpdate(BizInfoConf bizInfoConf) {

        BizInfoConf example = new BizInfoConf();
        example.setBizId(bizInfoConf.getBizId());
        example.setTaskAssignee(bizInfoConf.getTaskAssignee());
        example.setTaskId(bizInfoConf.getTaskId());
        this.deleteByModel(example);
        if (StringUtils.isNotBlank(bizInfoConf.getId())) {
            this.updateAll(bizInfoConf);
        } else {
            this.save(bizInfoConf);
        }
    }

    @Override
    public List<BizInfoConf> findByBizId(String bizId) {

        BizInfoConf example = new BizInfoConf();
        example.setBizId(bizId);
        return this.select(example);
    }

    @Override
    public void deleteByBizId(String bizId) {

        BizInfoConf bizInfoConf = new BizInfoConf();
        bizInfoConf.setBizId(bizId);
        this.deleteByModel(bizInfoConf);
    }

    @Override
    public BizInfoConf getMyWork(String bizId) {

        if (StringUtils.isNotBlank(bizId)) {
            LoginUser loginUser = WebUtil.getLoginUser();
            Example example = new Example(BizInfoConf.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("bizId", bizId);
            criteria.andEqualTo("taskAssignee", WebUtil.getLoginUsername());
            criteria.orIsNull("taskAssignee");
            if (CollectionUtils.isNotEmpty(loginUser.getRoles())) {
                loginUser.getRoles().forEach(role -> criteria.orLike("taskAssignee", Constants.BIZ_GROUP + role));
            }
            List<BizInfoConf> list = this.selectByExample(example);
            return CollectionUtils.isEmpty(list) ? null : list.get(0);
        }
        return null;
    }
}
