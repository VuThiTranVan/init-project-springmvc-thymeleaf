package com.spring.sample.service.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.spring.sample.entity.Role;
import com.spring.sample.entity.User;
import com.spring.sample.model.CustomUserDetails;
import com.spring.sample.model.UserModel;
import com.spring.sample.service.UserService;

@Service
public class UserServiceImp extends BaseServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	private UserServiceImp() {
	}

	@Override
	public UserModel findUserByEmail(String email) {
		logger.info("Fetching the user by email in the database");
		try {
			User user = userDAO.findUserByEmail(email);
			UserModel userModel = null;
			if (user != null) {
				userModel = new UserModel();
				BeanUtils.copyProperties(user, userModel);
			}
			return userModel;
		} catch (Exception e) {
			logger.error("An error occurred while fetching the user details by email from the database", e);
			return null;
		}
	}

	@Override
	public boolean existingEmail(String email, Integer id) {
		logger.info("Checking the user by email in the database");
		try {
			return userDAO.existingEmail(email, id);
		} catch (Exception e) {
			logger.error("An error occurred while checking the user details by email from the database", e);
			return true;
		}
	}

	@Override
	public UserModel findUser(Integer id) {
		logger.info("Checking the user in the database");
		try {
			User user = userDAO.find(id);
			UserModel userModel = null;
			if (user != null) {
				userModel = new UserModel();
				BeanUtils.copyProperties(user, userModel);
			}
			return userModel;
		} catch (Exception e) {
			logger.error("An error occurred while fetching the user details from the database", e);
			return null;
		}
	}

	@Override
	@Transactional
	public UserModel addUser(UserModel userModel) throws Exception {
		logger.info("Adding the user in the database");
		try {
			User condition = new User();
			condition.setName(userModel.getName());
			condition.setEmail(userModel.getEmail());
			condition.setPassword(passwordEncoder.encode(userModel.getPassword()));
			condition.setRole(Role.USER_ROLE);
			User user = userDAO.makePersistent(condition);
			userModel = new UserModel();
			BeanUtils.copyProperties(user, userModel);
			return userModel;
		} catch (Exception e) {
			logger.error("An error occurred while adding the user details to the database", e);
			throw e;
		}
	}

	@Override
	public UserModel editUser(UserModel userModel) throws Exception {
		logger.info("Updating the user in the database");
		try {
			User user = userDAO.find(userModel.getId(), true);
			if (StringUtils.hasText(userModel.getName())) {
				user.setName(userModel.getName());
			}
			if (StringUtils.hasText(userModel.getEmail())) {
				user.setEmail(userModel.getEmail());
			}
			if (StringUtils.hasText(userModel.getPassword())) {
				user.setPassword(passwordEncoder.encode(userModel.getPassword()));
			}
			userDAO.makePersistent(user);
			return userModel;
		} catch (Exception e) {
			logger.error("An error occurred while updating the user details to the database", e);
			throw e;
		}
	}

	@Override
	public boolean deleteUser(UserModel userModel) throws Exception {
		logger.info("Deleting the user in the database");
		try {
			User user = userDAO.find(userModel.getId(), true);
			userDAO.makeTransient(user);
			return true;
		} catch (Exception e) {
			logger.error("An error occurred while adding the user details to the database", e);
			throw e;
		}
	}

	@Override
	public List<UserModel> findAll() {
		logger.info("Fetching all users in the database");
		List<UserModel> userModelList = new ArrayList<UserModel>();
		try {
			List<User> userList = userDAO.findAll();
			for (User user : userList) {
				UserModel userModel = new UserModel();
				BeanUtils.copyProperties(user, userModel);
				userModelList.add(userModel);
			}
			return userModelList;
		} catch (Exception e) {
			logger.error("An error occurred while fetching all users from the database", e);
			return Collections.emptyList();
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userDAO.findUserByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(user, userModel);
		return new CustomUserDetails(userModel);
	}

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		try {
			User user = userDAO.findUserByEmail(token.getUsername());
			user.setSeries(token.getSeries());
			user.setToken(token.getTokenValue());
			user.setLastUsed(token.getDate());

			userDAO.makePersistent(user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		try {
			User condition = new User();
			condition.setSeries(series);
			User user = userDAO.findUser(condition);
			user.setToken(tokenValue);
			user.setLastUsed(lastUsed);

			userDAO.makePersistent(user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		try {
			User condition = new User();
			condition.setSeries(seriesId);
			User user = userDAO.findUser(condition);
			if (user != null) {
				return new PersistentRememberMeToken(user.getEmail(), user.getSeries(), user.getToken(),
						user.getLastUsed());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void removeUserTokens(String username) {
		try {
			User user = userDAO.findUserByEmail(username);
			user.setSeries(null);
			user.setToken(null);
			user.setLastUsed(null);

			userDAO.makePersistent(user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public Page<UserModel> paginate(UserModel userModel) {
		try {
			Page<User> users = userDAO.paginate(userModel.getPageable());
			return users.map(user -> {
				UserModel model = new UserModel();
				BeanUtils.copyProperties(user, model);
				return model;
			});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

}
