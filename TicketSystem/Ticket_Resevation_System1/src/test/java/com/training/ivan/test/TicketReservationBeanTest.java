package com.training.ivan.test;

import junit.framework.TestCase;

import com.training.ivan.TicketReservationBean;
import com.training.ivan.UserLogin;
import com.training.ivan.data.DatabaseListener;

/**
 * 
 * @author ivaniv 
 * Test class for ticket reservation operations
 */
public class TicketReservationBeanTest extends TestCase {

	TicketReservationBean bean;
	UserLogin login;
	DatabaseListener dl;

	public TicketReservationBeanTest(String testName) {
		super(testName);
	}

	protected void setUp() throws Exception {
		super.setUp();
		dl = new DatabaseListener();
		dl.contextInitialized(null); // no servlet context just dummy for initializing db
		bean = new TicketReservationBean();
		login = new UserLogin();
		login.init();
		/*	
		 * bind the login bean and reservations bean
		 * In production this is done automatically through the session
		 * Here we must manually bind it
		 * If we initialize the bean before binding it to the login bean
		 * A null pointer exception will be thrown, this is the expected behavior
		 */
		bean.setLogin(login); 
		bean.init();
	}

	/**
	 * Tests the reservation of a tickets by different users
	 */

	public void testReservationByTickeId() {
		bean.clear();
		// Ivan reserves ticket 0
		login.getUser().setUsername("Ivan");
		bean.reserve(0);

		// Petkan reserves ticket 1
		login.getUser().setUsername("Petkan");
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
		login.getUser().setUsername("Ivan");
		bean.reserve(0);

		// Petkan tries to reserve the same ticket
		login.getUser().setUsername("Petkan");
		bean.reserve(0);

		assertEquals(bean.isTicketReserved(), true);
		assertEquals("red", bean.reservationCheck(0));
	}

	/**
	 * Tests ticket reservation when user name is not entered
	 */

	public void testNoUsernameReservation() {
		bean.clear();
		
		login.getUser().setUsername(null);
		assertEquals(null, login.getUser().getUsername());
		assertEquals("green", bean.reservationCheck(0));
	}

	/**
	 * Tests cancellation of ticket reservation, which is reserved by the
	 * current user
	 */
	public void testUserCancelsReservation() {
		bean.clear();
		
		login.getUser().setUsername("Ivan");
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
		login.getUser().setUsername("Ivan");
		bean.reserve(0);
		login.getUser().setUsername(null);
		bean.declineReservation(0);
		assertEquals("red", bean.reservationCheck(0));
	}
	
	/**
	 * Tests what happens when two users try to reserve the same slot simultaneously
	 */
	public void testTwoUsersSimultaniously(){
		bean.clear();
		//Second user session initialization
		TicketReservationBean bean2 = new TicketReservationBean();
		UserLogin login2 = new UserLogin();
		login2.init();
		bean2.setLogin(login2); // binding
		bean2.init();
		
		login.getUser().setUsername("Ivan");
		login2.getUser().setUsername("Dragan");
		
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
		login.getUser().setUsername("Ivan");
		bean.reserve(0);
		bean.sessionDestroyed();
		assertEquals("green", bean.reservationCheck(0));
	}
	/**
	 * User session expires, but the user has already reserved a ticket
	 */
	public void testSessionKilledByUserReserved(){
		bean.clear();
		login.getUser().setUsername("Petkan");
		bean.reserve(0);
		bean.setTicketRequested(-1);
		bean.sessionDestroyed();
		assertEquals("blue",bean.reservationCheck(0));
	}
	
	protected void tearDown(){
		dl.contextDestroyed(null);// no servlet context used just dummy for clearing db
	}


}
