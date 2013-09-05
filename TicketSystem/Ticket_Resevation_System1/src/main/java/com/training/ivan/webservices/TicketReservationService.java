package com.training.ivan.webservices;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.dao.TicketDao;
import com.training.ivan.Ticket;
import com.training.ivan.data.TicketTableImitation;

/**
 * @author ivaniv
 * This class exposes web services for reservation of tickets
 */
@Path("tickets")
public class TicketReservationService {
	
	private static final Logger logger = LoggerFactory.getLogger(TicketReservationService.class);

	public TicketReservationService(){
		
	}
	
	//TODO
	/**
	 * Exposed web service for getting available tickets
	 * @return xml representation of available tickets
	 */
	@GET
	@Produces("text/xml")
	public String getAvailableTicketNumbers(){
		TicketTableImitation.init();
		StringBuilder builder = new StringBuilder();
		List<Ticket> tickets = TicketDao.readTickets(); //get data from "database"
		
		//build the xml document
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<tickets>");
			for(Ticket t:tickets){
				builder.append("<ticket-number>").append(t.getId()).append("</ticket-number>");
				builder.append("<ticket-user>").append(t.getUser() == null ? "empty" : t.getUser().getUsername());
				builder.append("</ticket-user>");
			}
			builder.append("</tickets>");
			logger.info("service for listing tickets is called");
			
			return builder.toString();
		}
	}
	
	
