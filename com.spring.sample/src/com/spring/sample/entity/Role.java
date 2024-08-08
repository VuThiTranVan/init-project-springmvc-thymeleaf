package com.spring.sample.entity;

import java.util.ArrayList;
import java.util.List;

public enum Role {
	USER(1), ADMIN(2);

	public final int value;
	public static final int USER_AND_ADMIN_ROLES = USER.value | ADMIN.value;
	public static final int USER_ROLE = USER.value;
	public static final int ADMIN_ROLE = ADMIN.value;

	private Role(int value) {
		this.value = value;
	}

	public static List<Role> userAndAdminRoles() {
		List<Role> roles = new ArrayList<Role>();
		roles.add(USER);
		roles.add(ADMIN);
		return roles;
	}

	public static List<Role> userRoles() {
		List<Role> roles = new ArrayList<Role>();
		roles.add(USER);
		return roles;
	}

	public static List<Role> adminRoles() {
		List<Role> roles = new ArrayList<Role>();
		roles.add(ADMIN);
		return roles;
	}

	public static boolean noRole(int role) {
		return ((role & Role.USER.value) != Role.USER.value) && ((role & Role.ADMIN.value) != Role.ADMIN.value);
	}

	public static boolean hasUserRole(int role) {
		return (role & Role.USER.value) == Role.USER.value;
	}

	public static boolean hasAdminRole(int role) {
		return (role & Role.ADMIN.value) == Role.ADMIN.value;
	}
	
	public static List<Role> toList(int role) {
		List<Role> roles = new ArrayList<Role>();
		if(noRole(role)) {
			return roles;
		}
		if ((role & Role.USER.value) == Role.USER.value) {
			roles.add(Role.USER);
		}
		if ((role & Role.ADMIN.value) == Role.ADMIN.value) {
			roles.add(Role.ADMIN);
		}
		return roles;
	}

}
