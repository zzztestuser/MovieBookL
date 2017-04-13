package com.moviebook.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.moviebook.bean.FriendBean;
import com.moviebook.bean.FriendBean.InviteStatus;
import com.moviebook.bean.UserBean;
import com.moviebook.database.FriendsManager;

/**
 * Servlet implementation class UserServlet
 */
public class UserFriendsServlet extends HttpServlet {
	private static final Logger log = LogManager.getLogger(UserFriendsServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserFriendsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Check authentication

		if ((request.getSession(false) == null) || (request.getSession(false).getAttribute("currentUserBean") == null)) {
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Unauthorized access");
			return;
		}

		// Check for presence of user parameter
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

		// Check for presence of status parameter
		String inviteStatus = StringUtils.trim(request.getParameter("inviteStatus"));
		inviteStatus = StringUtils.isEmpty(inviteStatus) ? "all" : inviteStatus.toLowerCase();

		log.info("Retrieving friends for user " + user + " for invite status " + inviteStatus);

		try {
			List<FriendBean> results;
			if (inviteStatus.equals("all")) {
				results = FriendsManager.getUserFriendsByIdAll(user);
			} else if (inviteStatus.equals("accepted")) {
				results = FriendsManager.getUserFriendsById(user, InviteStatus.ACCEPTED);
			} else if (inviteStatus.equals("sent")) {
				results = FriendsManager.getUserFriendsById(user, InviteStatus.SENT);
			} else if (inviteStatus.equals("rejected")) {
				results = FriendsManager.getUserFriendsById(user, InviteStatus.REJECTED);
			} else {
				// Bad invite status, throw exception
				log.error("Invalid status when retrieving friend: " + inviteStatus);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Invalid status when retrieving friend: " + inviteStatus);
				return;
			}

			if ((results == null) || (results.isEmpty())) {
				// No friends found!
				log.info("No friends for user " + user);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);

			} else {
				log.info(results.size() + " friends for user " + user);

				Gson gs = new Gson();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(gs.toJson(results));
				log.debug(gs.toJson(results));
				return;
			}
		} catch (SQLException e) {
			log.error("Exception encountered retrieving friends for user " + user, e);
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Exception encountered retrieving friends for user " + user);
			return;
		}
	}

}
