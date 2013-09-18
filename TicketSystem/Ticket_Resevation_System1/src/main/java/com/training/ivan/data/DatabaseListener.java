package com.training.ivan.data;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.training.ivan.TicketSystemConfig;

/**
 * @author ivaniv
 * When the system starts we make sure to initialize the data in the database
 * correctly When the system stops we make sure to clear the databases and
 * release resources correctly
 * 
 */
public class DatabaseListener implements ServletContextListener {
	
	/**
	 * Clears all data
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		if (TicketSystemConfig.USE_JPA) {
			JpaUtil.clear();
			JpaUtil.getEntityManagerFactory().close();
		}
		if (TicketSystemConfig.USE_DATABASE)
			DataBaseUtil.clearDB();

		TicketTableImitation.clear();
	}

	/**
	 * initializes all data
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		if (TicketSystemConfig.USE_JPA)
			JpaUtil.initDB();

		if (TicketSystemConfig.USE_DATABASE)
			DataBaseUtil.initDB();

		// inMemory data is always initialized simultaneously
		TicketTableImitation.init();
	}

}
