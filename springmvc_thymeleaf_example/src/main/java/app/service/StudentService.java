package app.service;

import java.util.List;

import app.model.Student;

public interface StudentService extends BaseService<Integer, Student> {
	boolean deleteStudent(Integer id);

	Student findByEmail(String email);

	List<Student> searchStudents(String name, int gender);

	List<Student> loadStudents();
}
