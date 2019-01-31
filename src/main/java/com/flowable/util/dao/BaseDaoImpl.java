package com.flowable.util.dao;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import com.flowable.util.PageHelper;
import com.flowable.util.ReflectionUtils;

/**
 * @param <T>
 * @author 26223
 * @time 2016年10月13日
 * @email lukw@eastcom-sw.com
 */
@SuppressWarnings("unchecked")
public class BaseDaoImpl<T> implements IBaseDao<T> {

	@PersistenceContext
	private EntityManager entityManager;

	private Class<?> entityClass;

	private Session session;

	public BaseDaoImpl() {
		entityClass = ReflectionUtils.getGenderClass(this.getClass());
		session = entityManager.unwrap(Session.class);
	}

	@Override
	public Serializable save(T t) {
		if (t != null) {
			return this.session.save(t);
		}
		return null;
	}

	@Override
	public void update(T t) {
		if (t != null) {
			this.session.update(t);
		}
	}

	@Override
	public void saveOrUpdate(T t) {
		if (t != null) {
			this.session.saveOrUpdate(t);
		}
	}

	@Override
	public void saveOrUpdate(List<T> list) {

		if (list != null && !list.isEmpty()) {
			for (T t : list) {
				this.saveOrUpdate(t);
			}
		}
	}

	@Override
	public void delete(T t) {
		if (t != null) {
			this.session.delete(t);
		}
	}

	@Override
	public void delete(List<T> list) {

		if (list != null && !list.isEmpty()) {
			for (T t : list) {
				this.delete(t);
			}
		}
	}

	@Override
	public void deleteByIds(List<String> list) {

		if (list != null && !list.isEmpty()) {
			for (String id : list) {
				this.deleteById(id);
			}
		}
	}

	@Override
	public void deleteById(String id) {

		T t = (T) this.session.get(entityClass, id);
		if (t != null) {
			this.delete(t);
		}
	}

	@Override
	public Criteria getCriteria() {

		return this.session.createCriteria(entityClass);
	}

	@Override
	public T getById(String id) {

		return (T) this.session.get(entityClass, id);
	}

	@Override
	public T getUniqueResult(String propertyName, Object value) {

		return (T) this.getCriteria().add(Restrictions.eq(propertyName, value)).uniqueResult();
	}

	@Override
	public List<T> find(Criteria criteria) {
		return criteria.list();
	}

	@Override
	public List<T> findAll() {

		return this.session.createCriteria(entityClass).list();
	}

	/////////////////////// HQL
	/////////////////////// query////////////////////////////////////////////////////////////////////////////////
	@Override
	public <E> List<E> find(String hql) {
		return this.createQuery(hql).getResultList();
	}

	@Override
	public int execute(String hql) {
		Query query = createQuery(hql);
		return query.executeUpdate();
	}

	@Override
	public int execute(String hql, Object[] parameter) {
		Query query = createQuery(hql);
		this.setParameter(query, parameter);
		return query.executeUpdate();
	}

	@Override
	public <E> List<E> find(String hql, Object parameter) {

		Query query = this.createQuery(hql);
		this.setParameter(query, parameter);
		return query.getResultList();
	}

	@Override
	public List<T> findByParams(Map<String, Object> params, boolean like) {

		DetachedCriteria detachedCriteria = this.getDetachedCriteriaByParams(params, like);
		return detachedCriteria.getExecutableCriteria(getCurrentSession()).list();
	}

	@Override
	public PageHelper<T> findByParams(Map<String, Object> params, PageHelper<T> page, boolean like) {

		DetachedCriteria detachedCriteria = this.getDetachedCriteriaByParams(params, like);
		PageHelper<T> result = new PageHelper<T>();
		result.setCount(this.count(detachedCriteria));
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		if (page != null && page.getRows() > 0) {
			criteria.setFirstResult(page.getFirstRow());
			criteria.setMaxResults(page.getMaxRow());
		}
		result.setList(criteria.list());
		return result;
	}

	private DetachedCriteria getDetachedCriteriaByParams(Map<String, Object> params, boolean like) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
		List<Field> fields = ReflectionUtils.getFields(entityClass);
		for (Field field : fields) {
			String fieldName = field.getName();
			Class<?> clazz = field.getType();
			if (params.get(fieldName) != null) {
				if (clazz == String.class && like) {
					detachedCriteria.add(Restrictions.like(fieldName, "%" + params.get(fieldName) + "%"));
				} else {
					detachedCriteria.add(Restrictions.eq(fieldName, params.get(fieldName)));
				}
			}
		}
		return detachedCriteria;
	}

	@Override
	public <E> PageHelper<E> find(PageHelper<E> page, String hql) {

		this.setHqlQueryCount(page, hql, null);
		if (page.getCount() < 1) {
			return page;
		}
		this.setHQLParameter(page, hql, null);
		return page;
	}

	@Override
	public <E> PageHelper<E> find(PageHelper<E> page, String hql, Object parameter) {

		this.setHqlQueryCount(page, hql, parameter);
		if (page.getCount() < 1) {
			return page;
		}
		this.setHQLParameter(page, hql, parameter);
		return page;
	}

	private <E> void setHQLParameter(PageHelper<E> page, String hql, Object parameter) {

		if (StringUtils.isNotBlank(page.getSort())) {
			hql += " order by " + page.getSort();
			if (StringUtils.isNotBlank(page.getOrder())) {
				hql += " " + page.getOrder();
			}
		}
		Query query = createQuery(hql);
		this.setParameter(query, parameter);
		if (page.getRows() > 0) {
			query.setFirstResult(page.getFirstRow());
			query.setMaxResults(page.getMaxRow());
		}
		page.setList(query.getResultList());
	}

	private <E> void setHqlQueryCount(PageHelper<E> page, String hql, Object parameter) {

		if (hql.contains("distinct")) {
			String countHql = removeOrders(hql);
			Query count = createQuery(countHql);
			this.setParameter(count, parameter);
			page.setCount(count.getResultList().size());
		} else {
			String countHql = "select count(*) " + removeSelect(removeOrders(hql));
			Query query = createQuery(countHql);
			this.setParameter(query, parameter);
			List<Object> list = query.getResultList();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
		}
	}

	@Override
	public Long count(String hqlString) {
		Query q = this.createQuery(hqlString);
		return (Long) q.getSingleResult();
	}

	@Override
	public Long count(String hqlString, Map<String, Object> parameter) {
		Query query = this.createQuery(hqlString);
		this.setParameter(query, parameter);
		return (Long) query.getSingleResult();
	}

	//////////////////////////// SQL
	//////////////////////////// query//////////////////////////////////////////////////////////////////////////
	@Override
	public <E> List<E> findBySql(String sql, Class<?> clazz) {

		Query query = this.createSQLQuery(sql,clazz);
		return query.getResultList();
	}

	@Override
	public <E> List<E> findBySql(String sql, Object parameter, Class<?> clazz) {

		Query query = this.createSQLQuery(sql,clazz);
		this.setParameter(query, parameter);
		return query.getResultList();
	}

	@Override
	public <E> List<E> findBySql(String sql, Object parameter) {

		Query query = this.createSQLQuery(sql);
		this.setParameter(query, parameter);
		return query.getResultList();
	}

	@Override
	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sql) {

		this.setSQLQueryCount(page, sql, null);
		if (page.getCount() < 1) {
			return page;
		}
		this.setSQLQueryParameter(page, sql, null, null);
		return page;
	}

	@Override
	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sql, Class<?> resultClass) {

		this.setSQLQueryCount(page, sql, null);
		if (page.getCount() < 1) {
			return page;
		}
		this.setSQLQueryParameter(page, sql, null, resultClass);
		return page;
	}

	@Override
	public int executeBySql(String sqlString) {
		Query query = createSQLQuery(sqlString);
		return query.executeUpdate();
	}

	@Override
	public int executeBySql(String sqlString, Map<String, Object> parameter) {
		Query query = createSQLQuery(sqlString);
		this.setParameter(query, parameter);
		return query.executeUpdate();
	}

	@Override
	public int executeBySql(String sqlString, Object[] parameter) {

		Query query = createSQLQuery(sqlString);
		this.setParameter(query, parameter);
		return query.executeUpdate();
	}

	@Override
	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sql, Object parameter, Class<?> resultClass) {

		this.setSQLQueryCount(page, sql, parameter);
		if (page.getCount() < 1) {
			return page;
		}
		this.setSQLQueryParameter(page, sql, parameter, resultClass);
		return page;
	}

	private <E> void setSQLQueryCount(PageHelper<E> page, String sql, Object parameter) {

		sql = sql.toLowerCase();
		if (sql.contains("distinct")) {
			String countSql = removeOrders(sql);
			Query count = this.createSQLQuery(countSql);
			this.setParameter(count, parameter);
			page.setCount(count.getResultList().size());
		} else {
			String countSql = "select count(*) " + removeSelect(removeOrders(sql));
			Query count = this.createSQLQuery(countSql);
			this.setParameter(count, parameter);
			List<Object> list = count.getResultList();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
		}
	}

	private <E> void setSQLQueryParameter(PageHelper<E> page, String sql, Object parameter, Class<?> resultClass) {

		if (StringUtils.isNotBlank(page.getSort())) {
			this.removeOrders(sql);
			sql += " order by " + page.getSort();
			if (StringUtils.isNotBlank(page.getOrder())) {
				sql += " " + page.getOrder();
			}
		}
		Query query = this.createSQLQuery(sql,resultClass);
		this.setParameter(query, parameter);
		if (page.getRows() > 0) {
			query.setFirstResult(page.getFirstRow());
			query.setMaxResults(page.getMaxRow());
		}
		page.setList(query.getResultList());
	}

	@Override
	public Session getCurrentSession() {

		return this.session;
	}

	@Override
	public Query createQuery(String hql) {

		return this.entityManager.createQuery(hql);
	}

	@Override
	public Query createSQLQuery(String sql) {

		return this.createSQLQuery(sql, entityClass);
	}
	
	@Override
	public Query createSQLQuery(String sql,Class<?> resultClass) {

		return this.entityManager.createNativeQuery(sql, resultClass);
	}

	/**
	 * 设置查询参数
	 *
	 * @param query
	 * @param parameter List Map Object[]
	 */
	private void setParameter(Query query, Object parameter) {

		if (parameter != null) {
			if (parameter instanceof Map) {
				Map<String, Object> param = (Map<String, Object>) parameter;
				this.setParameter(query, param);
			} else if (parameter instanceof List) {
				List<Object> list = (List<Object>) parameter;
				this.setParameter(query, list.toArray());
			} else if (parameter instanceof Object[]) {
				Object[] array = (Object[]) parameter;
				this.setParameter(query, array);
			} else {
				throw new RuntimeException(" setParameter error, parameter's type is not in List Map Object[]");
			}
		}
	}

	private void setParameter(Query query, Map<String, Object> parameter) {
		if (parameter != null) {
			Set<String> keySet = parameter.keySet();
			for (String string : keySet) {
				query.setParameter(string, parameter.get(string));
			}
		}
	}

	private void setParameter(Query query, Object[] parameter) {
		if (parameter != null && parameter.length > 0) {
			for (int i = 0; i < parameter.length; i++) {
				query.setParameter(i, parameter[i]);
			}
		}
	}

	/**
	 * 去除sql的select子句。
	 *
	 * @param sql
	 * @return
	 */
	private String removeSelect(String sql) {
		int beginPos = sql.toLowerCase().indexOf("from");
		return sql.substring(beginPos);
	}

	/**
	 * 去除hql的orderBy子句。
	 *
	 * @param hql
	 * @return
	 */
	private String removeOrders(String hql) {
		Pattern pattern = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "");
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	@Override
	public boolean check(List<T> list, T o) {

		if (list == null || list.isEmpty()) {
			return true;
		} else {
			int size = list.size();
			if (size > 1) {
				return false;
			} else {
				if (size == 1) {
					T t = list.get(0);
					String tid = ReflectionUtils.getter(t, "id").toString();
					String oid = ReflectionUtils.getter(o, "id") == null ? null
							: ReflectionUtils.getter(o, "id").toString();
					if (tid.equals(oid)) {
						return false;
					} else {
						return true;
					}
				}
			}
		}
		return true;
	}

	// -------------- Criteria --------------

	protected T get(DetachedCriteria detachedCriteria) {
		return get(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}

	protected T get(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		criteria.setResultTransformer(resultTransformer);
		return (T) criteria.uniqueResult();
	}

	/**
	 * 分页查询
	 *
	 * @param page
	 * @return
	 */
	protected PageHelper<T> find(PageHelper<T> page) {
		return find(page, createDetachedCriteria());
	}

	/**
	 * 使用检索标准对象分页查询
	 *
	 * @param page
	 * @param detachedCriteria
	 * @return
	 */
	protected PageHelper<T> find(PageHelper<T> page, DetachedCriteria detachedCriteria) {
		return find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}

	/**
	 * 使用检索标准对象分页查询
	 *
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */

	protected PageHelper<T> find(PageHelper<T> page, DetachedCriteria detachedCriteria,
			ResultTransformer resultTransformer) {
		// get count
		page.setCount(count(detachedCriteria));
		if (page.getCount() < 1) {
			return page;
		}
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		criteria.setResultTransformer(resultTransformer);
		if (page.getRows() > 0) {
			criteria.setFirstResult(page.getFirstRow());
			criteria.setMaxResults(page.getMaxRow());
		}
		if (StringUtils.isNotBlank(page.getSort())) {
			String[] fields = page.getSort().split(",");
			String[] orders = null;
			if (StringUtils.isNotBlank(page.getOrder()))
				orders = page.getOrder().split(",");
			for (int i = 0; i < fields.length; i++) {
				String f = fields[i];
				if (null != orders && orders.length > 0) {
					if ("DESC".equals(orders[i].toUpperCase())) {
						criteria.addOrder(Order.desc(f));
					} else {
						criteria.addOrder(Order.asc(f));
					}
				} else {
					criteria.addOrder(Order.asc(f));
				}
			}
		}

		page.setList(criteria.list());
		return page;

	}

	/**
	 * 分页查询
	 *
	 * @return
	 */
	protected List<T> find(DetachedCriteria detachedCriteria, int firstResult, int maxResults) {
		return find(detachedCriteria, firstResult, maxResults, Criteria.DISTINCT_ROOT_ENTITY);
	}

	/**
	 * 使用检索标准对象分页查询
	 *
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */

	protected List<T> find(DetachedCriteria detachedCriteria, int firstResult, int maxResults,
			ResultTransformer resultTransformer) {

		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		criteria.setResultTransformer(resultTransformer);
		// set page
		if (firstResult > -1 || maxResults > -1) {
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(maxResults);
		}
		return criteria.list();
	}

	/**
	 * 使用检索标准对象查询
	 *
	 * @param detachedCriteria
	 * @return
	 */
	protected <E> List<E> find(DetachedCriteria detachedCriteria) {
		return find(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}

	/**
	 * 使用检索标准对象查询
	 *
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */

	protected <E> List<E> find(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		criteria.setResultTransformer(resultTransformer);
		return criteria.list();
	}

	/**
	 * 使用检索标准对象查询记录数
	 *
	 * @param detachedCriteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Long count(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		Long totalCount = 0L;
		try {
			// Get orders
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			List orderEntrys = (List) field.get(criteria);
			// Remove orders
			field.set(criteria, new ArrayList());
			// Remove limit;
			criteria.setFirstResult(0);
			criteria.setMaxResults(0);
			// Get count
			criteria.setProjection(Projections.rowCount());
			totalCount = Long.valueOf(criteria.uniqueResult().toString());
			// Clean count
			criteria.setProjection(null);
			// Restore orders
			field.set(criteria, orderEntrys);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	/**
	 * 创建与会话无关的检索标准对象
	 *
	 * @param criterions Restrictions.eq("name", value);
	 * @return
	 */
	protected DetachedCriteria createDetachedCriteria(Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		for (Criterion c : criterions) {
			dc.add(c);
		}
		return dc;
	}

	/**
	 * 创建与会话无关的检索标准对象
	 *
	 * @param criterions Restrictions.eq("name", value);
	 * @return
	 */
	protected DetachedCriteria createDetachedCriteria(T o, Criterion... criterions) {
		return createDetachedCriteria(o, MatchMode.ANYWHERE, criterions);
	}

	/**
	 * 创建与会话无关的检索标准对象
	 *
	 * @param criterions Restrictions.eq("name", value);
	 * @return
	 */
	protected DetachedCriteria createDetachedCriteria(T o, MatchMode mode, Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(o.getClass());
		for (Criterion c : criterions) {
			dc.add(c);
		}
		if (o != null) {
			Example example = Example.create(o);
			example.enableLike(mode);
			example.excludeNone();// 空的不做查询条件
			example.excludeZeroes();// 0不要查询
			example.ignoreCase();// 忽略大小写
			dc.add(example);
		}
		return dc;
	}

	/**
	 * 根据MAP封装成查询条件对象，指定MatchMode
	 *
	 * @param param
	 * @param mode
	 * @return
	 */
	protected DetachedCriteria buildCriteriaByMap(Map<String, Object> param, MatchMode mode) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		if (param == null) {
			return dc;
		}
		for (Map.Entry<String, Object> entity : param.entrySet()) {
			Field field = ReflectionUtils.getDeclaredField(entityClass, entity.getKey());
			if (field == null || entity.getValue() == null || entity.getValue().toString().equals("")) {
				continue;
			}
			Annotation anno = field.getAnnotation(Transient.class);
			if (anno != null) {
				continue;
			}
			Object value = ReflectionUtils.convert(entity.getValue(), field.getType());
			if (value == null) {
				continue;
			}
			if (field.getType().equals(String.class)) {
				value = "%" + value + "%";
				dc.add(Restrictions.ilike(field.getName(), value));
			} else {
				dc.add(Restrictions.eq(field.getName(), value));
			}
		}
		return dc;
	}

	/**
	 * 根据MAP封装成查询条件对象
	 *
	 * @param param
	 * @return
	 */
	protected DetachedCriteria buildCriteriaByMap(Map<String, Object> param) {
		return buildCriteriaByMap(param, MatchMode.ANYWHERE);
	}
}