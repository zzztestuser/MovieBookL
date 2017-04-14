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
import com.moviebook.bean.MovieBean;

public class RecommendationsManager {

	private static final Logger log = LogManager.getLogger(RecommendationsManager.class);

	/**
	 * Note MovieBean object does not include screening locations
	 * 
	 * @param userID
	 * @return
	 * @throws SQLException
	 */
	public static List<MovieBean> getRecommendedMovies(int userID) throws SQLException {

		log.debug("Retrieving recommended movies for ID " + userID);

		final String sql = "SELECT `m`.`id`, `m`.`title`, `m`.`description`, `m`.`language`, `m`.`duration`, `m`.`posterSmallPath`, `m`.`posterMediumPath`, `m`.`posterLargePath` "
				+ " FROM `movie` `m`, `vw_user_recommended_movies` `rm` WHERE (`rm`.`userID` = ?) AND (`m`.`id` = `rm`.`movieID`) ORDER BY `title` ASC";

		ArrayList<MovieBean> mvList = new ArrayList<>();

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
						mv.setLanguage(StringUtils.trim(rs.getString("language")));
						mv.setDuration(rs.getInt("duration"));
						mv.setPosterSmallPath(StringUtils.trim(rs.getString("posterSmallPath")));
						mv.setPosterMediumPath(StringUtils.trim(rs.getString("posterMediumPath")));
						mv.setPosterLargePath(StringUtils.trim(rs.getString("posterLargePath")));

						mv.setGenre(GenreManager.getGenreNamesForMovie(mv.getId()));

						mvList.add(mv);
					}

				}

			}

		}

		return (mvList.size() == 0) ? null : mvList;

	}

	/**
	 * Returns all friends who are interested in a movie {@code movieID} for user {@code userID} with invite status {@code inviteStatus}
	 * 
	 * @param userID
	 * @param movieID
	 * @param inviteStatus
	 * @return
	 * @throws SQLException
	 */
	public static List<FriendBean> getInterestedFriends(int userID, int movieID, InviteStatus inviteStatus) throws SQLException {
		log.debug("Retrieving interested friends (invite status " + inviteStatus + ") for user ID " + userID + " for movie " + movieID);
		final String sql = "SELECT `u`.`id`, `u`.`email`, `u`.`name`, `u`.`profilePhotoPath`, `u`.`creationDateTime`, `u`.`modificationDateTime`, `uf`.`inviteStatus` "
				+ "FROM `user` `u`, `user_friends` `uf`, `vw_user_recommended_movies` `rm` "
				+ "WHERE (`uf`.`userID` = ?) AND (`u`.`id` = `uf`.`friendID`) AND (`uf`.`inviteStatus` = ?)"
				+ " AND (`rm`.`userID` = `uf`.`friendID`) AND (`rm`.`movieID` = ?) ORDER BY `name` ASC;";

		ArrayList<FriendBean> friendList = new ArrayList<>();

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setInt(1, userID);
				stmt.setInt(2, inviteStatus.code());
				stmt.setInt(3, movieID);

				try (ResultSet rs = stmt.executeQuery()) {

					while (rs.next()) {
						FriendBean result = new FriendBean();
						result.setId(rs.getInt("id"));
						result.setEmail(StringUtils.trim(rs.getString("email")));
						result.setName(StringUtils.trim(rs.getString("name")));
						result.setProfilePhotoPath((StringUtils.trim(rs.getString("profilePhotoPath"))));
						result.setCreationDateTime(rs.getTimestamp("creationDateTime").toLocalDateTime());
						result.setModificationDateTime(rs.getTimestamp("modificationDateTime").toLocalDateTime());
						result.setStatus(InviteStatus.valueOf(rs.getInt("inviteStatus")));

						log.info("Friend " + result.getId() + " of user " + userID + " is interested in movie " + movieID);

						friendList.add(result);
					}

				}

			}

		}

		return (friendList.isEmpty()) ? null : friendList;
	}

}
