package com.moviebook.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moviebook.bean.AttendeeBean;
import com.moviebook.bean.EventBean;
import com.moviebook.bean.UserBean;
import com.moviebook.database.EventsManager;

/**
 * Servlet implementation class EventsInviteServlet
 */
public class EventsServlet extends HttpServlet {
	private static final Logger log = LogManager.getLogger(EventsServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EventsServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Check authentication
		if ((request.getSession(false) == null) || (request.getSession(false).getAttribute("currentUserBean") == null)) {
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Unauthorized access");
			return;
		}

		int user;

		if (StringUtils.isEmpty(StringUtils.trim(request.getParameter("user")))) {
			// No user specified. Let's get the current user from session.
			user = ((UserBean) request.getSession(false).getAttribute("currentUserBean")).getId();
		} else {
			// Support only one userID at present.
			// TODO Implement support for semicomma delimited multiple user ids
			try {
				user = Integer.parseInt(StringUtils.trim(request.getParameter("user")));
				log.debug("User ID from passed parameter: " + user);
			} catch (NumberFormatException e) {
				log.error("Invalid userID passed", e);
				// Invalid ID, return
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Invalid user ID passed ");
				return;
			}
		}

		try {
			List<EventBean> events = EventsManager.getEvents(user);
			if ((events == null) || (events.isEmpty())) {
				// No results
				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				return;
			} else {
				log.info(events.size() + " events retrieved for user " + user);

				// Set the attendees
				for (EventBean i : events) {
					i.setAttendees(EventsManager.getAttendeesForEvent(i.getId()));
					log.info((i.getAttendees() == null ? "0" : i.getAttendees().size()) + " attendees for event " + i.getId());
				}


				Gson gs = new Gson();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(gs.toJson(events));
				// log.debug("events JSON is " + gspp.toJson(events));

			}

		} catch (SQLException e) {
			log.error("Exception encountered getting events", e);
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Exception encountered getting events");
			return;
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Check authentication
		if ((request.getSession(false) == null) || (request.getSession(false).getAttribute("currentUserBean") == null)) {
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Unauthorized access");
			return;
		}

		EventBean ev = new EventBean();
		ev.setCreatedByID(((UserBean) request.getSession(false).getAttribute("currentUserBean")).getId());

		// Check for friend parameter
		String friendsParam = StringUtils.trim(request.getParameter("friend"));
		if (StringUtils.isEmpty(friendsParam)) {
			// No friend specified. Fail out
			log.error("No attendee(s) specified for event.");
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("No attendee(s) specified for event.");
			return;
		} else {
			try {
				List<AttendeeBean> ab = new ArrayList<>();
				String[] friendsList = friendsParam.split(",");
				for (String i : friendsList) {
					AttendeeBean att = new AttendeeBean();
					att.setId(Integer.parseInt(i));
					att.setStatus(AttendeeBean.InviteStatus.SENT);
					ab.add(att);
				}

				ev.setAttendees(ab);

			} catch (NumberFormatException e) {
				log.error("Invalid attendee specified for event.");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Invalid attenee specified for event.");
				return;
			}
		}

		if (StringUtils.isEmpty(request.getParameter("screening"))) {
			// No screening specified. Fail out
			log.error("No screening specified for event.");
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("No screening specified for event.");
			return;
		} else {
			try {
				ev.setScreeningID(Integer.parseInt(StringUtils.trim(request.getParameter("screening"))));
			} catch (NumberFormatException e) {
				log.error("Invalid screening parameter specified for event.");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Invalid screening parameter specified for event.");
				return;
			}
		}

		if (StringUtils.isEmpty(request.getParameter("eventName"))) {
			// No screening specified. Fail out
			log.error("No screening specified for event.");
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("No screening specified for event.");
			return;
		} else {
			ev.setEventName(StringUtils.trim(request.getParameter("eventName")));
		}

		ev.setEventDescription(StringUtils.trim(request.getParameter("eventDescription")));

		try {
			int eventID = EventsManager.createEvent(ev);
			log.info("Created event with id " + eventID);
			// Success
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return;

		} catch (SQLException e) {
			log.error("Exception encountered creating event", e);
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Exception encountered creating event");
			return;
		}

	}

}
