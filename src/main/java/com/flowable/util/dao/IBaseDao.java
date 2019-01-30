package com.flowable.util.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import com.flowable.util.PageHelper;

/**
 * @author 26223
 * @time 2016年10月13日
 * @email lukw@eastcom-sw.com
 */
public interface IBaseDao<T> {

	Session getCurrentSession();

	List<T> findAll();

	Query createQuery(String hql);

	<E> List<E> find(String hql);

	Serializable save(T t);

	void update(T t);

	void saveOrUpdate(T t);

	void saveOrUpdate(List<T> list);

	void delete(T t);

	void delete(List<T> list);

	void deleteById(String id);

	void deleteByIds(List<String> list);

	Criteria getCriteria();

	T getById(String id);

	List<T> find(Criteria criteria);

	T getUniqueResult(String propertyName, Object value);

	/**
	 * 
	 * @param hql
	 * @param parameter three type List Map Object[]
	 * @return
	 */
	<E> List<E> find(String hql, Object parameter);

	/**
	 * 基本属性查询封装(不可用于引用属性的查询)
	 * 
	 * @param params
	 * @param like
	 * @return
	 */
	List<T> findByParams(Map<String, Object> params, boolean like);

	PageHelper<T> findByParams(Map<String, Object> params, PageHelper<T> page, boolean like);

	Query createSQLQuery(String sql);
	
	Query createSQLQuery(String sql,Class<?> resultClass);

	<E> List<E> findBySql(String sql, Class<?> clazz);

	<E> List<E> findBySql(String sql, Object parameter, Class<?> clazz);

	<E> List<E> findBySql(String sql, Object parameter);

	/**
	 * 唯一性校验
	 * 
	 * @param list
	 * @param o
	 * @return
	 */
	boolean check(List<T> list, T o);

	<E> PageHelper<E> findBySql(PageHelper<E> page, String sql, Object parameter, Class<?> resultClass);

	<E> PageHelper<E> find(PageHelper<E> page, String hql, Object parameter);

	<E> PageHelper<E> find(PageHelper<E> page, String hql);

	<E> PageHelper<E> findBySql(PageHelper<E> page, String sql, Class<?> resultClass);

	<E> PageHelper<E> findBySql(PageHelper<E> page, String sql);

	int executeBySql(String sqlString);

	int executeBySql(String sqlString, Map<String, Object> parameter);

	int executeBySql(String sqlString, Object[] parameter);

	int execute(String hql);

	int execute(String hql, Object[] parameter);

	Long count(String hqlString);

	Long count(String hqlString, Map<String, Object> parameter);

}
