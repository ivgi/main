package com.training.ivan;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "t_tickets")
public class Ticket {
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	@ManyToOne
	private User user;
	
	public Ticket(){
		
	}
	
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
