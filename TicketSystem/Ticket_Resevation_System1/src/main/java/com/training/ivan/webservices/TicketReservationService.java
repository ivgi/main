package com.training.ivan.webservices;

import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.training.dao.TicketDao;
import com.training.ivan.TicketSystemConfig;
import com.training.ivan.data.TicketTableImitation;
import com.training.model.Ticket;

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
		List<Ticket> tickets = TicketDao.getTickets();

		logger.info("service for listing tickets is called");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			Document xmlObject = docBuilder.newDocument();
			Element rootElement = xmlObject.createElement("tickets");
			xmlObject.appendChild(rootElement);
			Element ticketNumber;
			Element ticketUser;
			for (Ticket t : tickets) {
				ticketNumber = xmlObject.createElement("ticket-number");
				ticketUser = xmlObject.createElement("ticket-user");
				rootElement.appendChild(ticketNumber);
				ticketNumber.appendChild(xmlObject.createTextNode(String.valueOf(t.getId())));
				String username = t.getUser() == null ? "empty" : t.getUser().getUsername();
				rootElement.appendChild(ticketUser);
				ticketUser.appendChild(xmlObject.createTextNode(username));
			}
			
			// create a string from the document and send it
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(xmlObject), new StreamResult(writer));
			return writer.getBuffer().toString();
			
		} catch (ParserConfigurationException e) {
			logger.error("Error creating xml",e);
		} catch (TransformerConfigurationException e) {
			logger.error("Transforming xml document failed", e);
		} catch (TransformerException e) {
			logger.error("Transforming xml document failed", e);
		}
		return "<error></error>";
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
	public String takePlace(@FormParam("ticketId") String ticketId,
			@FormParam("username") String username) {
		String response = takePlaceUsingDomObject(ticketId, username);
		return  response != null ? response : takePlaceUsingStringBuilder(ticketId, username);
	}
	
	/**
	 * This method uses java.lang.StringBuilder
	 * @param ticketId - post parameter of the ticketId expected to be reserved
	 * @param username - post parameter of the user associated with the ticket
	 * @return response formed as xml document
	 */
	private String takePlaceUsingStringBuilder(String ticketId, String username){

		Integer ticketNum; // store the value of ticketId here
		StringBuilder builder = new StringBuilder();

		// build the xml document
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>").append(
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
	
	/**
	 * This method uses javax.xml package
	 * @param ticketId - post parameter of the ticketId expected to be reserved
	 * @param username - post parameter of the user associated with the ticket
	 * @return response formed as xml document
	 */
	private String takePlaceUsingDomObject(String ticketId, String username) {

		Integer ticketNum; // store the value of ticketId here

		try {
			ticketNum = Integer.parseInt(ticketId);
			logger.debug("Parsing the posted ticket number");
		} catch (NumberFormatException nfe) {
			ticketNum = null;
			logger.error("Non numeric value for ticketId entered by service client");
			logger.error("The ticket would not be registered");
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder;
		try {
			builder = dbf.newDocumentBuilder();
			Document xmlObject = builder.newDocument();
			Element rootElement = xmlObject.createElement("status");
			xmlObject.appendChild(rootElement);
			Text status = xmlObject.createTextNode("");
			rootElement.appendChild(status);
			
			// create a transformer to transform the dom document object into java.lang.String
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			StringWriter writer = new StringWriter();

			if (ticketNum != null) {

				if (ticketNum >= TicketSystemConfig.NUMBER_OF_TICKETS) {
					logger.info("The client provided 'out of scope' ticketId");
					status.setNodeValue("There is no ticket with id: " + ticketId);
					transformer.transform(new DOMSource(xmlObject), new StreamResult(writer));
					return writer.getBuffer().toString();
				}
				
				if (TicketDao.getUsernameByTicketId(ticketNum) == null) {
					// the ticket is free, so we can safely reserve it
					TicketDao.setUsernameByTicketId(ticketNum, username);
					status.setNodeValue("successful reservation of ticket: " + ticketId);
					transformer.transform(new DOMSource(xmlObject), new StreamResult(writer));
					return writer.getBuffer().toString();
				} else{
					// the ticket is taken, we notify the user
					status.setNodeValue("ticket " + ticketId + " is reserved by another user");
					transformer.transform(new DOMSource(xmlObject), new StreamResult(writer));
					return writer.getBuffer().toString();
				}

			} else{
				logger.debug("TicketNum is null");
				status.setNodeValue("wrong ticket number");
				transformer.transform(new DOMSource(xmlObject), new StreamResult(writer));
				return writer.getBuffer().toString();
			}
		} catch (ParserConfigurationException e) {
			logger.error("Can not create xml document", e);
		} catch (TransformerConfigurationException e) {
			logger.error("Transforming xml document failed", e);
		} catch (TransformerException e) {
			logger.error("Transforming xml document failed", e);
		}
		
		return null;

	}

}
