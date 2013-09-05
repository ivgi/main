package com.training.dao;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.ivan.Ticket;
import com.training.ivan.User;
/**
 * Class responsible for data manipulation of List&ltTicket&gt collection
 * This class also synchronizes reading and writing from ticket collections
 * @author ivaniv
 *
 */
public class TicketDao {

	private static final Logger logger = LoggerFactory
			.getLogger(TicketDao.class);
	
	private static final Lock lock = new ReentrantLock(true); // with fairness policy

	/**
	 * Finds a username by specified ticket id. Uses the User class. This method
	 * is synchronized, i.e. only one thread at a time can iterate and read from
	 * tickets
	 * 
	 * @param id
	 *            - number of the ticket
	 * @param tickets
	 *            - the tickets collection which we will iterate on
	 * @return - associated user name
	 */
	public static String getUsernameByTicketId(Integer id, List<Ticket> tickets) {
		lock.lock();
		logger.debug("Tickets locked due to reading operation. (Lock on TicketDao acquired)");
		try{
		if (tickets != null) {
			Ticket ticket;
			Iterator<Ticket> iter = tickets.iterator();
			while (iter.hasNext()) {
				ticket = iter.next();
				if (id == ticket.getId())
					return ticket.getUser() == null ? null : ticket.getUser()
							.getUsername();
			}
			return null;
		} else {
			logger.debug("NullPointer on tickets collection");
			return null;
		}
		}
		finally{
			lock.unlock();
			logger.debug("Tickets unlocked, reading operation finished.(Lock on TicketDao released)");
		}
	}

	/**
	 * Updates the username of the specified ticket.This method is synchronized,
	 * i.e. only one thread at a time can iterate and read from tickets
	 * 
	 * @param id
	 *            - number of the ticket
	 * @param tickets
	 *            - the tickets collection which will be updated
	 */
	public static void setUsernameByTicketId(Integer id, List<Ticket> tickets, String username) {
		lock.lock();
		logger.debug("Tickets locked due to writing operation. (Lock on TicketDao acquired by "+ username +"'s thread)");
		try{
		if (tickets != null) {
			Ticket ticket;
			ListIterator<Ticket> iter = tickets.listIterator();
			while (iter.hasNext()) {
				ticket = (Ticket) iter.next();
				if (id == ticket.getId()) {
					User user = new User();
					user.setUsername(username);
					iter.set(new Ticket(id, user));
				}
			}
		} else
			logger.debug("NullPointer on tickets collection");
	}
	finally{
		lock.unlock();
		logger.debug("Tickets unlocked, writing operation finished.(Lock on TicketDao released by "+ username +"'s thread)");
	}
}
}