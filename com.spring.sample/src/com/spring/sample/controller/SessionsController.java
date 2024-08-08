package com.spring.sample.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
@EnableWebMvc
public class SessionsController {
	private static final Logger logger = LoggerFactory.getLogger(SessionsController.class);

	@GetMapping(value = "/login")
	public String add(Locale locale, Model model) {
		logger.info("Requesting login form");
		return "sessions/login";
	}

}
