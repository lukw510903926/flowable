package com.flowable.oa.core.service.dict;


import com.flowable.oa.core.entity.dict.DictValue;
import com.flowable.oa.core.util.mybatis.IBaseService;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2017-12-26 13:47
 **/
public interface IDictValueService extends IBaseService<DictValue> {

    void saveOrUpdate(DictValue dictValue);

    DictValue getById(Integer valueId);
}
