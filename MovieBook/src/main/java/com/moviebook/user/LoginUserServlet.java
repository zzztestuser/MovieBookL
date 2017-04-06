package com.moviebook.user;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
import com.moviebook.database.InvalidUserException;
import com.moviebook.database.UserManager;

/**
 * Servlet implementation class UserLoginServlet
 */
public class LoginUserServlet extends HttpServlet {

	private static final Logger log = LogManager.getLogger(LoginUserServlet.class);

	public LoginUserServlet() {
		super();
	}

	/**
	 * Logout functionality. Invalid current session
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log.info("Session invalidation called!");
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		response.setStatus(HttpServletResponse.SC_OK);

	}

	/**
	 * @return JSON containing user details of user
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String user = StringUtils.trim(request.getParameter("username"));
		String password = StringUtils.trim(request.getParameter("password"));

		// Check if user is already in session
		User currentUser = (User) request.getSession().getAttribute("currentUserBean");
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

				User userDetails = UserManager.getUserByEmail(user);

				request.getSession().setAttribute("currentUserBean", userDetails);

				returnJson(response, userDetails);
				return;

			} else {
				log.info("User " + user + " failed to authenicate.");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			}

		} catch (SQLException e) {
			log.error("Exception occurred when checking user login!", e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (InvalidUserException e) {
			log.error("Invalid user requested!", e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}

	}

	private void returnJson(HttpServletResponse response, User user) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(user.toJson());
		log.debug(user.toJson());
	}

}
