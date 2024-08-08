package com.spring.sample.service.imp;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.sample.dao.MicropostDAO;
import com.spring.sample.dao.UserDAO;

public class BaseServiceImpl {

	@Autowired
	protected MicropostDAO micropostDAO;

	@Autowired
	protected UserDAO userDAO;

	public MicropostDAO getMicropostDAO() {
		return micropostDAO;
	}

	public void setMicropostDAO(MicropostDAO micropostDAO) {
		this.micropostDAO = micropostDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
}
