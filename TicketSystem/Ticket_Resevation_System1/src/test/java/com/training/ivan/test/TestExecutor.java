package com.training.ivan.test;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.training.dao.TicketDao;
import com.training.ivan.data.DatabaseListener;
import com.training.ivan.webservices.TicketReservationService;

public class TestExecutor extends TestSuite {
	static DatabaseListener dl;
	
	
	public static Test suite() {
		
		final TestSuite s = new TestSuite();
		s.addTestSuite(TicketDaoTest.class);
		s.addTestSuite(TicketReservationBeanTest.class);
		s.addTestSuite(TicketReservationServiceTest.class);
		
		/**
		 * Global setUp and tearDown methods. This class is needed in order to
		 * initializes global resources before the tests are started. After the tests
		 * finish the global resources are cleared
		 * 
		 * @return
		 */
		return new TestSetup(s) {

			protected void setUp() throws Exception {

				// make sure the ticket data is cleared
				// since this is not run on the server the persistance.xml file
				// is
				// expected to be in resources/META-INF, not in
				// WEB-INF/classes/META-INF
				// no servlet context just dummy for initializing db
				dl = new DatabaseListener();
				dl.contextInitialized(null);
			}

			protected void tearDown() throws Exception {
				dl.contextDestroyed(null); // no servlet context just database
											// clear operations
			}
		};
			
	}

}
