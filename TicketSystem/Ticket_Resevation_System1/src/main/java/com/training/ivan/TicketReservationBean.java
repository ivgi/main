package com.training.ivan;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ivaniv Ticket reservation management bean
 */

@ManagedBean(name = "reservations")
@SessionScoped
public class TicketReservationBean {

	private static final Logger logger = LoggerFactory.getLogger(TicketReservationBean.class);
	private static final int NUMBER_OF_TICKETS = 8;
	@ManagedProperty(value="#{login}")
	private UserLogin login;

	private int selectedTicket;
	private Integer ticketRequested;

	private static volatile HashMap<Integer, String> tickets;

	public void setTicketRequested(Integer ticketRequested) {
		this.ticketRequested = ticketRequested;
	}

	public HashMap<Integer, String> getTickets() {
		return tickets;
	}

	public int getSelectedTicket() {
		return selectedTicket;
	}

	public void setSelectedTicket(int selectedTicket) {
		this.selectedTicket = selectedTicket;
	}
	
	public void setLogin(UserLogin login) {
		this.login = login;
	}

	@PostConstruct
	public void init() {
		if (tickets == null) {
			tickets = new HashMap<Integer, String>();
			for (int i = 0; i < NUMBER_OF_TICKETS; i++) {
				tickets.put(i, null);
			}
		}
		selectedTicket = -1;
		ticketRequested = -1;

		logger.info("Initializing...");
	}

	public void clear() {
		logger.info("Clearing data ...");
		tickets = new HashMap<Integer, String>();
		for (int i = 0; i < NUMBER_OF_TICKETS; i++) {
			tickets.put(i, null);
		}
		logger.debug("Stopped clearing data ...");
	}

	public void deselected() {
		logger.debug("reserved set to false");
		selectedTicket = -1;
	}

	public void requestedTicket(Integer ticketId) {
		logger.debug("requested ticket is " + ticketId);
		ticketRequested = ticketId;
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
		else if (tickets.get(ticketNumber).equals(login.getUser().getUsername()))
			return "blue";
		else
			return "red";
	}

	/**
	 * Checks whether the currently clicked ticket is reserved
	 * 
	 * @return true if it is reserved, false otherwise
	 */
	public boolean isTicketReserved() {
		logger.debug("Selected ticket is: " + selectedTicket);
		if (selectedTicket == -1)
			return false;
		else if (reservationCheck(selectedTicket).equals("green")
				|| reservationCheck(selectedTicket).equals("blue"))
			return false;
		else
			return true;
	}

	/**
	 * Reserves a ticket to a user if the ticket is free
	 * 
	 * @param ticketId
	 *            - the number of the ticket
	 */
	public void reserve(Integer ticketId) {
		selectedTicket = ticketId;
		if (login.getUser().getUsername() == null || login.getUser().getUsername().isEmpty()) {
			login.getUser().setUsername(null);
		} else {
			synchronized (tickets) {
				if (tickets.get(ticketId) == null) {
					tickets.put(ticketId, login.getUser().getUsername());
					logger.info(login.getUser().getUsername() + " took slot: " + ticketId);
					ticketRequested = ticketId;
				} else
					logger.info(login.getUser().getUsername() + " was declined to take slot: "
							+ ticketId);
			}
		}
	}

	@PreDestroy
	public void sessionDestroyed() {
		if (ticketRequested != -1)
			tickets.put(ticketRequested, null);
		logger.debug("predestroy session for user: " + login.getUser().getUsername() + " called");
	}

	/**
	 * Frees a taken ticket
	 * 
	 * @param ticketId
	 */
	public synchronized void declineReservation(Integer ticketId) {
		if (tickets.get(ticketId) != null
				&& tickets.get(ticketId).equals(login.getUser().getUsername())) {
			logger.info(login.getUser().getUsername() + " freed slot: " + ticketId);
			tickets.put(ticketId, null);
			ticketRequested = -1;
		}
	}

}
