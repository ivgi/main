package com.training.ivan.test;

import java.util.List;

import junit.framework.TestCase;

import com.training.dao.TicketDao;
import com.training.model.Ticket;

public class TicketDaoTest extends TestCase {

	List<Ticket> tickets;

	public TicketDaoTest(String testName) {
		super(testName);
	}

	/**
	 * Add data to the tickets
	 */
	protected void setUp() throws Exception {

		tickets = TicketDao.getTickets();

		// add user ivan
		TicketDao.setUsernameByTicketId(1, "Ivan");

		// add user dragan
		TicketDao.setUsernameByTicketId(0, "Dragan");

		// add user tarzan
		TicketDao.setUsernameByTicketId(2, "Tarzan");

		// add null user
		TicketDao.setUsernameByTicketId(3, null);

	}

	public void testGetUsernameByTicketId() {
		assertEquals("Tarzan", TicketDao.getUsernameByTicketId(2));
		assertEquals(null, TicketDao.getUsernameByTicketId(3));
	}

	public void testSetUsernameByTicketId() {
		TicketDao.setUsernameByTicketId(3, "Petkan");
		assertEquals("Petkan", TicketDao.getUsernameByTicketId(3));
		TicketDao.setUsernameByTicketId(3, null);
		assertEquals(null, TicketDao.getUsernameByTicketId(3));
	}
}
