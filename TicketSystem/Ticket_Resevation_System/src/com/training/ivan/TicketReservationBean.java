package com.training.ivan;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ivaniv
 * Ticket reservation management bean
 */

@ManagedBean(name = "reservations", eager = true)
@ApplicationScoped
public class TicketReservationBean {

	String username;
	boolean isReserved;
	HashMap<Integer, String> tickets;
	Logger logger;

	public boolean isReserved() {
		return isReserved;
	}

	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@PostConstruct
	public void init() {
		logger = LoggerFactory.getLogger(TicketReservationBean.class);
		logger.debug("Clearing data ...");
		tickets = new HashMap<Integer, String>();
		for (int i = 0; i < 5; i++) {
			tickets.put(i, null);
		}
		username = null;
		isReserved = false;
		logger.debug("Stopped clearing data ...");
		logger.info("Initializing...");
	}

	public void usernameToNull() {
		logger.debug("username set to null");
		username = null;
	}

	public void reservedToFalse() {
		logger.debug("reserved set to false");
		isReserved = false;
	}

	/**
	 * Checks whether a ticket is reserved by the current user, free or reserved
	 * by another user
	 * 
	 * @param ticketNumber
	 *            the number of the ticket
	 * @return the corresponding css class
	 */
	public String reservationCheck(Integer ticketNumber) {
		logger.debug("Reservation check on: " + ticketNumber
				+ " returned color ");
		if (tickets.get(ticketNumber) == null)
			return "green";
		else if (tickets.get(ticketNumber).equals(username))
			return "blue";
		else
			return "red";
	}

	/**
	 * Reserves a ticket to a user if the ticket is free
	 * 
	 * @param ticketId
	 *            - the number of the ticket
	 */
	public void reserve(Integer ticketId) {
		if (getUsername() == null || getUsername().isEmpty()) {
			setUsername(null);
		} else {
			if (tickets.get(ticketId) == null) {
				tickets.put(ticketId, username);
				logger.info(username + " took slot: " + ticketId);
			} else {
				isReserved = true;
				logger.info(username + " was declined to take slot: "
						+ ticketId);
			}
		}
	}

	/**
	 * Frees a taken ticket
	 * 
	 * @param ticketId
	 */
	public void declineReservation(Integer ticketId) {
		if (tickets.get(ticketId) != null
				&& tickets.get(ticketId).equals(username)) {
			logger.info(username + " freed slot: " + ticketId);
			tickets.put(ticketId, null);
			isReserved = false;
		}
	}
}
