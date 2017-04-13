package com.moviebook.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.moviebook.bean.user.UserBean;
import com.moviebook.database.InvalidUserException;
import com.moviebook.database.UserManager;

/**
 * Servlet implementation class UserLoginServlet
 */
public class SessionServlet extends HttpServlet {

	private static final Logger log = LogManager.getLogger(SessionServlet.class);

	public SessionServlet() {
		super();
	}

	/**
	 * Logout functionality. Invalidate current session
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log.info("Session invalidation called!");
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		response.setStatus(HttpServletResponse.SC_NO_CONTENT);

	}

	/**
	 * Retrieves current user from session
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {

			UserBean currentUser = (UserBean) request.getSession().getAttribute("currentUserBean");
			if (currentUser != null) {
				returnJson(response, currentUser);
			} else {
				returnUnauthorized(response, "No user currently logged in");
				return;
			}
		} else {
			returnUnauthorized(response, "No user currently logged in");
			return;
		}

	}

	/**
	 * Login funcionality
	 * 
	 * @return JSON containing user details of user
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String user = StringUtils.trim(request.getParameter("username"));
		String password = StringUtils.trim(request.getParameter("password"));

		// Check if user is already in session

		UserBean currentUser = (UserBean) request.getSession().getAttribute("currentUserBean");
		if (currentUser != null) {
			log.debug("currentUserBean already exists in session.");

			// If already exists, check for user match
			if (!currentUser.getEmail().equals(user)) {
				log.warn("currentUserBean does not match with new login attempt!");
				response.setStatus(HttpServletResponse.SC_CONFLICT);
			} else {
				// Return existing user
				returnJson(response, currentUser);
			}

			return;
		}

		try {
			if (UserManager.isUserValid(user, password)) {
				log.info("User " + user + " successfully authenicated.");

				UserBean userDetails = UserManager.getUserByEmail(user);

				request.getSession().setAttribute("currentUserBean", userDetails);
				
				log.info("Returning response!");
				returnJson(response, userDetails);
				

			} else {
				log.info("User " + user + " failed to authenicate.");
				returnUnauthorized(response, "Authenication failure");

			}

		} catch (SQLException e) {
			log.error("Exception occurred when checking user login!", e);
			returnUnauthorized(response, "Exception occurred during authenication!");
		} catch (InvalidUserException e) {
			log.error("Invalid user requested!", e);
			returnUnauthorized(response, "Authenication failure!");
		}

	}

	private void returnJson(HttpServletResponse response, UserBean user) throws IOException {

		Gson gs = new Gson();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(gs.toJson(user));
		log.debug("JSON output for user");
		log.debug(gs.toJson(user));

	}

	private void returnUnauthorized(HttpServletResponse response, String message) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		if (!StringUtils.isEmpty(message))
			response.getWriter().write(message);
	}

}
