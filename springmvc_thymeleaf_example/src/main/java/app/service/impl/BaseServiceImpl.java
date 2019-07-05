package app.service.impl;

import app.dao.StudentDAO;

public class BaseServiceImpl {
	protected StudentDAO studentDAO;

	public StudentDAO getStudentDAO() {
		return studentDAO;
	}

	public void setStudentDAO(StudentDAO studentDAO) {
		this.studentDAO = studentDAO;
	}
}
