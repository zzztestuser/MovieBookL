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

import com.moviebook.bean.user.MovieBean;

public class RecommendationsManager {

	private static final Logger log = LogManager.getLogger(RecommendationsManager.class);

	public static List<MovieBean> getRecommendedMovies(int userID) throws SQLException {

		log.debug("Retrieving recommended movies for ID " + userID);

		final String sqlRecommendedMovies = "SELECT `m`.`id`, `m`.`title`, `m`.`description`, `m`.`language`, `m`.`duration`, `m`.`posterSmallPath`, `m`.`posterMediumPath`, `m`.`posterLargePath` "
				+ " FROM `movie` `m`, `vw_user_recommended_movies` `rm` WHERE (`rm`.`userID` = ?) AND (`m`.`id` = `rm`.`movieID`) ORDER BY `title` ASC";
		final String sqlGetGenres = "SELECT `g`.`name` FROM `genre` `g`, `movie_genre` `m` WHERE `m`.`movieID` = ? AND `g`.`id` = `m`.`genreID`;";

		ArrayList<MovieBean> mvList = new ArrayList<>();

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sqlRecommendedMovies); PreparedStatement stmt2 = conn.prepareStatement(sqlGetGenres)) {

				stmt.setInt(1, userID);
				try (ResultSet rs = stmt.executeQuery()) {

					while (rs.next()) {

						// Populate bean
						MovieBean mv = new MovieBean();
						mv.setId(rs.getInt("id"));
						mv.setTitle(StringUtils.trim(rs.getString("title")));
						mv.setDescription(StringUtils.trim(rs.getString("description")));
						mv.setLanguage(StringUtils.trim(rs.getString("language")));
						mv.setDuration(rs.getInt("duration"));
						mv.setPosterSmallPath(StringUtils.trim(rs.getString("posterSmallPath")));
						mv.setPosterMediumPath(StringUtils.trim(rs.getString("posterMediumPath")));
						mv.setPosterLargePath(StringUtils.trim(rs.getString("posterLargePath")));

						stmt2.setInt(1, mv.getId());
						try (ResultSet rs2 = stmt2.executeQuery()) {
							ArrayList<String> mvGenres = new ArrayList<>();
							while (rs2.next()) {
								mvGenres.add(StringUtils.trim(rs2.getString("name")));
							}

							mv.setGenre(mvGenres);

						}

						mvList.add(mv);
					}

				}

			}

		}

		return (mvList.size() == 0) ? null : mvList;

	}

	
}
