package com.training.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table ( name = "t_users")
public class User {
	
	@Column(name="username", length = 40)
	private String username;
	
	@Id
	@GeneratedValue
	private int userId;
	
	public User(){
	}
	
	public User(String username) {
		this.username = username;
	}

	public User(String username, int userId) {
		this.username = username;
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
