package com.training.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.ivan.User;
import com.training.ivan.data.DataBaseUtil;

public class UserDao {

	private static final Logger logger = LoggerFactory
			.getLogger(TicketDao.class);
	
	/**
	 * Queries the database and returns a user object
	 * @param userId - id of the user
	 * @return a user object associated with @param id
	 */

	public static User getUserById(Integer userId) {

		Connection con = DataBaseUtil.getDatabaseConnection();
		if (con == null)
			return null;

		Statement stmt = null;
		String query = "SELECT id, username FROM usert";

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

}
