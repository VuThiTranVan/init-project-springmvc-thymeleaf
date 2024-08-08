package com.spring.sample.controller;

import java.util.Locale;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.sample.interceptor.Flash;
import com.spring.sample.model.CustomUserDetails;
import com.spring.sample.model.MicropostModel;
import com.spring.sample.service.MicropostService;

@Controller
public class StaticPagesController {

	private static final Logger logger = LoggerFactory.getLogger(StaticPagesController.class);

	@Resource
	private Flash flash;
	
	@Autowired
	@Qualifier("micropostService")
	MicropostService micropostService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@GetMapping(value = "/test")
	public String root(Locale locale, Model model, final RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		logger.info("Home Page Requested, locale = " + locale);
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("flash", "user.create.success");
		flash.info("user.create.success");
		flash.keep();
		return "redirect: " + request.getContextPath() + "/users";
	}

	@GetMapping(value = {"/", "/home"})
	public String home(@RequestParam(name = "page", required = false) Optional<Integer> page, Locale locale, Model model, Authentication authentication, HttpServletRequest request) {
		logger.info("Home Page Requested, locale = " + locale);
		if(authentication != null && authentication.isAuthenticated()) {
			CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();
			MicropostModel micropostModel = new MicropostModel();
			micropostModel.setPage(page.orElse(1));
			micropostModel.setUserId(userDetails.getUser().getId());
			Page<MicropostModel> microposts = micropostService.paginate(micropostModel);
			model.addAttribute("microposts", microposts);
			model.addAttribute("micropost", new MicropostModel());
			userDetails.getUser().setTotalMicropost(micropostService.count(micropostModel));
		}
		return "static_pages/home";
	}

	@GetMapping(value = "/help")
	public String help(Locale locale, Model model) {
		logger.info("Home Page Requested, locale = " + locale);
		return "static_pages/help";
	}

	@GetMapping(value = "/about")
	public String about(Locale locale, Model model) {
		logger.info("Home Page Requested, locale = " + locale);
		return "static_pages/about";
	}

	@GetMapping(value = "/contact")
	public String contact(Locale locale, Model model) {
		logger.info("Home Page Requested, locale = " + locale);
		return "static_pages/contact";
	}

	@GetMapping(value = { "/access_denied" })
	public String accessDenied() {
		logger.info("Access denied");
		return "access_denied";
	}
}
