package com.training.ivan;

public class User {
	
	private String username;
	private int userId;
	
	public User(){
		this.username = null;
		this.userId = 0;
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
