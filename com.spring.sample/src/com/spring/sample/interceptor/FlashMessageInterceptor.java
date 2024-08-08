package com.spring.sample.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class FlashMessageInterceptor extends HandlerInterceptorAdapter {
	
	@Resource
	Flash flash;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (!flash.isKept() && modelAndView != null) {
			modelAndView.addObject("flash", flash);
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (flash.isKept()) {
			flash.unKeep();
		} else {
			flash.clear();
		}
	}
}
