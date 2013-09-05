package com.training.ivan;

import com.training.ivan.User;

public class Ticket {
	
	private int id;
	private User user;
	
	public Ticket(int id, User user){
		this.id = id;
		this.user = user;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User username) {
		this.user = username;
	}

}
