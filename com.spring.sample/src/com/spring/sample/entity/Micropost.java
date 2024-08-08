package com.spring.sample.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Micropost extends BaseEntity implements Serializable {

	private Integer id;
	private Integer userId;
	private String content;

	public User user;

	public Micropost() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
