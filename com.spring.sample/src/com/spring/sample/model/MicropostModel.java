package com.spring.sample.model;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class MicropostModel extends BaseModel {
	private Integer id;
	private Integer userId;
	@NotEmpty(message = "{micropost.validation.content.required}")
	@Size(max = 128, message = "{micropost.validation.content.length}")
	private String content;
	private Date createdAt;

	private UserModel user;

	public MicropostModel() {
//		Sort.by("price").descending()
	}

	public MicropostModel(Integer id) {
		this.id = id;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

}
