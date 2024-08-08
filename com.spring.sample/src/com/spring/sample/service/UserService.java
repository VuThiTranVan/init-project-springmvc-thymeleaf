package com.spring.sample.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.spring.sample.model.UserModel;

public interface UserService extends UserDetailsService, PersistentTokenRepository {
	UserModel findUserByEmail(String email);

	boolean existingEmail(String email, Integer id);

	UserModel findUser(Integer id);

	UserModel addUser(UserModel user) throws Exception;

	UserModel editUser(UserModel userModel) throws Exception;

	boolean deleteUser(UserModel userModel) throws Exception;

	List<UserModel> findAll();

	Page<UserModel> paginate(UserModel userModel);
}
