package com.training.ivan.test;

import java.util.List;

import junit.framework.TestCase;

import com.training.dao.TicketDao;
import com.training.ivan.Ticket;
import com.training.ivan.User;
import com.training.ivan.data.TicketTableImitation;

public class TicketDaoTest extends TestCase {
	
	private User user;
	List<Ticket> tickets;
	
	public TicketDaoTest(String testName){
		super(testName);
	}
	
	/**
	 * Add data to the tickets
	 */
	protected void setUp() throws Exception {
		
		TicketTableImitation.init();
		TicketTableImitation.clear(); // make sure the ticket data is cleared
		tickets = TicketTableImitation.tickets; //TODO real db
		
		//add user ivan
		TicketDao.setUsernameByTicketId(1, "Ivan");
		
		//add user dragan
		TicketDao.setUsernameByTicketId(0, "Dragan");
		
		//add user tarzan
		TicketDao.setUsernameByTicketId(2, "Tarzan");
		
		//add null user
		TicketDao.setUsernameByTicketId(3, null);
		
	}
	
	public void testGetUsernameByTicketId(){
		assertEquals("Tarzan", TicketDao.getUsernameByTicketId(2));
		assertEquals(null,TicketDao.getUsernameByTicketId(3));
	}
	
	public void testSetUsernameByTicketId(){
		TicketDao.setUsernameByTicketId(3,"Petkan");
		assertEquals("Petkan",TicketDao.getUsernameByTicketId(3));
		TicketDao.setUsernameByTicketId(3, null);
		assertEquals(null,TicketDao.getUsernameByTicketId(3));
	}
	

}
