package com.training.ivan.test;

import junit.framework.TestCase;

import com.training.ivan.data.DatabaseListener;
import com.training.ivan.data.TicketTableImitation;
import com.training.ivan.webservices.TicketReservationService;

public class TicketReservationServiceTest extends TestCase {
	
	TicketReservationService service; 
	DatabaseListener dl;
	
	protected void setUp(){

		dl = new DatabaseListener();
		dl.contextInitialized(null); // no servlet context just dummy for initializing db
		service = new TicketReservationService();
	}
	
	public void testTakePlaceWebService(){
		
		// Set the expected output
		StringBuilder builder = new StringBuilder();

		// test success
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<status>");
		builder.append("successful reservation of ticket: 0").append("</status>");
		assertEquals(builder.toString(), service.takePlace("0", "Ivan"));
		
		// test ticket out of scope
		builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<status>");
		builder.append("There is no ticket with id: 20").append("</status>");
		assertEquals(builder.toString(), service.takePlace("20", "Ivan"));
		
		//test decline
		builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<status>");
		builder.append("ticket 0 is reserved by another user").append("</status>");
		assertEquals(builder.toString(), service.takePlace("0", "Ivan"));
		
	}
	
	protected void tearDown(){
		dl.contextDestroyed(null); // no servlet context used just dummy for clearing db
	}
	
	
	

}
