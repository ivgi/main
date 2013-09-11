package com.training.ivan.webservices;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.dao.TicketDao;
import com.training.ivan.Ticket;
import com.training.ivan.TicketSystemConfig;
import com.training.ivan.data.TicketTableImitation;

/**
 * @author ivaniv This class exposes web services for reservation of tickets
 */
@Path("tickets")
public class TicketReservationService {

	private static final Logger logger = LoggerFactory
			.getLogger(TicketReservationService.class);

	public TicketReservationService() {

	}

	/**
	 * Exposed web service for getting available tickets
	 * 
	 * @return xml representation of available tickets
	 */
	@GET
	@Produces("text/xml")
	public String getAvailableTicketNumbers() {
		TicketTableImitation.init();
		StringBuilder builder = new StringBuilder();
		List<Ticket> tickets = TicketDao.getTickets(); 

		// build the xml document
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(
				"<tickets>");
		for (Ticket t : tickets) {
			builder.append("<ticket-number>").append(t.getId())
					.append("</ticket-number>");
			builder.append("<ticket-user>").append(
					t.getUser() == null ? "empty" : t.getUser().getUsername());
			builder.append("</ticket-user>");
		}
		builder.append("</tickets>");
		logger.info("service for listing tickets is called");

		return builder.toString();
	}

	/**
	 * Exposed web service for reserving a place
	 * 
	 * @param ticketId
	 *            - post parameter of the ticketId expected to be reserved
	 * @param username
	 *            - post parameter of the user associated with the ticket
	 * @return - response formed as xml document
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("text/xml")
	public String takePlace(@FormParam("ticketId") String ticketId, @FormParam("username") String username) {

		Integer ticketNum; // store the value of ticketId here
		StringBuilder builder = new StringBuilder();
		TicketTableImitation.init();

		// build the xml document
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(
				"<status>");

		try {
			ticketNum = Integer.parseInt(ticketId);
			logger.debug("Parsing the posted ticket number");
		} catch (NumberFormatException nfe) {
			ticketNum = null;
			logger.error("Non numeric value for ticketId entered by service client");
			logger.error("The ticket would not be registered");
		}

		if (ticketNum != null) {

			if (ticketNum >= TicketSystemConfig.NUMBER_OF_TICKETS) {
				logger.info("The client provided 'out of scope' ticketId");
				builder.append("There is no ticket with id: " + ticketNum)
						.append("</status>");
				return builder.toString();
			}

			if (TicketDao.getUsernameByTicketId(ticketNum) == null) {
				// the ticket is free, so we can safely reserve it
				TicketDao.setUsernameByTicketId(ticketNum, username);
				builder.append("successful reservation of ticket: " + ticketNum)
						.append("</status>");
				return builder.toString();
			} else {
				// the ticket is taken, we notify the user
				builder.append(
						"ticket " + ticketNum + " is reserved by another user")
						.append("</status>");
				return builder.toString();
			}

		} else {
			logger.debug("TicketNum is null");
			builder.append("wrong ticket number").append("</status>");
			return builder.toString();
		}
	}

}
