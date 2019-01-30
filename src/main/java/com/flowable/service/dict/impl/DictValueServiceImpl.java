package com.flowable.service.dict.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flowable.entity.dict.DictType;
import com.flowable.entity.dict.DictValue;
import com.flowable.service.dict.IDictTypeService;
import com.flowable.service.dict.IDictValueService;
import com.flowable.util.ReflectionUtils;
import com.flowable.util.WebUtil;
import com.flowable.util.exception.ServiceException;
import com.flowable.util.service.BaseServiceImpl;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2017-12-26 13:47
 **/
@Service
public class DictValueServiceImpl extends BaseServiceImpl<DictValue> implements IDictValueService {

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
            this.update(dictValue);
        } else {
            dictValue.setCreator(WebUtil.getLoginUser().getUsername());
            dictValue.setCreateTime(new Date());
            this.save(dictValue);
        }
    }

    @Override
    public DictValue getById(String valueId) {

        Optional<DictValue> dictValue = Optional.ofNullable(this.get(valueId));
        dictValue.map(value -> {
            if (StringUtils.isNotBlank(value.getDictTypeId())) {
                DictType dictType = this.dictTypeService.get(value.getDictTypeId());
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
        Map<String, Object> params = ReflectionUtils.beanToMap(example);
        List<DictValue> list = this.findbyParam(params, false);
        if (this.check(dictValue.getId(), list)) {
            example.setName(null);
            example.setCode(dictValue.getCode());
            params = ReflectionUtils.beanToMap(example);
            list = this.findbyParam(params, false);
            return this.check(dictValue.getId(), list);
        }
        return true;
    }
}
