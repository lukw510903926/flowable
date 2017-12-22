package com.flowable.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.flowable.common.utils.PageHelper;


/**
 * @author 26223
 * @time 2016年10月13日
 * @email lukw@eastcom-sw.com
 */
public interface IBaseDao<T> {

	public Session getCurrentSession();
	
	public SessionFactory getSessionFactory();
	
	public List<T> findAll();

	public Query createQuery(String hql);

	public <E> List<E> find(String hql);

	public Serializable save(T t);

	public void update(T t);

	public void saveOrUpdate(T t);

	public void saveOrUpdate(List<T> list);

	public void delete(T t);

	public void delete(List<T> list);
	
	public void deleteById(String id);

	public void deleteByIds(List<String> list);

	public Criteria getCriteria();

	public T getById(String id);
	
	public List<T> find(Criteria criteria);

	public T getUniqueResult(String propertyName,Object value);

	/**
	 * 
	 * @param hql
	 * @param parameter	three type List Map Object[]
	 * @return
	 */
	public <E> List<E> find(String hql, Object parameter);
	
	/**
	 * 基本属性查询封装(不可用于引用属性的查询)
	 * @param params
	 * @param like
	 * @return
	 */
	public List<T> findByParams(Map<String, Object> params, boolean like);
	
	public SQLQuery createSQLQuery(String sql);

	public <E> List<E> findBySql(String sql, Class<?> clazz);

	public <E> List<E> findBySql(String sql, Object parameter, Class<?> clazz);
	
	public <E> List<E> findBySql(String sql, Object parameter);
	
	/**
	 * 唯一性校验
	 * @param list
	 * @param o
	 * @return
	 */
	public boolean check(List<T> list, T o);

	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sql, Object parameter, Class<?> resultClass);

	public <E> PageHelper<E> find(PageHelper<E> page, String hql, Object parameter);

	public <E> PageHelper<E> find(PageHelper<E> page, String hql);

	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sql, Class<?> resultClass);

	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sql);

	int executeBySql(String sqlString);

	int executeBySql(String sqlString, Map<String, Object> parameter);

	int executeBySql(String sqlString, Object[] parameter);

	int execute(String hql);

	int execute(String hql, Object[] parameter);

	Long count(String hqlString);

	Long count(String hqlString, Map<String, Object> parameter);

}
