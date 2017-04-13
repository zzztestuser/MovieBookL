package com.moviebook.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariadb.jdbc.internal.queryresults.Results;

import com.moviebook.bean.FriendBean;
import com.moviebook.bean.FriendBean.InviteStatus;
import com.moviebook.bean.UserBean;

public class FriendsManager {

	private static final Logger log = LogManager.getLogger(FriendsManager.class);

	public static List<FriendBean> getUserFriendsByIdAll(int id) throws SQLException {

		log.debug("Retrieving friends for ID " + id);

		final String sql = "SELECT `u`.`id`, `u`.`email`, `u`.`name`, `u`.`profilePhotoPath`, `u`.`creationDateTime`, `u`.`modificationDateTime`, `uf`.`inviteStatus`"
				+ " FROM `user` `u`, `user_friends` `uf` WHERE (`uf`.`userID` = ?) AND (`u`.`id` = `uf`.`friendID`) ORDER BY `name` ASC";

		List<FriendBean> resultList = new ArrayList<>();

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setInt(1, id);

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

						log.info("Found friend for ID " + id + " - ID " + result.getId());

						resultList.add(result);

					}

					log.info("Retrieved total of " + resultList.size() + " friends for ID " + id);

				}

			}
		}

		return resultList;

	}

	public static List<FriendBean> getUserFriendsById(int id, InviteStatus status) throws SQLException {

		log.debug("Retrieving friends for ID " + id + " and status " + status.code());

		final String sql = "SELECT `u`.`id`, `u`.`email`, `u`.`name`, `u`.`profilePhotoPath`, `u`.`creationDateTime`, `u`.`modificationDateTime`, `uf`.`inviteStatus`"
				+ " FROM `user` `u`, `user_friends` `uf` WHERE (`uf`.`userID` = ?) AND (`u`.`id` = `uf`.`friendID`) AND (`uf`.`inviteStatus` = ?) ORDER BY `name` ASC";

		ArrayList<FriendBean> resultList = new ArrayList<>();

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setInt(1, id);
				stmt.setInt(2, status.code());

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

						log.info("Found friend for ID " + id + " - ID " + result.getId());

						resultList.add(result);

					}

					log.info("Retrieved total of " + resultList.size() + " friends for ID " + id);

				}

			}
		}

		return resultList;

	}
}
