package com.moviebook.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moviebook.bean.AttendeeBean;
import com.moviebook.bean.EventBean;

public class EventsManager {

	private static final Logger log = LogManager.getLogger(EventsManager.class);

	/**
	 * @param event
	 * @return ID of the event that was created.
	 * @throws SQLException
	 */
	public static int createEvent(EventBean event) throws SQLException {

		log.info("Creating new event");

		// Use FK violations to catch integrity violations instead of checking. Lazy method
		final String sqlCreateEvent = "INSERT INTO `event`(`screeningID`,`createdBy`,`name`,`description`) VALUES(?, ?, ?, ?)";

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sqlCreateEvent, Statement.RETURN_GENERATED_KEYS)) {
				stmt.setInt(1, event.getScreeningID());
				stmt.setInt(2, event.getCreatedByID());
				stmt.setString(3, event.getEventName());
				stmt.setString(4, StringUtils.trim(event.getEventDescription()));

				int updatedRows = stmt.executeUpdate();
				log.info("No. of updated rows:" + updatedRows);

				try (ResultSet rs = stmt.getGeneratedKeys()) {
					rs.next();
					int id = rs.getInt(1);
					log.debug("ID of new event is " + id);

					// invite users next
					inviteToEvent(event.getAttendees(), id);
					return id;
				}

			}
		}
	}

	public static void inviteToEvent(AttendeeBean invitee, int eventID) throws SQLException {
		int userID = invitee.getId();
		int status = invitee.getStatus().code();
		log.info("Inviting user " + userID + " to event " + eventID + " with status " + status);
		final String sql = "INSERT INTO `event_attendees`(`eventID`, `userID`,`attendanceStatus`) VALUES(?, ?, ?)";
		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, eventID);
				stmt.setInt(2, userID);
				stmt.setInt(3, status);

				int updatedRows = stmt.executeUpdate();
				log.info("No. of updated rows: " + updatedRows);
			}
		}
	}

	public static void inviteToEvent(List<AttendeeBean> invitees, int eventID) throws SQLException {
		log.info("Inviting " + invitees.size() + " users to event " + eventID);
		final String sql = "INSERT INTO `event_attendees`(`eventID`, `userID`,`attendanceStatus`) VALUES(?, ?, ?)";
		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				conn.setAutoCommit(false);

				stmt.setInt(1, eventID);

				for (AttendeeBean i : invitees) {
					stmt.setInt(2, i.getId());
					stmt.setInt(3, i.getStatus().code());
					stmt.addBatch();
				}

				int updatedRows[] = stmt.executeBatch();
				conn.commit();

				int totalRows = 0;
				for (int i : updatedRows) {
					totalRows += i;
				}

				log.info("Total no. of updated rows: " + totalRows);
			} catch (SQLException e) {
				// Rollback and float exceptions up
				conn.rollback();
				throw e;
			} finally {
				conn.setAutoCommit(true);
			}

		}

	}

	public static List<EventBean> getEvents(int userID, int inviteStatus) throws SQLException {

		List<EventBean> results = new ArrayList<>();

		final String sql = "SELECT `e`.`id`, `e`.`screeningID`, `ms`.`movieID`, `e`.`createdBy`, `u`.`name` AS `createdByName`,"
				+ " `e`.`name` AS `eventName`, `e`.`description` AS `eventDescription`, `e`.`creationDateTime`, `e`.`modificationDateTime`,"
				+ " `t`.`name` AS `theatreName`, `t`.`location` AS `theatreLocation`, `ms`.`screeningDateTime`"
				+ " FROM `event_attendees` `ea`, `event` `e`, `user` `u`, `movie_screenings` `ms`, `theatre` `t`"
				+ " WHERE (`ea`.`userID` = ?) AND (`ea`.`attendanceStatus` = ?) AND (`e`.`id` = `ea`.`eventID`) AND (`u`.`id` = `e`.`createdBy`)"
				+ " AND (`ms`.`id` = `e`.`screeningID`) AND (`t`.`id` = `ms`.`theatreID`)" + " ORDER BY `creationDateTime` ASC;";

		log.info("Retriving events for user " + userID + " with invite status " + inviteStatus);

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, userID);
				stmt.setInt(2, inviteStatus);

				try (ResultSet rs = stmt.executeQuery()) {
					EventBean ev = new EventBean();
					ev.setId(rs.getInt("id"));
					ev.setScreeningID(rs.getInt("screeningID"));
					ev.setMovieID(rs.getInt("movieID"));
					ev.setCreatedByID(rs.getInt("createdBy"));
					ev.setCreatedByName(rs.getString("createdByName"));
					ev.setEventName(rs.getString("eventName"));
					ev.setEventDescription(rs.getString("eventDescription"));
					ev.setCreationDateTime(rs.getTimestamp("creationDateTime").toLocalDateTime());
					ev.setModificationDateTime(rs.getTimestamp("modificationDateTime").toLocalDateTime());
					ev.setTheatreName(rs.getString("theatreName"));
					ev.setTheatreLocation(rs.getString("theatreLocation"));
					ev.setScreeningDateTime(rs.getTimestamp("screeningDateTime").toLocalDateTime());

					results.add(ev);

				}
			}
		}

		log.info(results.size() + " events retrieved for user " + userID + " with invite status " + inviteStatus);

		return results.isEmpty() ? null : results;
	}

	public static List<EventBean> getEvents(int userID) throws SQLException {

		List<EventBean> results = new ArrayList<>();

		final String sql = "SELECT `e`.`id`, `e`.`screeningID`, `ms`.`movieID`, `e`.`createdBy`, `u`.`name` AS `createdByName`,"
				+ " `e`.`name` AS `eventName`, `e`.`description` AS `eventDescription`, `e`.`creationDateTime`, `e`.`modificationDateTime`,"
				+ " `t`.`name` AS `theatreName`, `t`.`location` AS `theatreLocation`, `ms`.`screeningDateTime`"
				+ " FROM `event_attendees` `ea`, `event` `e`, `user` `u`, `movie_screenings` `ms`, `theatre` `t`"
				+ " WHERE (`ea`.`userID` = ?) AND (`e`.`id` = `ea`.`eventID`) AND (`u`.`id` = `e`.`createdBy`)"
				+ " AND (`ms`.`id` = `e`.`screeningID`) AND (`t`.`id` = `ms`.`theatreID`)" + " ORDER BY `creationDateTime` ASC;";

		log.info("Retriving all events for user " + userID);

		try (Connection conn = DatabaseHelper.getDbConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, userID);

				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						EventBean ev = new EventBean();
						ev.setId(rs.getInt("id"));
						ev.setScreeningID(rs.getInt("screeningID"));
						ev.setMovieID(rs.getInt("movieID"));
						ev.setCreatedByID(rs.getInt("createdBy"));
						ev.setCreatedByName(rs.getString("createdByName"));
						ev.setEventName(rs.getString("eventName"));
						ev.setEventDescription(rs.getString("eventDescription"));
						ev.setCreationDateTime(rs.getTimestamp("creationDateTime").toLocalDateTime());
						ev.setModificationDateTime(rs.getTimestamp("modificationDateTime").toLocalDateTime());
						ev.setTheatreName(rs.getString("theatreName"));
						ev.setTheatreLocation(rs.getString("theatreLocation"));
						ev.setScreeningDateTime(rs.getTimestamp("screeningDateTime").toLocalDateTime());

						results.add(ev);
					}
				}
			}
		}

		log.info(results.size() + " events retrieved for user " + userID);

		return results.isEmpty() ? null : results;
	}

}
