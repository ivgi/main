package com.training.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.ivan.User;
import com.training.ivan.data.DataBaseUtil;

/**
 * Class responsible for user data operations with the database. If the database
 * is not used, then the methods return null or do nothing
 * 
 * @author ivaniv
 * 
 */
public class UserDao {

	private static final Logger logger = LoggerFactory
			.getLogger(TicketDao.class);

	/**
	 * Queries the database and returns a user object
	 * 
	 * @param userId
	 *            - id of the user
	 * @return a user object associated with @param id
	 */

	public static User getUserById(Integer userId) {

		Connection con = DataBaseUtil.getDatabaseConnection();
		if (con == null)
			return null;

		Statement stmt = null;
		String query = "SELECT id, username FROM usert WHERE id = " + userId;

		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				if (userId == rs.getInt("id"))
					return new User(rs.getString("username"), userId);
			}
			return null;
		} catch (SQLException e) {
			logger.error("Getting user from database failed", e);
		} finally {
			if (stmt != null)
				try {
					stmt.close();
					con.close();
				} catch (SQLException e) {
					logger.error("Database access errors on close method", e);
				}
		}
		return null;
	}

	/**
	 * Adds user to the database. If a user already exists, will catch and log
	 * an sql exception. The work of the system will not be interrupted.
	 * 
	 * @param user
	 */
	public static void addUser(User user) {

		Connection con = DataBaseUtil.getDatabaseConnection();
		if (con == null) {
			logger.error("Connection problem, adding user to database failed");
			return;
		}
		String query = "INSERT INTO usert(username) VALUES( ? )";
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement(query);
			stm.setString(1, user.getUsername());
			stm.execute();
		} catch (SQLException e) {
			logger.error(e.toString()); // TODO check if e.toString() is correct
		} finally {
			if (stm != null)
				try {
					stm.close();
					con.close();
				} catch (SQLException e) {
					logger.error("Database access errors on close method", e);
				}
		}
	}

	/**
	 * Updates the data of the existing user. If there is no user an
	 * SqlException is logged. The system work is not interrupted. This method
	 * is currently not used by other classes but is useful, so it is declared
	 * public.
	 * 
	 * @param user
	 */
	public static void updateUser(User user) {

		Connection con = DataBaseUtil.getDatabaseConnection();
		if (con == null) {
			logger.error("Connection problem, updating user failed");
			return;
		}
		String updateQuery = "UPDATE usert SET username = ? WHERE id = ?";
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement(updateQuery);
			stm.setString(1, user.getUsername());
			stm.setInt(2, user.getUserId());
			stm.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.toString()); // TODO check if e.toString() is correct
		} finally {
			if (stm != null)
				try {
					stm.close();
					con.close();
				} catch (SQLException e) {
					logger.error("Database access errors on close method", e);
				}
		}
	}

	/**
	 * Checks if the user exists and updates data. If user does not exist,
	 * creates new user
	 * 
	 * @param user
	 *            - to be added or updated
	 * 
	 */
	public static void addOrUpdateUser(User user) {

		if (getUserById(user.getUserId()) == null)
			addUser(user);
		else
			updateUser(user);
	}
	
	public static Integer getLastInsertedUserId(){
		Connection con = DataBaseUtil.getDatabaseConnection();
		if (con == null) {
			logger.error("Connection problem, getting last userId failed");
			return -1;
		}
		String query = "SELECT id FROM usert order by id DESC LIMIT 1";
		logger.debug("SELECT id FROM usert order by id DESC LIMIT 1");
		Statement stm = null;
		try {
			stm = con.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.next();
			logger.debug("Last inserted userId is: " + rs.getInt("id"));
			return rs.getInt("id");
		} catch (SQLException e) {
			logger.error(e.toString()); // TODO check if e.toString() is correct
		} finally {
			if (stm != null)
				try {
					stm.close();
					con.close();
				} catch (SQLException e) {
					logger.error("Database access errors on close method", e);
				}
		}
		return -1;
	}

}
