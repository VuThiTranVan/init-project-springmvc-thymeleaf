package com.spring.sample.dao.imp;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.spring.sample.dao.UserDAO;
import com.spring.sample.entity.User;
import com.spring.sample.util.CommonUtil;

@Repository
public class UserDAOImp extends GenericDAOImp<User, Integer> implements UserDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImp.class);

	public UserDAOImp() {
		super(User.class);
	}

	public User findUser(User user) {
		logger.info("Finding the user in the database");
		List<User> userList = (List<User>) getHibernateTemplate().findByExample(user);
		if (!CommonUtil.isEmpty(userList)) {
			return userList.get(0);
		}
		return null;
	}

	public User findUserByEmail(String email) {
		logger.info("Finding the user by email in the database");

		return getHibernateTemplate().execute(new HibernateCallback<User>() {
			public User doInHibernate(Session session) throws HibernateException {
				Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
				query.setParameter("email", email);
				return query.uniqueResult();
			}
		});
	}

	public boolean existingEmail(String email, Integer id) {
		logger.info("Finding the user by email in the database");
		return getHibernateTemplate().execute(new HibernateCallback<Long>() {
			public Long doInHibernate(Session session) throws HibernateException {
				String sql = "SELECT COUNT(*) FROM User WHERE email = :email";
				if (id != null) {
					sql += " AND id <> :id";
				}
				Query<Long> query = session.createQuery(sql, Long.class);
				query.setParameter("email", email);
				if (id != null) {
					query.setParameter("id", id);
				}
				return query.uniqueResult();
			}
		}) > 0;
	}

}
