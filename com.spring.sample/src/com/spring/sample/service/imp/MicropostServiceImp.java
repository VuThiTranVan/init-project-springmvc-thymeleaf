package com.spring.sample.service.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.spring.sample.dao.MicropostDAO;
import com.spring.sample.entity.Micropost;
import com.spring.sample.model.MicropostModel;
import com.spring.sample.model.UserModel;
import com.spring.sample.service.MicropostService;

@Service
public class MicropostServiceImp extends BaseServiceImpl implements MicropostService {

	private static final Logger logger = LoggerFactory.getLogger(MicropostServiceImp.class);

	private MicropostServiceImp() {
	}

	public void setMicropostDAO(MicropostDAO micropostDAO) {
		this.micropostDAO = micropostDAO;
	}

	@Override
	public MicropostModel findMicropost(Integer id) {
		logger.info("Checking the micropost in the database");
		try {
			Micropost micropost = micropostDAO.find(id);
			MicropostModel micropostModel = null;
			if (micropost != null) {
				micropostModel = new MicropostModel();
				BeanUtils.copyProperties(micropost, micropostModel);
			}
			return micropostModel;
		} catch (Exception e) {
			logger.error("An error occurred while fetching the micropost details from the database", e);
			return null;
		}
	}

	@Override
	public MicropostModel addMicropost(MicropostModel micropostModel) throws Exception {
		logger.info("Adding the micropost in the database");
		try {
			Micropost condition = new Micropost();
			condition.setUserId(micropostModel.getUserId());
			condition.setContent(micropostModel.getContent());
			Micropost micropost = micropostDAO.makePersistent(condition);
			micropostModel = new MicropostModel();
			BeanUtils.copyProperties(micropost, micropostModel);
			return micropostModel;
		} catch (Exception e) {
			logger.error("An error occurred while adding the micropost details to the database", e);
			throw e;
		}
	}

	@Override
	public MicropostModel editMicropost(MicropostModel micropostModel) throws Exception {
		logger.info("Updating the micropost in the database");
		try {
			Micropost micropost = micropostDAO.find(micropostModel.getId(), true);
			if (StringUtils.hasText(micropostModel.getContent())) {
				micropost.setContent(micropostModel.getContent());
			}
			micropostDAO.makePersistent(micropost);
			return micropostModel;
		} catch (Exception e) {
			logger.error("An error occurred while updating the micropost details to the database", e);
			throw e;
		}
	}

	@Override
	public boolean deleteMicropost(MicropostModel micropostModel) throws Exception {
		logger.info("Deleting the micropost in the database");
		try {
			Micropost micropost = micropostDAO.find(micropostModel.getId(), true);
			micropostDAO.makeTransient(micropost);
			return true;
		} catch (Exception e) {
			logger.error("An error occurred while adding the micropost details to the database", e);
			throw e;
		}
	}

	@Override
	public List<MicropostModel> findAll(MicropostModel micropostModel) {
		logger.info("Fetching all microposts in the database");
		List<MicropostModel> micropostModelList = new ArrayList<MicropostModel>();
		try {
			Micropost condition = new Micropost();
			condition.setUserId(micropostModel.getUserId());
			List<Micropost> micropostList = micropostDAO.findByExample(condition);
			for (Micropost micropost : micropostList) {
				MicropostModel model = new MicropostModel();
				BeanUtils.copyProperties(micropost, model);
				micropostModelList.add(model);
			}
			return micropostModelList;
		} catch (Exception e) {
			logger.error("An error occurred while fetching all microposts from the database", e);
			return Collections.emptyList();
		}
	}

	@Override
	public Page<MicropostModel> paginate(MicropostModel micropostModel) {
		try {
			Micropost condition = new Micropost();
			condition.setUserId(micropostModel.getUserId());
			Page<Micropost> microposts = micropostDAO.paginate(condition, micropostModel.getPageable());
			return microposts.map(micropost -> {
				MicropostModel model = new MicropostModel();
				BeanUtils.copyProperties(micropost, model);
				UserModel user = new UserModel();
				BeanUtils.copyProperties(micropost.getUser(), user);
				model.setUser(user);
				return model;
			});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	

	@Override
	public Long count(MicropostModel micropostModel) {
		logger.info("Counting the micropost in the database");
		try {
			return micropostDAO.count(Restrictions.eq("userId", micropostModel.getUserId()));
		} catch (Exception e) {
			logger.error("An error occurred while counting the micropost details from the database", e);
			return (long) 0;
		}
	}
}
