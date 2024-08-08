package com.spring.sample.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class User extends BaseEntity implements Serializable {

	private Integer id;
	private String name;
	private String email;
	private String password;
	private String series;
	private String token;
	private Date lastUsed;
	private Integer role;

	private List<Micropost> microposts;

	public User() {

	}

	public User(Integer id) {
		this.id = id;
	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public List<Micropost> getMicroposts() {
		return microposts;
	}

	public void setMicroposts(List<Micropost> microposts) {
		this.microposts = microposts;
	}

}
