package com.moviebook.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moviebook.bean.user.MovieBean;

public class MovieManager {

	private static final Logger log = LogManager.getLogger(UserManager.class);

	public static List<MovieBean> getRecommendedMovies(int userID) throws SQLException {

		log.debug("Retrieving recommended movies for ID " + userID);

		final String sql = "SELECT `m`.`id`, `m`.`title`, `m`.`description`, `m`.`language`, `m`.`duration`, `m`.`posterSmallPath`, `m`.`posterMediumPath`, `m`.`posterLargePath` "
				+ " FROM `movie` `m`, `vw_user_recommended` `rm` WHERE (`rm`.`userID` = ?) AND (`m`.`id` = `rm`.`movieID`) ORDER BY `title` ASC";

		HashMap<Integer, MovieBean> movieList = new HashMap<>();

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setInt(1, userID);
				try (ResultSet rs = stmt.executeQuery()) {

					while (rs.next()) {

						// Populate bean
						MovieBean mv = new MovieBean();
						mv.setId(rs.getInt("id"));
						mv.setTitle(StringUtils.trim(rs.getString("title")));
						mv.setDescription(StringUtils.trim(rs.getString("description")));
					}

				}

			}
		}

		return null;

	}

}
