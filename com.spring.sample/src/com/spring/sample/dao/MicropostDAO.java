package com.spring.sample.dao;

import org.hibernate.criterion.Criterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.sample.entity.Micropost;

public interface MicropostDAO extends GenericDAO<Micropost, Integer> {
	Page<Micropost> paginate(Micropost micropost, Pageable pageable);
	public Long count(Criterion... criterion);

}
