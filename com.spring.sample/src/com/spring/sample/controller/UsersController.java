package com.spring.sample.controller;

import java.util.Locale;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.sample.interceptor.Flash;
import com.spring.sample.model.MicropostModel;
import com.spring.sample.model.UserModel;
import com.spring.sample.service.MicropostService;
import com.spring.sample.service.UserService;

@Controller
@EnableWebMvc
public class UsersController {
	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	UserService userService;

	@Autowired
	MicropostService micropostService;

	@Resource
	Flash flash;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	}

	@GetMapping(value = "/users")
	public String index(@RequestParam(name = "page", required = false) Optional<Integer> page, Locale locale,
			Model model, HttpServletRequest request) {
		UserModel userModel = new UserModel();
		userModel.setPage(page.orElse(1));
		Page<UserModel> users = userService.paginate(userModel);
		model.addAttribute("users", users);
		return "users/index";
	}
	
	@GetMapping(value = "/users/{id}")
	public String show(@PathVariable Integer id, @RequestParam(name = "page", required = false) Optional<Integer> page, Model model) {
		UserModel userModel = userService.findUser(id);
		model.addAttribute("user", userModel);
		MicropostModel micropostModel = new MicropostModel();
		micropostModel.setUserId(userModel.getId());
		micropostModel.setPage(page.orElse(1));
		Page<MicropostModel> microposts = micropostService.paginate(micropostModel);
		model.addAttribute("microposts", microposts);
		return "users/show";
	}

	@GetMapping(value = { "/users/add", "/signup" })
	public String add(Locale locale, Model model) {
		model.addAttribute("user", new UserModel());
		return "users/add";
	}

	@PostMapping(value = "/users")
	public String create(@ModelAttribute("user") @Validated UserModel userModel, BindingResult bindingResult,
			Model model, final RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		if (bindingResult.hasErrors()) {
			logger.info("Returning register.jsp page, validate failed");
			return "users/add";
		}
		UserModel user = userService.addUser(userModel);
		// Add message to flash scope
		flash.success("user.create.success");
		flash.keep();
		request.login(userModel.getEmail(), userModel.getPassword());
		return "redirect: " + request.getContextPath() + "/users/" + user.getId();
	}

	@GetMapping(value = "/users/{id}/edit")
	public String edit(@PathVariable Integer id, Model model) {
		model.addAttribute("user", userService.findUser(id));
		return "users/edit";
	}

	@PutMapping(value = "/users/{id}")
	public String update(@ModelAttribute("user") @Validated UserModel userModel, BindingResult bindingResult,
			Model model, final RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		if (bindingResult.hasErrors()) {
			logger.info("Returning edit.jsp page, validate failed");
			return "users/edit";
		}
		UserModel user = userService.editUser(userModel);
		// Add message to flash scope
		flash.success("user.update.success");
		flash.keep();
		return "redirect: " + request.getContextPath() + "/users/" + user.getId();
	}

	@DeleteMapping(value = "/users/{id}", produces = { MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<String> destroy(@PathVariable Integer id, Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Deleting user: " + id);
		userService.deleteUser(new UserModel(id));
		String contectType = request.getContentType();
		if (contectType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
			return new ResponseEntity<String>("{\"result\" : \"OK\", \"id\" : " + id + ", \"model\" : \"user\"}",
					HttpStatus.OK);
		} else {
			response.sendRedirect(request.getContextPath() + "/users");
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/users/{id}/following")
	public String following(@PathVariable Integer id, Model model) {
		model.addAttribute("users", userService.findAll());
		model.addAttribute("title", "Following");
		return "users/edit";
	}
	
	@GetMapping(value = "/users/{id}/followers")
	public String followers(@PathVariable Integer id, Model model) {
		model.addAttribute("users", userService.findAll());
		model.addAttribute("title", "Followers");
		return "users/edit";
	}

}
