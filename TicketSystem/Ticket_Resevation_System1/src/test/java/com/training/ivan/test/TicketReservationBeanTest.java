package com.training.ivan.test;

import junit.framework.TestCase;

import com.training.ivan.TicketReservationBean;

/**
 * 
 * @author ivaniv 
 * Test class for ticket reservation operations
 */
public class TicketReservationBeanTest extends TestCase {

	TicketReservationBean bean;

	public TicketReservationBeanTest(String testName) {
		super(testName);
	}

	protected void setUp() throws Exception {
		super.setUp();
		bean = new TicketReservationBean();
		bean.init();
	}

	/**
	 * Tests the reservation of a tickets by different users
	 */

	public void testReservationByTickeId() {
		bean.clear();
		// Ivan reserves ticket 0
		bean.setUsername("Ivan");
		bean.reserve(0);

		// Petkan reserves ticket 1
		bean.setUsername("Petkan");
		bean.reserve(1);

		assertEquals("red", bean.reservationCheck(0));
		assertEquals("blue", bean.reservationCheck(1));
		assertEquals("green", bean.reservationCheck(2));
	}

	/**
	 * Tests that already reserved ticket by another user can not be reserved
	 * again
	 */

	public void testDeclineReservationByTicketId() {
		bean.clear();
		// Ivan reserves ticket 0
		bean.setUsername("Ivan");
		bean.reserve(0);

		// Petkan tries to reserve the same ticket
		bean.setUsername("Petkan");
		bean.reserve(0);

		assertEquals(bean.isTicketReserved(), true);
		assertEquals("red", bean.reservationCheck(0));
	}

	/**
	 * Tests ticket reservation when user name is not entered
	 */

	public void testNoUsernameReservation() {
		bean.clear();
		bean.setUsername(null);
		assertEquals(null, bean.getUsername());
		assertEquals("green", bean.reservationCheck(0));
	}

	/**
	 * Tests cancellation of ticket reservation, which is reserved by the
	 * current user
	 */
	public void testUserCancelsReservation() {
		bean.clear();
		bean.setUsername("Ivan");
		bean.reserve(0);
		assertEquals("blue", bean.reservationCheck(0));
		bean.declineReservation(0);
		assertEquals("green", bean.reservationCheck(0));
	}

	/**
	 * When a user declines a reservation that is not his own, the application
	 * does not cancel the reservation
	 */
	public void testDeclineReservationNotAccepted() {
		bean.clear();
		bean.setUsername("Ivan");
		bean.reserve(0);
		bean.setUsername(null);
		bean.declineReservation(0);
		assertEquals("red", bean.reservationCheck(0));
	}
	
	/**
	 * Tests what happens when two users try to reserve the same slot simultaneously
	 */
	public void testTwoUsersSimultaniously(){
		bean.clear();
		TicketReservationBean bean2 = new TicketReservationBean();
		bean2.init();
		bean.setUsername("Ivan");
		bean2.setUsername("Dragan");
		
		bean.reserve(0);
		bean2.reserve(0);
		assertEquals(true, bean2.isTicketReserved());
		assertEquals("blue", bean.reservationCheck(0));
	}
	/**
	 * Tests what happens when the user session expires
	 */
	public void testSessionKilledByUser(){
		bean.clear();
		bean.setUsername("Ivan");
		bean.reserve(0);
		bean.sessionDestroyed();
		assertEquals("green", bean.reservationCheck(0));
	}
	/**
	 * User session expires, but the user has already reserved a ticket
	 */
	public void testSessionKilledByUserReserved(){
		bean.clear();
		bean.setUsername("Petkan");
		bean.reserve(0);
		bean.setTicketRequested(-1);
		bean.sessionDestroyed();
		assertEquals("blue",bean.reservationCheck(0));
	}


}
