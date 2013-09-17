package com.training.ivan.data;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.training.ivan.TicketSystemConfig;

public class DatabaseListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		if (TicketSystemConfig.USE_JPA) {
			JpaUtil.clear();
			JpaUtil.getEntityManagerFactory().close();
		}
		if (TicketSystemConfig.USE_DATABASE)
			DataBaseUtil.clearDB();

		TicketTableImitation.clear();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		if (TicketSystemConfig.USE_JPA)
			JpaUtil.initDB();

		if (TicketSystemConfig.USE_DATABASE)
			DataBaseUtil.initDB();

		// inMemory data is always initialized simultaneously
		TicketTableImitation.init();
	}

}
