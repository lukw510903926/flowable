package com.flowable.oa.service.dict.Impl;

import java.util.Date;
import java.util.List;

import com.flowable.oa.service.dict.IDictValueService;
import com.flowable.oa.util.WebUtil;
import com.flowable.oa.util.mybatis.BaseServiceImpl;
import com.flowable.oa.util.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flowable.oa.entity.dict.DictType;
import com.flowable.oa.entity.dict.DictValue;
import com.flowable.oa.service.dict.IDictTypeService;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2017-12-26 13:46
 **/
@Service
public class DictTypeServiceImplImpl extends BaseServiceImpl<DictType> implements IDictTypeService {

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
            this.updateNotNull(dictType);
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
            this.deleteById(dictType.getId());
            DictValue dictValue = new DictValue();
            dictValue.setDictTypeId(dictType.getId());
            this.dictValueService.deleteByModel(dictValue);
        }
    }

    private boolean check(DictType dictType) {

        DictType example = new DictType();
        example.setName(dictType.getName());
        List<DictType> list = this.findByModel(example, false);
        return this.check(dictType.getId(), list);
    }
}
