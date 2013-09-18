package com.training.ivan.test;

import java.util.List;

import junit.framework.TestCase;

import com.training.dao.TicketDao;
import com.training.ivan.data.DatabaseListener;
import com.training.ivan.data.JpaUtil;
import com.training.ivan.data.TicketTableImitation;
import com.training.model.Ticket;
import com.training.model.User;

public class TicketDaoTest extends TestCase {
	
	private User user;
	List<Ticket> tickets;
	DatabaseListener dl;
	public TicketDaoTest(String testName){
		super(testName);
	}
	
	/**
	 * Add data to the tickets
	 */
	protected void setUp() throws Exception {
		
		TicketTableImitation.init();
		TicketTableImitation.clear(); // make sure the ticket data is cleared
		// since this is not run on the server the persistance.xml file is 
		// expected to be in resources/META-INF, not in WEB-INF/classes/META-INF
		dl = new DatabaseListener();
		dl.contextInitialized(null); // no servlet context just dummy for initializing db
		tickets = TicketDao.getTickets(); 
		
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
	
	
	protected void tearDown() throws Exception{
		dl.contextDestroyed(null); // no servlet context just database clear operations
	}
	

}
