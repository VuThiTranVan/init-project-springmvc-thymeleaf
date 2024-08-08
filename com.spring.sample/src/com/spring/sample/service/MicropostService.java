package com.spring.sample.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.sample.model.MicropostModel;

public interface MicropostService {
	MicropostModel findMicropost(Integer id);

	MicropostModel addMicropost(MicropostModel micropostModel) throws Exception;

	MicropostModel editMicropost(MicropostModel micropostModel) throws Exception;

	boolean deleteMicropost(MicropostModel micropostModel) throws Exception;

	List<MicropostModel> findAll(MicropostModel micropostModel);

	Page<MicropostModel> paginate(MicropostModel micropostModel);
	Long count(MicropostModel micropostModel);
}
