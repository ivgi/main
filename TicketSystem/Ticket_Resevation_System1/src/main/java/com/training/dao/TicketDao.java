package com.training.dao;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.ivan.Ticket;
import com.training.ivan.User;

public class TicketDao {
	
	private static final Logger logger = LoggerFactory.getLogger(TicketDao.class);

	/**
	 * Finds a username by specified ticket id. Uses the user
	 * 
	 * @param id - number of the ticket
	 * @param tickets - the tickets collection which we will iterate on
	 * @return - associated user name
	 */
	public static String getUsernameByTicketId(Integer id, List<Ticket> tickets) {
		if (tickets != null) {
			Ticket ticket;
			Iterator<Ticket> iter = tickets.iterator();
			while (iter.hasNext()) {
				ticket = iter.next();
				if(id == ticket.getId())
					return ticket.getUser() == null ? null : ticket.getUser().getUsername();
			}
			return null;
		}
		else{
			logger.debug("NullPointer on tickets collection");
			return null;
		}
	}
	/**
	 * Updates the username of the specified ticket
	 * @param id - number of the ticket
	 * @param tickets - the tickets collection which will be updated
	 */
	public static void setUsernameByTicketId(Integer id, List<Ticket> tickets, String username){
		if(tickets != null){
			Ticket ticket;
			ListIterator<Ticket> iter = tickets.listIterator();
			while(iter.hasNext()){
				ticket = (Ticket)iter.next();
				if(id == ticket.getId()){
					User user = new User();
					user.setUsername(username);
					iter.set(new Ticket(id,user));
				}
			}
		}
		else
			logger.debug("NullPointer on tickets collection");
		
	}

}
