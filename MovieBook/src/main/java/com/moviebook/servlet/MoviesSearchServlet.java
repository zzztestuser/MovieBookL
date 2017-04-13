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
import com.moviebook.database.MoviesManager;

/**
 * Servlet implementation class MoviesSearchServlet
 */
public class MoviesSearchServlet extends HttpServlet {
	private static final Logger log = LogManager.getLogger(MoviesSearchServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MoviesSearchServlet() {
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

		// Check for parameters
		String title = StringUtils.trim(request.getParameter("title"));

		log.info("Searching for movie with title " + title);

		if (StringUtils.isEmpty(title)) {
			log.error("No parameter passed when searching for movie");
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("No parameter passed when searching for movie");
			return;
		}

		try {
			List<MovieBean> movies = MoviesManager.findMovieByTitle(title);

			if (movies == null || movies.isEmpty()) {
				log.info("No movies found");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("No movies found");

			} else {
				log.info(movies.size() + " movies found in search.");
				Gson gs = new Gson();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(gs.toJson(movies));
				return;
			}

		} catch (SQLException e) {
			log.error("Exception encountered when searching for movies.", e);
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Exception encountered when searching for movies.");
			return;
		}
	}

}
