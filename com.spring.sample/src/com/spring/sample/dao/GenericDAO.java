package com.spring.sample.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.sample.entity.BaseEntity;

public interface GenericDAO<E extends BaseEntity, Id extends Serializable> {

	E find(Id id);

	E find(Id id, boolean lock) throws Exception;

	List<E> findAll() throws Exception;

	List<E> findByExample(E exampleInstance);

	List<E> findByExample(E exampleInstance, String[] excludeProperty);

	E makePersistent(E entity) throws Exception;

	void makeTransient(E entity) throws Exception;

	List<E> findByCriteria(Criterion... criterion);

	Timestamp getSystemTimestamp();

	Page<E> paginate(Pageable pageable);
//	int count(E exampleInstance, String[] excludeProperty, boolean isLike);
//
//	int count();
//
//	int count(Criterion... criterion);

}
