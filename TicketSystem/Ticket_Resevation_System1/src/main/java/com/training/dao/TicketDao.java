package com.training.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.ivan.Ticket;
import com.training.ivan.TicketSystemConfig;
import com.training.ivan.User;
import com.training.ivan.data.DataBaseUtil;
import com.training.ivan.data.TicketTableImitation;

/**
 * Class responsible for data manipulation of List&ltTicket&gt collection This
 * class also synchronizes reading and writing from ticket collections
 * 
 * @author ivaniv
 * 
 */
public class TicketDao {

	private static final Logger logger = LoggerFactory
			.getLogger(TicketDao.class);

	private static final Lock lock = new ReentrantLock(true); // with fairness
																// policy

	/**
	 * Retrieves all available tickets from ticket table and adds them to a list
	 * 
	 * @return list of tickets
	 */
	private static ArrayList<Ticket> getTicketsFromDB() {

		Connection con = DataBaseUtil.getDatabaseConnection();

		if (con == null)
			return null;

		Statement stmt = null;
		String query = "SELECT id, userId FROM ticket ORDER BY ID ASC";
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();

		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Integer ticketId = rs.getInt("id");
				Integer userId = rs.getInt("userId");
				User user = UserDao.getUserById(userId);
				//logger.debug("ticket" + ticketId + "- user: " + user);
				tickets.add(new Ticket(ticketId, user));
			}
		} catch (SQLException e) {
			logger.error("Getting tickets failed", e);
		} finally {
			if (stmt != null)
				try {
					stmt.close();
					con.close();
				} catch (SQLException e) {
					logger.error("Database access errors on close method", e);
				}
		}
		return tickets;
	}

	/**
	 * According to USE_DATABASE option, this method gets tickets from the
	 * database. If USE_DATABASE option is disabled inMemory data is used
	 * 
	 * @return list of all tickets
	 */
	public static ArrayList<Ticket> getTickets() {

		if (TicketSystemConfig.USE_DATABASE) {

			ArrayList<Ticket> tickets = getTicketsFromDB();

			if (tickets == null) {
				logger.info("PROBLEM WITH THE DATABASE, USING THE INNER MEMORY INSTEAD!");
				TicketSystemConfig.USE_DATABASE = false;

				// recursively calling the method
				// will return inMemory tickets this time
				return getTickets();
			}
			return tickets;
		} else {
			logger.info("Using inMemory data");
			return TicketTableImitation.tickets;
		}

	}

	/**
	 * Finds a username by specified ticket id. Uses the User class. This method
	 * is synchronized, i.e. only one thread at a time can iterate and read from
	 * tickets
	 * 
	 * @param id
	 *            - number of the ticket
	 * @return - associated user name
	 */
	public static String getUsernameByTicketId(Integer id) {
		lock.lock();
		try {
			logger.debug("Tickets locked due to reading operation. (Lock on TicketDao acquired)");
			Ticket ticket = getTicketById(id);
			if (ticket != null)
				return ticket.getUser() == null ? null : ticket.getUser()
						.getUsername();
		} finally {
			lock.unlock();
			logger.debug("Tickets unlocked, reading operation finished.(Lock on TicketDao released)");
		}

		return null;
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
	public static void setUsernameByTicketId(Integer id, String username) {
		lock.lock();
		try {
			logger.debug("Tickets locked due to writing operation. (Lock on TicketDao acquired by "
					+ username + "'s thread)");

			// a reference to a ticket (can be ticket taken from the database or
			// ticket in the memory). Here ticket can't be null. If ticket is
			// null - logical error and system must stop
			Ticket ticket = getTicketById(id);
			User user = new User(username);

			if (TicketSystemConfig.USE_DATABASE) {

				// if no user exists, then create user
				// get assigned id of the user
				// associate the assigned id with a ticket
				if (ticket.getUser() == null) {
					UserDao.addUser(user);
					UserDao.getLastInsertedUserId();
					TicketDao.setUserId(id, UserDao.getLastInsertedUserId()); // associate
				} else {
					//if the ticket is associated with a user then change the name of the user
					user.setUserId(ticket.getUser().getUserId());
					UserDao.addOrUpdateUser(user);
					
				} 
			}
			ticket.setUser(user);

		} finally {
			lock.unlock();
			logger.debug("Tickets unlocked, writing operation finished.(Lock on TicketDao released by "
					+ username + "'s thread)");
		}
	}

	/**
	 * Makes a copy of the arraylist. While reading - the data is locked. A copy
	 * of the arraylist is needed for synchronization reasons. We need to be
	 * sure that the read data are correct.
	 * 
	 * @return - returns a copy of the tickets arrayList
	 */
	public static List<Ticket> readTickets() {
		lock.lock();
		try {
			logger.debug("Tickets locked for copy read operation");
			@SuppressWarnings("unchecked")
			ArrayList<Ticket> tickets = (ArrayList<Ticket>) (TicketTableImitation.tickets)
					.clone();
			logger.debug("Ticket data copied");
			return tickets;
		} finally {
			lock.unlock();
			logger.debug("Copy read operation ended, lock released");
		}
	}

	/**
	 * Sets the userId of the ticket. This operation is done directly in the
	 * database. UserId must not be -1, if the userId is -1 SQL Exception will
	 * be logged and the method will return gracefully
	 * 
	 * @param ticketId
	 */
	private static void setUserId(Integer ticketId, Integer userId) {
		Connection con = DataBaseUtil.getDatabaseConnection();
		if (con == null) {
			logger.error("Connection problem, updating userId failed");
			return;
		}
		String updateQuery = "UPDATE ticket SET userId = ? WHERE id = ?";
		logger.debug("Update query: " + "\"UPDATE ticket SET userId = " + userId + " WHERE id = " + ticketId +"\"" );
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement(updateQuery);
			stm.setInt(1, userId);
			stm.setInt(2, ticketId);
			stm.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.toString());
		} finally {
			if (stm != null)
				try {
					stm.close();
					con.close();
				} catch (SQLException e) {
					logger.error("Database access errors on close method", e);
				}
		}
	}

	/**
	 * Traverses a collection of tickets and returns a ticket associated with id
	 * 
	 * @param ticketId
	 *            - the ticketId we search
	 * @return -
	 */

	private static Ticket getTicketById(Integer ticketId) {

		List<Ticket> tickets = getTickets();
		if (tickets != null) {
			ListIterator<Ticket> iter = tickets.listIterator();
			while (iter.hasNext()) {
				Ticket ticket = (Ticket)iter.next();
				if (ticketId == ticket.getId()) {
					return ticket;
				}
			}
			logger.debug("ticket id:  " + ticketId + " out of scope ");
		} else
			logger.debug("NullPointer on tickets collection");

		return null;
	}
}
