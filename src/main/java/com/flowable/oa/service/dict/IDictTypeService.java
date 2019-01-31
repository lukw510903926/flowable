package com.flowable.oa.service.dict;

import com.flowable.oa.util.mybatis.IBaseService;
import com.flowable.oa.entity.dict.DictType;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2017-12-26 13:46
 **/
public interface IDictTypeService extends IBaseService<DictType> {

    void saveOrUpdate(DictType dictType);

    void delete(DictType dictType);
}
