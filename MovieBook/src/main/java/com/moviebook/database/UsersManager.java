package com.moviebook.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moviebook.bean.FriendBean;
import com.moviebook.bean.FriendBean.InviteStatus;
import com.moviebook.bean.UserBean;

/**
 * Database manager class for handling {@code USER} table in database
 * 
 * @author Lua Choon Ngee
 *
 */
public class UsersManager {

	private static final Logger log = LogManager.getLogger(UsersManager.class);

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

	public static UserBean getUserByEmail(String email) throws SQLException, InvalidUserException {

		log.debug("Retrieving user record for email " + email);

		final String sql = "SELECT `id`, `email`, `name`, `profilePhotoPath`, `creationDateTime`, `modificationDateTime` FROM `user` WHERE `email` = ?";

		UserBean result = null;

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setString(1, email);

				try (ResultSet rs = stmt.executeQuery()) {

					if (rs.next()) {

						result = new UserBean();
						result.setId(rs.getInt("id"));
						result.setEmail(StringUtils.trim(rs.getString("email")));
						result.setName(StringUtils.trim(rs.getString("name")));
						result.setProfilePhotoPath((StringUtils.trim(rs.getString("profilePhotoPath"))));
						result.setCreationDateTime(rs.getTimestamp("creationDateTime").toLocalDateTime());
						result.setModificationDateTime(rs.getTimestamp("modificationDateTime").toLocalDateTime());

						if (rs.next() != false) {
							// More than one user returned!
							throw new InvalidUserException("More than 1 user returned from database for email " + email);
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

	public static UserBean getUserById(int id) throws SQLException, InvalidUserException {

		log.debug("Retrieving user record for ID " + id);

		final String sql = "SELECT `id`, `email`, `name`, `profilePhotoPath`, `creationDateTime`, `modificationDateTime` FROM `user` WHERE `id` = ?";

		UserBean result = null;

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setInt(1, id);

				try (ResultSet rs = stmt.executeQuery()) {

					if (rs.next()) {

						result = new UserBean();
						result.setId(rs.getInt("id"));
						result.setEmail(StringUtils.trim(rs.getString("email")));
						result.setName(StringUtils.trim(rs.getString("name")));
						result.setProfilePhotoPath((StringUtils.trim(rs.getString("profilePhotoPath"))));
						result.setCreationDateTime(rs.getTimestamp("creationDateTime").toLocalDateTime());
						result.setModificationDateTime(rs.getTimestamp("modificationDateTime").toLocalDateTime());

						if (rs.next() != false) {
							// More than one user returned!
							throw new InvalidUserException("More than 1 user returned from database for ID " + id);
						}

						log.info("Retrieved user record for ID " + id);

					} else {
						log.warn("No user record for ID " + id + " exists!");
					}

				}

			}
		}
		return result;
	}

	public static boolean isUserExistsById(int id) throws SQLException {

		log.debug("Checking for ID " + id);

		final String sql = "SELECT COUNT(`id`) FROM `user` WHERE `id` = ?";

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setInt(1, id);

				try (ResultSet rs = stmt.executeQuery()) {
					// Should return 1 row only
					if (rs.next()) {
						return ((rs.getInt(1) == 1));
					}

				}

			}
		}

		return false;
	}


}
