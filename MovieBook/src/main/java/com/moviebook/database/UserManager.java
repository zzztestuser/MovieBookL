package com.moviebook.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moviebook.user.User;

/**
 * Database manager class for handling {@code USER} table in database
 * 
 * @author Lua Choon Ngee
 *
 */
public class UserManager {

	private static final Logger log = LogManager.getLogger(UserManager.class);

	/**
	 * Returns if there is an exact match in the database for the user ID and password. Please use the hashed password if relevant
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static boolean isUserValid(String email, String password) throws SQLException {

		final String sql = "SELECT COUNT(`id`) FROM `user` WHERE `email` = ? AND `password` = ?";

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setString(1, email);
				stmt.setString(2, password);

				try (ResultSet results = stmt.executeQuery()) {

					// Should return 1 row only
					if (results.next()) {
						return (results.getInt(1) == 1);
					}

				}

			}
		}

		return false;
	}

	public static User getUserByEmail(String email) throws SQLException, InvalidUserException {

		log.debug("Retrieving user record for email " + email);

		final String sql = "SELECT `id`, `email`, `name`, `profilePhotoPath`, `creationDateTime`, `modificationDateTime` FROM `user` WHERE `email` = ?";

		User result = null;

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setString(1, email);

				try (ResultSet rs = stmt.executeQuery()) {

					if (rs.next()) {

						result = new User();
						result.setId(rs.getInt("id"));
						result.setEmail(StringUtils.trim(rs.getString("email")));
						result.setName(StringUtils.trim(rs.getString("name")));
						result.setProfilePhotoPath((StringUtils.trim(rs.getString("profilePhotoPath"))));
						result.setCreationDateTime(rs.getTimestamp("creationDateTime").toLocalDateTime());
						result.setModificationDateTime(rs.getTimestamp("modificationDateTime").toLocalDateTime());

						if (rs.next() != false) {
							// More than one user returned!
							throw new InvalidUserException("More than 1 user returned from database for " + email);
						}

						log.info("Retrieved user record for email " + email);

					} else {
						log.warn("No user record for email " + email + " exists!");
					}

				}

			}
		}
		return result;

	}

}
