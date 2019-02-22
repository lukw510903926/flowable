package com.flowable.oa.core.service.dict.Impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.flowable.oa.core.entity.dict.DictType;
import com.flowable.oa.core.entity.dict.DictValue;
import com.flowable.oa.core.service.dict.IDictTypeService;
import com.flowable.oa.core.service.dict.IDictValueService;
import com.flowable.oa.core.util.WebUtil;
import com.flowable.oa.core.util.exception.ServiceException;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2017-12-26 13:47
 **/
@Service
public class DictValueServiceImplImpl extends BaseServiceImpl<DictValue> implements IDictValueService {

    @Autowired
    private IDictTypeService dictTypeService;

    @Override
    public void saveOrUpdate(DictValue dictValue) {

        if (!this.check(dictValue)) {
            throw new ServiceException("名称/编码不可重复");
        }
        dictValue.setModified(new Date());
        dictValue.setModifier(WebUtil.getLoginUser().getUsername());
        if (StringUtils.isNotBlank(dictValue.getId())) {
            this.updateNotNull(dictValue);
        } else {
            dictValue.setCreator(WebUtil.getLoginUser().getUsername());
            dictValue.setCreateTime(new Date());
            this.save(dictValue);
        }
    }

    @Override
    public DictValue getById(String valueId) {

        Optional<DictValue> dictValue = Optional.ofNullable(this.selectByKey(valueId));
        dictValue.map(value -> {
            if (StringUtils.isNotBlank(value.getDictTypeId())) {
                DictType dictType = this.dictTypeService.selectByKey(value.getDictTypeId());
                value.setDictType(dictType);
            }
            return value;
        });
        return dictValue.orElse(null);
    }

    private boolean check(DictValue dictValue) {

        DictValue example = new DictValue();
        example.setDictTypeId(dictValue.getDictTypeId());
        example.setName(dictValue.getName());
        List<DictValue> list = this.findByModel(example, false);
        if (this.check(dictValue.getId(), list)) {
            example.setName(null);
            example.setCode(dictValue.getCode());
            list = this.findByModel(example, false);
            return this.check(dictValue.getId(), list);
        }
        return true;
    }
}
