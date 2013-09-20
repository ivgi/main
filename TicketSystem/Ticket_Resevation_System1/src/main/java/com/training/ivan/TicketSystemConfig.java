package com.training.ivan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketSystemConfig {

	private static final Logger logger = LoggerFactory
			.getLogger(TicketSystemConfig.class);

	public static int NUMBER_OF_TICKETS;

	/**
	 * Can change dynamically if there is a problem with the database There will
	 * be inconsistencies in the data seen by the user There is no
	 * synchronization between the database and inMemory data
	 * 
	 */
	public static boolean USE_DATABASE;

	/**
	 * JPA uses different database than the database used by the jdbc datasource
	 * If this configuration is set to true JPA will be used. The two databases
	 * work simultaneously.
	 */
	public static boolean USE_JPA;

	// initialize the configuration variables from java properties file
	static {
		Properties prop = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream("ticket_system.properties");
			prop.load(in);
			USE_DATABASE = prop.getProperty("database.jdbcConnection").equals("true") ? true : false;
			USE_JPA = prop.getProperty("database.jpaConnection").equals("true") ? true : false;
			NUMBER_OF_TICKETS = Integer.parseInt(prop.getProperty("tickets.number"));
		} catch (NumberFormatException e) {
			logger.error("wrong configuration parameter for ticket.number property, loading default value");
			NUMBER_OF_TICKETS = 10;
		} catch (Exception e) {
			logger.info("ticket_system.properties not loaded, loading system default configuration");
			logger.error(e.getMessage());
			NUMBER_OF_TICKETS = 10;
			USE_DATABASE = false;
			USE_JPA = false;
		}
		finally{
			try {
				if(in != null)
				in.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

	}
}
