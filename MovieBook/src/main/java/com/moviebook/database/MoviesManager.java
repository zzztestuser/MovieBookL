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

import com.moviebook.bean.MovieBean;

public class MoviesManager {

	private static final Logger log = LogManager.getLogger(MoviesManager.class);

	public static final MovieBean getMovie(int movieID) throws SQLException {
		log.debug("Retrieving movie ID " + movieID);

		final String sql = "SELECT `m`.`id`, `m`.`title`, `m`.`description`, `m`.`language`, `m`.`duration`, `m`.`posterSmallPath`, `m`.`posterMediumPath`, `m`.`posterLargePath` "
				+ " FROM `movie` `m` WHERE (`m`.`id` = ?)";

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setInt(1, movieID);

				try (ResultSet rs = stmt.executeQuery()) {

					if (rs.next()) {
						MovieBean mv = new MovieBean();
						mv.setId(rs.getInt("id"));
						mv.setTitle(StringUtils.trim(rs.getString("title")));
						mv.setDescription(StringUtils.trim(rs.getString("description")));
						mv.setLanguage(StringUtils.trim(rs.getString("language")));
						mv.setDuration(rs.getInt("duration"));
						mv.setPosterSmallPath(StringUtils.trim(rs.getString("posterSmallPath")));
						mv.setPosterMediumPath(StringUtils.trim(rs.getString("posterMediumPath")));
						mv.setPosterLargePath(StringUtils.trim(rs.getString("posterLargePath")));
						mv.setGenre(GenreManager.getGenreNamesForMovie(mv.getId()));
						return mv;

					} else {
						return null;
					}

				}
			}
		}

	}

	public static List<MovieBean> findMovieByTitle(String title) throws SQLException {
		log.debug("Searching for movies with title " + title);

		// Due to the collation we use, LIKE is case-insensitive, so no need to work around
		final String sql = "SELECT `m`.`id`, `m`.`title`, `m`.`description`, `m`.`language`, `m`.`duration`, `m`.`posterSmallPath`, `m`.`posterMediumPath`, `m`.`posterLargePath` "
				+ " FROM `movie` `m` WHERE (`m`.`title` LIKE ?) ORDER BY `title` ASC";

		List<MovieBean> results = new ArrayList<>();
		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setString(1, "%" + title + "%");

				try (ResultSet rs = stmt.executeQuery()) {

					while (rs.next()) {
						MovieBean mv = new MovieBean();
						mv.setId(rs.getInt("id"));
						mv.setTitle(StringUtils.trim(rs.getString("title")));
						mv.setDescription(StringUtils.trim(rs.getString("description")));
						mv.setLanguage(StringUtils.trim(rs.getString("language")));
						mv.setDuration(rs.getInt("duration"));
						mv.setPosterSmallPath(StringUtils.trim(rs.getString("posterSmallPath")));
						mv.setPosterMediumPath(StringUtils.trim(rs.getString("posterMediumPath")));
						mv.setPosterLargePath(StringUtils.trim(rs.getString("posterLargePath")));
						mv.setGenre(GenreManager.getGenreNamesForMovie(mv.getId()));
						results.add(mv);

					}

					return results;

				}
			}
		}

	}

}
