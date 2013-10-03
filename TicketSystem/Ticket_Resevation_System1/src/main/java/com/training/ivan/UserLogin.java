package com.training.ivan;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.model.User;

@ManagedBean(name="login")
@SessionScoped
public class UserLogin {
	private static final Logger logger = LoggerFactory.getLogger(UserLogin.class);
	private User user;
	private boolean wrongUser;

	public boolean isWrongUser() {
		return wrongUser;
	}

	public void setWrongUser(boolean wrongUser) {
		this.wrongUser = wrongUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@PostConstruct
	public void init(){
		user = new User();
		setWrongUser(false);
	}
	
	/**
	 *  Logs the user in the system
	 * @return String representing the page location
	 */
	
	public String login(){
		setWrongUser(false);
		if(user.getUsername() == null && user.getUsername().isEmpty()){
			setWrongUser(true);
			user.setUsername(null);
			logger.debug("Wrong username");
			return "userLogin.xhtml";
		}
		else{
			logger.info("Logged in as user: " + user.getUsername());
			return "reservations.xhtml";
		}
	}
	
	/**
	 *  Logs the user out of the system
	 * @return String representing the page location
	 */
	public String logout(){
		logger.info("User \"" + user.getUsername() + "\" logged out");
		user.setUsername(null);
		return "userLogin.xhtml";
	}
}
