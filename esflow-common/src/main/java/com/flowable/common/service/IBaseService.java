package com.flowable.common.service;

import com.flowable.common.utils.PageHelper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author 26223
 * @time 2016年10月13日
 * @email lukw@eastcom-sw.com
 */
public interface IBaseService<T> {

    List<T> findAll();

    List<T> findbyParam(Map<String, Object> param, boolean isLike);

    PageHelper<T> findByParams(Map<String, Object> params, PageHelper<T> page, boolean like);

    void saveOrUpdate(T t);

    Serializable save(T t);

    void update(T t);

    T get(String id);

    void delete(T t);

    void delete(List<T> t);

    void deleteById(String id);

    void deleteByIds(List<String> list);

    boolean check(Serializable uid, List<T> list);
}
