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
import com.moviebook.bean.MovieBean;
import com.moviebook.bean.UserBean;
import com.moviebook.database.RecommendationsManager;

/**
 * Servlet implementation class RecommendedMoviesServlet
 */
public class RecommendedMoviesServlet extends HttpServlet {

	private static final Logger log = LogManager.getLogger(RecommendedMoviesServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RecommendedMoviesServlet() {
		super();
	}

	/**
	 * Retrives the list of recommended movies Supports one parameter user=<id>
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
			log.info("Retrieving recommended movies for user " + user);
			List<MovieBean> rcMovies = RecommendationsManager.getRecommendedMovies(user);
			if ((rcMovies == null) || (rcMovies.isEmpty())) {
				log.info("No recommended movies for user " + user);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				return;
			} else {
				log.info(rcMovies.size() + " recommended movies for user " + user);
				returnJson(response, rcMovies);
			}

		} catch (SQLException e) {
			log.error("Exception encountered retrieving recommended movies for user " + user, e);
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Exception encountered retrieving recommended movies for user " + user);
			return;
		}

	}

	private void returnJson(HttpServletResponse response, List<MovieBean> movies) throws IOException {

		Gson gs = new Gson();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(gs.toJson(movies));
		log.debug("JSON output for movies:");
		log.debug(gs.toJson(movies));

	}

}
