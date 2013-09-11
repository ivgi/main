package com.training.ivan.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.ivan.TicketSystemConfig;


public class DataBaseUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DataBaseUtil.class);
	
	/**
	 * This method is used to open a new connection and return a reference to it.
	 * The system will be able to connect to the database only if there is a configured datasource to the database
	 * The configuration of the DataSource is done by web app server configuration or database administration tools
	 * prior to deploying the ticket application
	 * 
	 * This implementation is independent of the container.
	 * 
	 * @return a reference to an open connection
	 */
	public static Connection getDatabaseConnection() {
		try {
			// Obtain our environment naming context
			Context initContext = new InitialContext();
			DataSource ds = (DataSource)initContext.lookup("java:/comp/env/jdbc/TICKETS");
			Connection con = ds.getConnection();
			if(con == null)
				logger.info("Connection is null");
			return con;
		} catch (NamingException e) {
			logger.error("Datasource lookup problem",e);
		} catch (SQLException e) {
			logger.error("Can not get a connection to the database",e);
		}
		return null; //if exception occurs the method returns null
	}
	
	/**
	 *  Initializes the database in an initial state. This method is used after 
	 *  first database creation or after a clearDB() method. 
	 *  In production this method will be used only for an initial deployment of the web app.
	 */
	public static void initDB(){
		
		logger.info("Initializing database ...");
		Connection con = getDatabaseConnection();
		if(con == null){
			logger.info("Database not initialized!");
			logger.debug("Connection is null");
			return;
		}
		PreparedStatement stm = null;
		String prepare = "INSERT INTO ticket VALUE ( ? ,NULL)";
		logger.debug("Preparing statement: \"INSERT INTO ticket VALUE ( ? ,NULL)\"");
		
		//insert empty tickets
		try {
			stm = con.prepareStatement(prepare);
			for(int i = 0; i < TicketSystemConfig.NUMBER_OF_TICKETS; i++){
				stm.setInt(1, i);
				stm.execute();
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		finally{
			if(stm!=null)
				try {
					stm.close();
					con.close();
					logger.debug("InitDB method is closing db connection");
				} catch (SQLException e) {
					logger.error("Database access errors on close method",e);
				}
		}
	}
	
	/**
	 * Clears the tables in the database - deletes all data in the database.
	 * This method is only applicable for exercise and testing purposes
	 */
	
	public static void clearDB(){
		Connection con = getDatabaseConnection();
		if(con == null){
			logger.info("Database not cleared!");
			logger.debug("Connection is null");
			return;
		}
		Statement stm = null;
		logger.info("Clearing db ...");
		try {
			stm = con.createStatement();
			stm.execute("DELETE FROM ticket");
			stm.execute("DELETE FROM usert");
			
			//if more database tables are added to the database
			//delete statements for this tables must be manually added here
			
		} catch (SQLException e) {
			logger.error("Clearing database failed",e);
		}finally{
			if(stm!=null)
				try {
					stm.close();
					con.close();
				} catch (SQLException e) {
					logger.error("Database access errors on close method",e);
				}
		}
		
		
	}

}
