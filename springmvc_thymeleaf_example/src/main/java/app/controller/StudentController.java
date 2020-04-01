package app.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.model.Student;
import app.service.StudentService;

@Controller
public class StudentController {
	private static final Logger logger = Logger.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	@RequestMapping(value = { "/", "/students"})
	public ModelAndView index() {
		logger.info("home page");
		ModelAndView model = new ModelAndView("views/students/index");
		model.addObject("student", new Student());
		model.addObject("students", studentService.loadStudents());
		return model;
	}

	@RequestMapping(value = "/students/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") int id, Model model) {
		logger.info("detail student");
		Student student = studentService.findById(id);
		if (student == null) {
			model.addAttribute("css", "danger");
			model.addAttribute("msg", "Student not found");
		}
		model.addAttribute("student", student);
		return "views/students/student";
	}

	@RequestMapping(value = "/students/{id}/delete", method = RequestMethod.GET)
	public String deleteStudent(@PathVariable("id") Integer id, final RedirectAttributes redirectAttributes) {
		logger.info("delete student");
		if (studentService.deleteStudent(id)) {
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Student is deleted!");
		} else {
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("msg", "Delete student fails!!!!");
		}

		return "redirect:/";

	}

	@RequestMapping(value = "/students/add", method = RequestMethod.GET)
	public String newStudent(Model model) {
		Student student = new Student();

		// set default value
		student.setGender(0);
		student.setId(-1);

		model.addAttribute("studentForm", student);
		model.addAttribute("status", "add");

		return "views/students/student-form";

	}
	
	@RequestMapping(value = "/students", method = RequestMethod.POST)
	public String createOrUpdate(Student studentForm, final RedirectAttributes redirectAttributes) {
		System.out.println("blabla");
		Student student;
		if(studentForm.getId() != -1) { // edit
			student = studentService.saveOrUpdate(studentForm);
		} else { // create
			student = studentService.save(studentForm);
		}
		
		if (student != null) {
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Student is create!");
		} else {
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("msg", "Create student fails!!!!");
		}
		return "redirect:/";
	}
	

	@RequestMapping(value = "/students/{id}/edit", method = RequestMethod.GET)
	public String editStudent(@PathVariable("id") int id, Model model) {

		Student student = studentService.findById(id);
		model.addAttribute("studentForm", student);
		model.addAttribute("status", "edit");

		return "views/students/student-form";

	}

}
