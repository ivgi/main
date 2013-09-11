package com.training.ivan;

public class TicketSystemConfig {
	
	public static final int NUMBER_OF_TICKETS = 8;
	
	/**
	 * Can change dynamically if there is a problem with the database
	 * There will be inconsistencies in the data seen by the user
	 * There is no synchronization between the database and inMemory data
	 * TODO Synchronization :)
	 */
	public static boolean USE_DATABASE = true; 

	//TODO initialize the configuration variables from java properties file
}
