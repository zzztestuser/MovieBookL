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

public class GenreManager {

	private static final Logger log = LogManager.getLogger(GenreManager.class);

	public static final List<String> getGenreNamesForMovie(int movieID) throws SQLException {
		final String sql = "SELECT `g`.`name` FROM `genre` `g`, `movie_genre` `m` WHERE `m`.`movieID` = ? AND `g`.`id` = `m`.`genreID`;";

		List<String> result = new ArrayList<>();

		try (Connection conn = DatabaseHelper.getDbConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, movieID);

				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						result.add(StringUtils.trim(rs.getString("name")));
					}
				}

			}
		}

		return (result.size() != 0) ? result : null;
	}

}
