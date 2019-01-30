package com.flowable.service.dict.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * @create 2017-12-26 13:46
 **/
@Service
public class DictTypeServiceImpl extends BaseServiceImpl<DictType> implements IDictTypeService {

    @Autowired
    private IDictValueService dictValueService;

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(DictType dictType) {

        if (!this.check(dictType)) {
            throw new ServiceException("字典名称不可重复");
        }
        dictType.setModified(new Date());
        dictType.setModifier(WebUtil.getLoginUser().getUsername());
        if (StringUtils.isNotBlank(dictType.getId())) {
            this.update(dictType);
        } else {
            dictType.setCreator(WebUtil.getLoginUser().getUsername());
            dictType.setCreateTime(new Date());
            this.save(dictType);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(DictType dictType) {

        if (dictType != null) {
            this.delete(dictType);
            DictValue dictValue = new DictValue();
            dictValue.setDictTypeId(dictType.getId());
            Map<String, Object> params = ReflectionUtils.beanToMap(dictValue);
            List<DictValue> list = this.dictValueService.findbyParam(params, false);
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(value -> this.dictValueService.delete(value));
            }
        }
    }

    private boolean check(DictType dictType) {

        DictType example = new DictType();
        example.setName(dictType.getName());
        Map<String, Object> params = ReflectionUtils.beanToMap(example);
        List<DictType> list = this.findbyParam(params, false);
        return this.check(dictType.getId(), list);
    }
}
