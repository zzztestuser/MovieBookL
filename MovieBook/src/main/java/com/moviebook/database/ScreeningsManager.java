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

import com.moviebook.bean.ScreeningBean;

public class ScreeningsManager {

	private static final Logger log = LogManager.getLogger(ScreeningsManager.class);

	public static List<ScreeningBean> getScreenings(int movieID) throws SQLException {

		log.info("Retrieving movie screenings for movie " + movieID);

		final String sql = "SELECT `m`.`id`, `t`.`name`, `t`.`location`, `m`.`screeningDateTime` FROM `movie_screenings` `m`, `theatre` `t` WHERE (`t`.`id` = `m`.`theatreID`) AND (`m`.`movieID` = ?)";

		List<ScreeningBean> results = new ArrayList<>();

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setInt(1, movieID);
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						ScreeningBean result = new ScreeningBean();
						result.setId(rs.getInt("id"));
						result.setTheatreName(StringUtils.trim(rs.getString("name")));
						result.setTheatreLocation(StringUtils.trim(rs.getString("location")));
						result.setScreeningDateTime(rs.getTimestamp("screeningDateTime").toLocalDateTime());

						results.add(result);
					}

				}
			}
		}
		log.info(results.size() + " screenings for movie " + movieID);
		return results.isEmpty() ? null : results;

	}

}
