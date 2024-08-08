package com.spring.sample.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Flash {

	private static final String INFO = "info";
	private static final String SUCCESS = "success";
	private static final String ERROR = "error";
	private static final String WARNING = "warning";
	private static final String NOTICE = "notice";

	private final Map<String, String> msgs = new HashMap<String, String>();

	private boolean isKept; // keep msg for one more request (when the controller method redirects to
							// another)
	
	public Flash() {
		
	}

	private void message(String severity, String message) {
		msgs.put(message, severity);
	}

	public void info(String message) {
		this.message(INFO, message);
	}

	public void success(String message) {
		this.message(SUCCESS, message);
	}

	public void notice(String message) {
		this.message(NOTICE, message);
	}

	public void warning(String message) {
		this.message(WARNING, message);
	}

	public void error(String message) {
		this.message(ERROR, message);
	}

	public boolean isEmptyMessage() {
		return msgs.isEmpty();
	}

	public void clear() {
		msgs.clear();
		isKept = false;
	}

	public Map<String, String> getMessage() {
		return msgs;
	}

	public boolean isKept() {
		return isKept;
	}

	public void keep() {
		isKept = true;
	}

	public void unKeep() {
		isKept = false;
	}
}
