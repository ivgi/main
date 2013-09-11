package com.training.dao;

import java.sql.Connection;
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

	private static final Logger logger = LoggerFactory.getLogger(TicketDao.class);

	private static final Lock lock = new ReentrantLock(true); // with fairness policy
	
	
	
private static ArrayList<Ticket> getTicketsFromDB(){
	
	Connection con = DataBaseUtil.getDatabaseConnection();
	
	if(con == null)
	return null;
	
	Statement stmt = null;
	String query = "SELECT id, userId FROM ticket";
	ArrayList<Ticket> tickets = new ArrayList<Ticket>();
	
	try {
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			Integer ticketId = 	rs.getInt("id");
			Integer userId = rs.getInt("userId");
			User user = UserDao.getUserById(userId);
			tickets.add(new Ticket(ticketId,user));
		}
	} catch (SQLException e) {
		logger.error("Getting tickets failed",e);
	}finally{
		if(stmt!=null)
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				logger.error("Database access errors on close method",e);
			}
	}
	return tickets;
}

/**
 *  According to USE_DATABASE option, this method gets tickets from the database
 *  If USE_DATABASE option is disabled inMemory data is used
 * @return list of all tickets
 */
	public static ArrayList<Ticket> getTickets(){
		
		if(TicketSystemConfig.USE_DATABASE){
			
			ArrayList<Ticket> tickets = getTicketsFromDB();

			if(tickets == null){
				logger.info("PROBLEM WITH THE DATABASE, USING THE INNER MEMORY INSTEAD!");
				TicketSystemConfig.USE_DATABASE = false;
				
				// recursively calling the method
				// will return inMemory tickets this time
				return getTickets(); 
			}
			logger.info("Using data from database");
			return tickets;
		}
		else{
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
	 * @param tickets
	 *            - the tickets collection which we will iterate on
	 * @return - associated user name
	 */
	public static String getUsernameByTicketId(Integer id) {
		lock.lock();
		try {
			logger.debug("Tickets locked due to reading operation. (Lock on TicketDao acquired)");
			List<Ticket> tickets = getTickets();
			if (tickets != null) {
				Ticket ticket;
				Iterator<Ticket> iter = tickets.iterator();
				while (iter.hasNext()) {
					ticket = iter.next();
					if (id == ticket.getId())
						return ticket.getUser() == null ? null : ticket.getUser().getUsername();
				}
				logger.debug("ticket id:  " + id + " out of scope ");
				return null;
			} else {
				logger.error("NullPointer on tickets collection");
				return null;
			}
		} finally {
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
	public static void setUsernameByTicketId(Integer id, String username) {
		lock.lock();
		try {
			logger.debug("Tickets locked due to writing operation. (Lock on TicketDao acquired by "
					+ username + "'s thread)");
			List<Ticket> tickets = getTickets();
			if (tickets != null) {
				Ticket ticket;
				ListIterator<Ticket> iter = tickets.listIterator();
				while (iter.hasNext()) {
					ticket = (Ticket) iter.next();
					if (id == ticket.getId()) {
						User user = new User(username,0); //TODO make the one to many connection user-ticket
						user.setUsername(username); //TODO write user in db
						iter.set(new Ticket(id, user));
					}
				}
			} else
				logger.debug("NullPointer on tickets collection");
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
		try{
		logger.debug("Tickets locked for copy read operation");
		ArrayList<Ticket> tickets = (ArrayList<Ticket>)(TicketTableImitation.tickets).clone();
		logger.debug("Ticket data copied");
		return tickets;
		}finally{
			lock.unlock();
			logger.debug("Copy read operation ended, lock released");
		}
	}
}
