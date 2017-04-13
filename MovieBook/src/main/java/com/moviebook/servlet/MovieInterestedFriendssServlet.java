package com.moviebook.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moviebook.bean.UserBean;

/**
 * Servlet implementation class MovieInterestedUsersServlet
 */
public class MovieInterestedFriendssServlet extends HttpServlet {
	private static final Logger log = LogManager.getLogger(MovieInterestedFriendssServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MovieInterestedFriendssServlet() {
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

		// Check if movie param is passed in
		int movie;
		int user;
		if (StringUtils.isEmpty(StringUtils.trim(request.getParameter("movie")))) {
			// No movie specified. Fail out
			log.error("No movie specifed when checking for interested users");
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("No movie ID passed ");
			return;

		} else {
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

		}
		
		// Make the actual call

	}

}
