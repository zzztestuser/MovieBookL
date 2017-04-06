package com.moviebook.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moviebook.util.ConfigUtil;

public class DatabaseHelper {

	private static final Logger log = LogManager.getLogger(DatabaseHelper.class);

	public static Connection getDbConnection() throws SQLException {
		String connectionString = "jdbc:mariadb://" + ConfigUtil.getProperty("database.host") + ":" + ConfigUtil.getProperty("database.port") + "/"
				+ ConfigUtil.getProperty("database.schema") + "?user=" + ConfigUtil.getProperty("database.user") + "&password="
				+ ConfigUtil.getProperty("database.password");

		log.debug("Database connection string: " + connectionString);
		log.debug("Creating database connection...");

		try {
			// We shouldn't need to register the driver.. but somehow we do
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new SQLException("Unable to initialize database driver", e);
		}

		return DriverManager.getConnection(connectionString);
	}

}
