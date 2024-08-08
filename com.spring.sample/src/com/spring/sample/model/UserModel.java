package com.spring.sample.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.spring.sample.validator.FieldMatch;
import com.spring.sample.validator.NullOrNotBlank;
import com.spring.sample.validator.UniqueEmail;

@FieldMatch.List({
		@FieldMatch(first = "password", second = "confirmation", message = "{user.validation.password.notmatch}") })
@UniqueEmail(name = "email", message = "{user.validation.email.exist}")
public class UserModel extends BaseModel {
	private Integer id;
	@NotEmpty(message = "{user.validation.name.required}")
	@Size(max = 64, message = "{user.validation.name.max}")
	private String name;
	@NotEmpty(message = "{user.validation.email.required}")
	@Email(message = "{pattern.email}")
	private String email;
	@NullOrNotBlank(message = "{user.validation.password.required}")
	@Size(max = 64, min = 6, message = "{user.validation.password.length}")
	private String password = null;
	@NullOrNotBlank(message = "{user.validation.confirmation.required}")
	private String confirmation = null;

	private int role;
	private Long totalMicropost = 0L;

	public UserModel() {

	}

	public UserModel(Integer id) {
		this.id = id;
	}

	public UserModel(String name, String email) {
		super();
		this.name = name;
		this.email = email;
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

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public Long getTotalMicropost() {
		return totalMicropost;
	}

	public void setTotalMicropost(Long totalMicropost) {
		this.totalMicropost = totalMicropost;
	}

}
