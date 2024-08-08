package com.spring.sample.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.sample.model.UserModel;
import com.spring.sample.validator.UserValidator;

@Controller
public class RegisterController {

	private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

	@Autowired
	MessageSource messageSource;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator());
	}
	
	@ModelAttribute("user")
	public UserModel createUserModel() {
		// ModelAttribute value should be same as used in the empSave.jsp
		return new UserModel();
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Locale locale, Model model) {
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@ModelAttribute("user") @Validated UserModel user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			logger.info("Returning register.jsp page, validate failed");
			return "register";
		}
		model.addAttribute("name", user.getName());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("password", user.getPassword());
		return "user";
	}

}