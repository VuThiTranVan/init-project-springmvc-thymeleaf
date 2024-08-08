package com.spring.sample.dao;

import com.spring.sample.entity.User;

public interface UserDAO extends GenericDAO<User, Integer> {
	User findUser(User user);

	User findUserByEmail(String email);

	boolean existingEmail(String email, Integer id);

}
