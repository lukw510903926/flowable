package com.flowable.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author 26223
 * @time 2016年10月13日
 * @email lukw@eastcom-sw.com
 */
public interface IBaseService<T> {

	public List<T> findAll();

	public List<T> findbyParam(Map<String,Object> param, boolean isLike);

	public void saveOrUpdate(T t);
	
	public Serializable save(T t);
	
	public void update(T t);
	
	public T get(String id);
	
	public void delete(T t);

	public void delete(List<T> t);
	
	public void deleteById(String id);
	
	public void deleteByIds(List<String> list);
}
