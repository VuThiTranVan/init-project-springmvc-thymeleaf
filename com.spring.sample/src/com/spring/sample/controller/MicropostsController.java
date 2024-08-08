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
import org.springframework.security.core.Authentication;
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
import com.spring.sample.model.CustomUserDetails;
import com.spring.sample.model.MicropostModel;
import com.spring.sample.service.MicropostService;

@Controller
@EnableWebMvc
public class MicropostsController {
	private static final Logger logger = LoggerFactory.getLogger(MicropostsController.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	MicropostService micropostService;

	@Resource
	Flash flash;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	}

	@GetMapping(value = "/microposts")
	public String index(@RequestParam(name = "page", required = false) Optional<Integer> page, Locale locale,
			Model model, HttpServletRequest request) {
		MicropostModel micropostModel = new MicropostModel();
		micropostModel.setPage(page.orElse(1));
		Page<MicropostModel> microposts = micropostService.paginate(micropostModel);
		model.addAttribute("microposts", microposts);
		return "microposts/index";
	}

	@GetMapping(value = { "/microposts/add" })
	public String add(Locale locale, Model model) {
		model.addAttribute("micropost", new MicropostModel());
		return "microposts/add";
	}

	@PostMapping(value = "/microposts")
	public String create(@ModelAttribute("micropost") @Validated MicropostModel micropostModel,
			BindingResult bindingResult, Model model, final RedirectAttributes redirectAttributes,
			Authentication authentication, HttpServletRequest request) throws Exception {
		if (bindingResult.hasErrors()) {
			logger.info("Returning add micropost page, validate failed");
			if (authentication != null && authentication.isAuthenticated()) {
				CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
				MicropostModel condition = new MicropostModel();
				condition.setPage(1);
				condition.setUserId(userDetails.getUser().getId());
				Page<MicropostModel> microposts = micropostService.paginate(condition);
				model.addAttribute("microposts", microposts);
			}
			return "static_pages/home";
		}
		if (authentication != null && authentication.isAuthenticated()) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			micropostModel.setUserId(userDetails.getUser().getId());
		}
		micropostService.addMicropost(micropostModel);
		// Add message to flash scope
		flash.success("micropost.create.success");
		flash.keep();
		return "redirect: " + request.getContextPath() + "/home";
	}

	@GetMapping(value = "/microposts/{id}")
	public String show(@PathVariable Integer id, Model model) {
		model.addAttribute("micropost", micropostService.findMicropost(id));
		return "microposts/show";
	}

	@GetMapping(value = "/microposts/{id}/edit")
	public String edit(@PathVariable Integer id, Model model) {
		model.addAttribute("micropost", micropostService.findMicropost(id));
		return "microposts/edit";
	}

	@PutMapping(value = "/microposts/{id}")
	public String update(@ModelAttribute("micropost") @Validated MicropostModel micropostModel,
			BindingResult bindingResult, Model model, final RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws Exception {
		if (bindingResult.hasErrors()) {
			logger.info("Returning edit.jsp page, validate failed");
			return "microposts/edit";
		}
		MicropostModel micropost = micropostService.editMicropost(micropostModel);
		// Add message to flash scope
		flash.success("micropost.update.success");
		flash.keep();
		return "redirect: " + request.getContextPath() + "/microposts/" + micropost.getId();
	}

	@DeleteMapping(value = "/microposts/{id}", produces = { MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<String> destroy(@PathVariable Integer id, Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Deleting micropost: " + id);
		micropostService.deleteMicropost(new MicropostModel(id));
		String contectType = request.getContentType();
		if (contectType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
			return new ResponseEntity<String>("{\"result\" : \"OK\", \"id\" : " + id + ", \"model\" : \"micropost\"}",
					HttpStatus.OK);
		} else {
			response.sendRedirect(request.getContextPath() + "/home");
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

}
