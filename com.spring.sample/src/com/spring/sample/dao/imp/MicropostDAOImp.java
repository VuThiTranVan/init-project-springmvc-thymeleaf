package com.spring.sample.dao.imp;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.spring.sample.dao.MicropostDAO;
import com.spring.sample.entity.Micropost;
import com.spring.sample.util.SearchQueryTemplate;

@Repository
public class MicropostDAOImp extends GenericDAOImp<Micropost, Integer> implements MicropostDAO {

	public MicropostDAOImp() {
		super(Micropost.class);
	}

	@Override
	public Page<Micropost> paginate(Micropost micropost, Pageable pageable) {
		String sql = "FROM Micropost WHERE userId = :userId";
		String countSql = "SELECT COUNT(*) FROM Micropost WHERE userId = :userId";
		SearchQueryTemplate searchQueryTemplate = new SearchQueryTemplate(sql, countSql, pageable);
		searchQueryTemplate.addParameter("userId", micropost.getUserId());
		searchQueryTemplate.addOrder(Direction.DESC, "createdAt");
		return paginate(searchQueryTemplate);
	}

	@Override	
	public Long count(Criterion... criterion) {
		Criteria criteria = getSession().createCriteria(Micropost.class);
		criteria.setProjection(Projections.rowCount());

		return (Long) criteria.uniqueResult();
	}

}
