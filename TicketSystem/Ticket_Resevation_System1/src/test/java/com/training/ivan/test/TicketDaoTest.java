package com.training.ivan.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.training.dao.TicketDao;
import com.training.ivan.Ticket;
import com.training.ivan.User;

public class TicketDaoTest extends TestCase {
	
	private List<Ticket> tickets;
	private User user;
	
	public TicketDaoTest(String testName){
		super(testName);
	}
	
	/**
	 * Add data to the tickets
	 */
	protected void setUp() throws Exception {
		
		tickets = new ArrayList<Ticket>();
		
		//add user ivan
		user = new User();
		user.setUsername("Ivan");
		tickets.add(new Ticket(1,user));
		
		//add user dragan
		user = new User();
		user.setUsername("Dragan");
		tickets.add(new Ticket(0,user));
		
		//add user tarzan
		user = new User();
		user.setUsername("Tarzan");
		tickets.add(new Ticket(2,user));
		
		//add null user
		tickets.add(new Ticket(3,null));
		
	}
	
	public void testGetUsernameByTicketId(){
		assertEquals("Tarzan", TicketDao.getUsernameByTicketId(2, tickets));
		assertEquals(null,TicketDao.getUsernameByTicketId(3, tickets));
	}
	
	public void testSetUsernameByTicketId(){
		TicketDao.setUsernameByTicketId(3, tickets,"Petkan");
		assertEquals("Petkan",TicketDao.getUsernameByTicketId(3, tickets));
		TicketDao.setUsernameByTicketId(3, tickets, null);
		assertEquals(null,TicketDao.getUsernameByTicketId(3, tickets));
	}
	

}
