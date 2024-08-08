package com.spring.sample.dao.imp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.spring.sample.dao.GenericDAO;
import com.spring.sample.entity.BaseEntity;
import com.spring.sample.util.SearchQueryTemplate;

public abstract class GenericDAOImp<E extends BaseEntity, Id extends Serializable> extends HibernateDaoSupport
		implements GenericDAO<E, Id> {

	@Autowired
	private SessionFactory sessionFactory;

	private Class<E> persistentClass;

	public GenericDAOImp(Class<E> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public Class<E> getPersistentClass() {
		return persistentClass;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	// --------------------------------
	@Override
	public E find(Id id) {
		return (E) getSession().get(getPersistentClass(), id);
	}

	public E find(Id id, boolean lock) throws Exception {
		if (lock) {
			return getSession().load(getPersistentClass(), id, LockMode.PESSIMISTIC_WRITE);
		}
		return getSession().get(getPersistentClass(), id);
	}

	public List<E> findAll() {
		return findByCriteria();
	}

	public List<E> findByExample(E exampleInstance) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();

		CriteriaQuery<E> criteria = builder.createQuery(getPersistentClass());
		Root<E> root = criteria.from(getPersistentClass());
		criteria.select(root);

		return getSession().createQuery(criteria).getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<E> findByExample(E exampleInstance, String[] excludeProperty) {
		Criteria criteria = getSession().createCriteria(getPersistentClass().getName());
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		criteria.add(example);
		return (List<E>) criteria.list();
	}

	public E makePersistent(E entity) throws Exception {
		Date currentDate = getSystemTimestamp();
		if (entity.getCreatedAt() == null) {
			entity.setCreatedAt(currentDate);
		}
		entity.setUpdatedAt(currentDate);
		getHibernateTemplate().saveOrUpdate(entity);
		getHibernateTemplate().flush();
		return entity;
	}

	public void makeTransient(E entity) throws Exception {
		getHibernateTemplate().delete(entity);
		getHibernateTemplate().flush();
	}

	@SuppressWarnings("unchecked")
	public List<E> findByCriteria(Criterion... criterion) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
		for (Criterion c : criterion) {
			criteria.add(c);
		}
		return (List<E>) getHibernateTemplate().findByCriteria(criteria);
	}

	public Timestamp getSystemTimestamp() {
		String sql = "SELECT CURRENT_TIMESTAMP AS systemtimestamp";
		Object obj = getHibernateTemplate().execute(session -> session.createNativeQuery(sql).uniqueResult());
		Timestamp syatemTimestamp = null;
		if (obj != null) {
			syatemTimestamp = (Timestamp) obj;
		}
		return syatemTimestamp;
	}

	public Page<E> paginate(Pageable pageable) {
		String sql = "FROM " + getPersistentClass().getName();
		String countSql = "SELECT COUNT(*) FROM " + getPersistentClass().getName();
		return paginate(new SearchQueryTemplate(sql, countSql, pageable));
	}

	protected Page<E> paginate(SearchQueryTemplate searchQueryTemplate) {

		Query<E> query = getSession().createQuery(searchQueryTemplate.getSql(true), getPersistentClass());
		searchQueryTemplate.setPageable(query);
		searchQueryTemplate.setParameters(query);
		List<E> results = query.list();

		Query<Long> queryCount = getSession().createQuery(searchQueryTemplate.getCountSql(), Long.class);
		searchQueryTemplate.setParameters(queryCount);
		Long count = queryCount.uniqueResult();

		return wrapResult(results, searchQueryTemplate.getPageable(), count);
	}

	private Page<E> wrapResult(List<E> results, Pageable page, long count) {
		if (results == null) {
			results = Collections.emptyList();
		}
		return new PageImpl<>(results, page, count);
	}

	// --------------------------------
	/**
	 * Code nay dang su dung HibernateTemplate
	 */
//	@SuppressWarnings("unchecked")
//	public E find(Id id) {
//		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
//		criteria.add(Restrictions.eq("id", id));
//		return (E) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
//	}
//
//	public E find(Id id, boolean lock) throws Exception {
//		if (lock) {
//			return getHibernateTemplate().load(getPersistentClass(), id, LockMode.PESSIMISTIC_WRITE);
//		}
//		return getHibernateTemplate().get(getPersistentClass(), id);
//	}
//
//	public List<E> findAll() {
//		return findByCriteria();
//	}
//
//	public List<E> findByExample(E exampleInstance) {
//		return getHibernateTemplate().findByExample(exampleInstance);
//	}
//
//	@SuppressWarnings("unchecked")
//	public List<E> findByExample(E exampleInstance, String[] excludeProperty) {
//		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
//		Example example = Example.create(exampleInstance);
//		for (String exclude : excludeProperty) {
//			example.excludeProperty(exclude);
//		}
//		criteria.add(example);
//		return (List<E>) getHibernateTemplate().findByCriteria(criteria);
//	}
//
//	public int count(E exampleInstance, String[] excludeProperty, boolean isLike) {
//		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
//		Example example = Example.create(exampleInstance);
//		for (String exclude : excludeProperty) {
//			example.excludeProperty(exclude);
//		}
//		if (isLike) {
//			example.enableLike(MatchMode.ANYWHERE).ignoreCase();
//		}
//		return DataAccessUtils.intResult(
//				getHibernateTemplate().findByCriteria(criteria.add(example).setProjection(Projections.rowCount())));
//	}
//
//	public int count() {
//		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
//		return DataAccessUtils
//				.intResult(getHibernateTemplate().findByCriteria(criteria.setProjection(Projections.rowCount())));
//	}
//
//	public int count(Criterion... criterion) {
//		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
//		for (Criterion c : criterion) {
//			criteria.add(c);
//		}
//		return DataAccessUtils
//				.intResult(getHibernateTemplate().findByCriteria(criteria.setProjection(Projections.rowCount())));
//	}
//
//	public E makePersistent(E entity) throws Exception {
//		Date currentDate = getSystemTimestamp();
//		if (entity.getCreatedAt() == null) {
//			entity.setCreatedAt(currentDate);
//		}
//		entity.setUpdatedAt(currentDate);
//		getHibernateTemplate().saveOrUpdate(entity);
//		getHibernateTemplate().flush();
//		return entity;
//	}
//
//	public void makeTransient(E entity) throws Exception {
//		getHibernateTemplate().delete(entity);
//		getHibernateTemplate().flush();
//	}
//
//	@SuppressWarnings("unchecked")
//	public List<E> findByCriteria(Criterion... criterion) {
//		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
//		for (Criterion c : criterion) {
//			criteria.add(c);
//		}
//		return (List<E>) getHibernateTemplate().findByCriteria(criteria);
//	}
//
//	public Timestamp getSystemTimestamp() {
//		String sql = "SELECT CURRENT_TIMESTAMP AS systemtimestamp";
//		Object obj = getHibernateTemplate().execute(session -> session.createNativeQuery(sql).uniqueResult());
//		Timestamp syatemTimestamp = null;
//		if (obj != null) {
//			syatemTimestamp = (Timestamp) obj;
//		}
//		return syatemTimestamp;
//	}
//
//	public Page<E> paginate(Pageable pageable) {
//		String sql = "FROM " + getPersistentClass().getName();
//		String countSql = "SELECT COUNT(*) FROM " + getPersistentClass().getName();
//		return paginate(new SearchQueryTemplate(sql, countSql, pageable));
//	}
//
//	protected Page<E> paginate(SearchQueryTemplate searchQueryTemplate) {
//		List<E> results = getHibernateTemplate().execute(new HibernateCallback<List<E>>() {
//			public List<E> doInHibernate(Session session) {
//				Query<E> query = session.createQuery(searchQueryTemplate.getSql(true), getPersistentClass());
//				searchQueryTemplate.setPageable(query);
//				searchQueryTemplate.setParameters(query);
//				return query.list();
//			}
//		});
//
//		Long count = getHibernateTemplate().execute(new HibernateCallback<Long>() {
//			public Long doInHibernate(Session session) {
//				Query<Long> query = session.createQuery(searchQueryTemplate.getCountSql(), Long.class);
//				searchQueryTemplate.setParameters(query);
//				return query.uniqueResult();
//			}
//		});
//
//		return wrapResult(results, searchQueryTemplate.getPageable(), count);
//	}
//

}
