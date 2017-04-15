package com.moviebook.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moviebook.bean.AttendeeBean;
import com.moviebook.bean.AttendeeBean.InviteStatus;
import com.moviebook.bean.UserBean;
import com.moviebook.database.EventsManager;

/**
 * Servlet implementation class EventsInviteServlet
 */
public class EventsInviteServlet extends HttpServlet {
	private static final Logger log = LogManager.getLogger(EventsInviteServlet.class);

	public EventsInviteServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Check authentication
		if ((request.getSession(false) == null) || (request.getSession(false).getAttribute("currentUserBean") == null)) {
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Unauthorized access");
			return;
		}

		// Get user
		int userID;

		if (StringUtils.isEmpty(StringUtils.trim(request.getParameter("user")))) {
			// No user specified. Let's get the current user from session.
			userID = ((UserBean) request.getSession(false).getAttribute("currentUserBean")).getId();
		} else {
			// Support only one userID at present.
			// TODO Implement support for semicomma delimited multiple user ids
			try {
				userID = Integer.parseInt(StringUtils.trim(request.getParameter("user")));
				log.debug("User ID from passed parameter: " + userID);
			} catch (NumberFormatException e) {
				log.error("Invalid userID passed", e);
				// Invalid ID, return
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Invalid user ID passed ");
				return;
			}
		}

		// Check for parameters
		int eventID;
		if (StringUtils.isEmpty(request.getParameter("event"))) {
			log.error("No event parameter passed when accepting invite");
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("No event parameter passed when accepting invite");
			return;
		} else {
			try {
				eventID = Integer.parseInt(StringUtils.trim(request.getParameter("event")));
			} catch (NumberFormatException e) {
				log.error("Invalid event parameter passed", e);
				// Invalid ID, return
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Invalid event parameter passed");
				return;
			}
		}
		try {
			EventsManager.updateAttendeeInviteStatus(eventID, userID, InviteStatus.ACCEPTED.code());
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return;
		} catch (SQLException e) {
			log.error("Exception encountered accepting invite for event " + eventID + " and user " + userID, e);
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Exception encountered accepting invite for event " + eventID + " and user " + userID);
			return;
		}
	}

}
