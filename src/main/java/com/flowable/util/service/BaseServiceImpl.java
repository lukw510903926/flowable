package com.flowable.util.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.flowable.util.PageHelper;
import com.flowable.util.ReflectionUtils;
import com.flowable.util.dao.IBaseDao;

/**
 * @author 26223
 * @time 2016年10月13日
 * @email lukw@eastcom-sw.com
 */
public class BaseServiceImpl<T> implements IBaseService<T> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IBaseDao<T> baseDao;

	@Override
	public List<T> findAll() {
		logger.info(" baseDao : " + baseDao.getClass());
		return baseDao.findAll();
	}

	@Override
	public PageHelper<T> findByParams(Map<String, Object> params, PageHelper<T> page, boolean like) {

		return this.baseDao.findByParams(params, page, like);
	}

	@Override
	public List<T> findbyParam(Map<String, Object> param, boolean isLike) {
		return this.baseDao.findByParams(param, isLike);
	}

	@Override
	public void saveOrUpdate(T t) {

		this.baseDao.saveOrUpdate(t);
	}

	@Override
	public Serializable save(T t) {

		return this.baseDao.save(t);
	}

	@Override
	public void update(T t) {

		this.baseDao.update(t);
		;
	}

	@Override
	public T get(String id) {

		return this.baseDao.getById(id);
	}

	@Override
	public void delete(T t) {

		this.baseDao.delete(t);
	}

	@Override
	public void delete(List<T> t) {

		this.baseDao.delete(t);
	}

	@Override
	public void deleteById(String id) {

		this.baseDao.deleteById(id);
	}

	@Override
	public void deleteByIds(List<String> list) {

		this.baseDao.deleteByIds(list);
	}

	@Override
	public boolean check(Serializable uid, List<T> list) {

		if (CollectionUtils.isEmpty(list)) {
			return true;
		}
		int size = list.size();
		if (size > 1) {
			return false;
		}
		T t = list.get(0);
		String oid = ReflectionUtils.getter(t, "id") + "";
		return oid.equals(uid);
	}
}
