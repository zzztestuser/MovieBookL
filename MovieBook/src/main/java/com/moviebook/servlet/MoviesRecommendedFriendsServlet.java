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
import com.moviebook.bean.FriendBean;
import com.moviebook.bean.UserBean;
import com.moviebook.bean.FriendBean.InviteStatus;
import com.moviebook.database.FriendsManager;
import com.moviebook.database.RecommendationsManager;

public class MoviesRecommendedFriendsServlet extends HttpServlet {
	private static final Logger log = LogManager.getLogger(MoviesRecommendedFriendsServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MoviesRecommendedFriendsServlet() {
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

		int movie;
		int user;
		String inviteStatus;

		// Check if movie param is passed in
		if (StringUtils.isEmpty(StringUtils.trim(request.getParameter("movie")))) {
			// No movie specified. Fail out
			log.error("No movie specifed when checking for interested users");
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("No movie ID passed");
			return;
		} else {
			// TODO Implement support for semicomma delimited multiple movie ids
			try {
				movie = Integer.parseInt(StringUtils.trim(request.getParameter("movie")));
			} catch (NumberFormatException e) {
				log.error("Invalid movie ID passed", e);
				// Invalid ID, return
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Invalid movie ID passed ");
				return;
			}
		}

		// Check for user next
		if (StringUtils.isEmpty(StringUtils.trim(request.getParameter("user")))) {
			// No user specified. Let's get the current user from session.
			user = ((UserBean) request.getSession(false).getAttribute("currentUserBean")).getId();
		} else {
			// Support only one userID at present.
			// TODO Implement support for semicomma delimited multiple user ids
			try {
				user = Integer.parseInt(StringUtils.trim(request.getParameter("user")));
			} catch (NumberFormatException e) {
				log.error("Invalid userID passed", e);
				// Invalid ID, return
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Invalid user ID passed ");
				return;
			}
		}

		// Check for inviteStatus - default to only accepted friends
		inviteStatus = StringUtils.trim(request.getParameter("inviteStatus"));
		if (StringUtils.isEmpty(inviteStatus))
			inviteStatus = "accepted";

		// Make the actual call
		log.info("Retrieving friends interested in movie " + movie + " for user " + user + " with invite status " + inviteStatus);

		try {
			List<FriendBean> results;
			if (inviteStatus.equals("accepted")) {
				results = RecommendationsManager.getInterestedFriends(user, movie, InviteStatus.ACCEPTED);
			} else if (inviteStatus.equals("sent")) {
				results = RecommendationsManager.getInterestedFriends(user, movie, InviteStatus.SENT);
			} else if (inviteStatus.equals("rejected")) {
				results = RecommendationsManager.getInterestedFriends(user, movie, InviteStatus.REJECTED);
			} else {
				// Bad invite status, throw exception
				log.error("Invalid status " + inviteStatus + " when retrieving friends interested in movie " + movie + " for user " + user);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Invalid status " + inviteStatus + " when retrieving friends interested in movie " + movie + " for user " + user);
				return;
			}

			if ((results == null) || (results.isEmpty())) {
				// No friends found!
				log.info("No friends interested in movie " + movie + " for user " + user);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);

			} else {
				log.info(results.size() + " friends interested in movie " + movie + " for user " + user);

				Gson gs = new Gson();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(gs.toJson(results));
				log.debug(gs.toJson(results));
				return;
			}

		} catch (SQLException e) {
			log.error("Exception encountered retrieving friends interested in movie " + movie + " for user " + user + " with invite status " + inviteStatus, e);
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(
					"Exception encountered retrieving friends interested in movie " + movie + " for user " + user + " with invite status " + inviteStatus);
			return;
		}

	}

}
