package com.flowable.service.dict;

import com.flowable.entity.dict.DictValue;
import com.flowable.util.service.IBaseService;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2017-12-26 13:47
 **/
public interface IDictValueService extends IBaseService<DictValue> {

    void saveOrUpdate(DictValue dictValue);

    DictValue getById(String valueId);
}
