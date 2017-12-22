package com.flowable.common.utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class LoginUser implements Serializable{

	private static final long serialVersionUID = 1L;

	private String username;
	
	private String name;
	
	private Set<String> roles = new HashSet<String>();
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}
