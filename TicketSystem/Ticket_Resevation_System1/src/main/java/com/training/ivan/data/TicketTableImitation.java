package com.training.ivan.data;

import java.util.ArrayList;

import com.training.ivan.Ticket;

/**
 * 
 * @author ivaniv Static data living in computer memory This class imitates the
 *         use of a database It stores the ticket data making it permanent and
 *         available to all classes which need this data
 */
public class TicketTableImitation {

	private static final int NUMBER_OF_TICKETS = 8;
	public static volatile ArrayList<Ticket> tickets;

	/**
	 * Initializes tickets according to the ticket number
	 */
	public static void init() {
		if (tickets == null) {
			tickets = new ArrayList<Ticket>(NUMBER_OF_TICKETS);
			for (int i = 0; i < NUMBER_OF_TICKETS; i++) {
				tickets.add(new Ticket(i, null));
			}
		}
	}

	/**
	 * forces new initialization of the tickets and initializes them again.In
	 * production this method is meaningless, but we use it for exercise
	 * purposes
	 */
	public static void clear() {
		tickets = null;
		init();
	}

}