package app.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import app.model.Student;
import app.service.StudentService;

public class StudentServiceImpl extends BaseServiceImpl implements StudentService {
	private static final Logger logger = Logger.getLogger(StudentServiceImpl.class);

	@Override
	public Student saveOrUpdate(Student entity) {
		try {
			return getStudentDAO().saveOrUpdate(entity);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public boolean deleteStudent(Integer id) {
		try {
			Student student = getStudentDAO().findById(id);
			return delete(student);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public Student findByEmail(String email) {
		try {
			return getStudentDAO().findByEmail(email);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Student> searchStudents(String name, int gender) {
		try {
			return getStudentDAO().searchStudentUsingCretial(name, gender);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Student> loadStudents() {
		try {
			return getStudentDAO().loadStudents();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Student findById(Serializable key) {
		try {
			return getStudentDAO().findById(key);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean delete(Student entity) {
		try {
			getStudentDAO().delete(entity);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

}
