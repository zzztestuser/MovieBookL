package com.moviebook.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.moviebook.bean.user.UserBean;
import com.moviebook.database.InvalidUserException;
import com.moviebook.database.UserManager;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final Logger log = LogManager.getLogger(UserServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
	}

	/**
	 * Retrieves details on a user given user's ID. Following syntaxes are supported
	 * 
	 * api/user/<id(s)> - Retrieve user details with given ID. For multiple, separate with semi-colon
	 * 
	 * api/user/email/<email> - Retrieve user with given ID
	 * 
	 * 
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

		log.debug("Path info to UserServlet is " + request.getPathInfo());

		// Parameterize the URL and delegate accordingly. Remember that params will always be at least length 1
		String[] params = request.getPathInfo().split("/");

		if (params.length == 2) {
			// Only one parameter passed in, should be api/user/id

		} else if (params.length == 3) {
			// Check if the first parameter is an integer to see if's more detailed
			if (NumberUtils.isDigits(params[1])) {
				// First param refers to ID
				// Second param onwards refers to fields requested

				int id = Integer.parseInt(params[1]);

				switch (StringUtils.trim(params[2]).toLowerCase()) {
				case "friends":
					log.info("Retrieving friends list for id " + id);
					try {
						getFriends(id, response);
					} catch (SQLException | IOException e) {
						log.error("Error occurred when retrieving friends list for ID " + id, e);
					}
					break;
				}

			} else {

			}
		}

	}

	private void getFriends(int userID, HttpServletResponse response) throws SQLException, IOException {

		if (UserManager.isUserExistsById(userID)) {
			List<UserBean> results = UserManager.getUserFriendsById(userID);

			if ((results == null) || (results.isEmpty())) {
				// No friends found!
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);

			} else {
				Gson gs = new Gson();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(gs.toJson(results));
				log.debug(gs.toJson(results));
				return;
			}

		} else {
			// No such user!
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("No such user ID");
		}

	}

}
