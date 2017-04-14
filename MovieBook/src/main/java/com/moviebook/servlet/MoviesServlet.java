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

import com.google.gson.Gson;
import com.moviebook.bean.MovieBean;
import com.moviebook.database.MoviesManager;

/**
 * Servlet implementation class MoviesServlet
 */
public class MoviesServlet extends HttpServlet {
	private static final Logger log = LogManager.getLogger(MoviesServlet.class);

	public MoviesServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Check authentication
		if ((request.getSession(false) == null) || (request.getSession(false).getAttribute("currentUserBean") == null)) {
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Unauthorized access");
			return;
		}

		// Check for parameters
		int id;
		if (StringUtils.isEmpty(request.getParameter("id"))) {
			log.error("No parameter passed when retrieving movie");
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("No parameter passed when retrieving movie");
			return;
		} else {
			try {
				id = Integer.parseInt(StringUtils.trim(request.getParameter("id")));
			} catch (NumberFormatException e) {
				log.error("Invalid movie ID passed", e);
				// Invalid ID, return
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Invalid movie ID passed ");
				return;
			}
		}

		log.info("Searching for movie with id " + id);

		try {
			MovieBean mv = MoviesManager.getMovie(id);
			
			if (mv == null) {
				log.info("No movie with ID " + id);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("No movie with ID " + id);
				return;
			} else {

				Gson gs = new Gson();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(gs.toJson(mv));
				log.debug(gs.toJson(mv));
				return;
			}
			
		} catch (SQLException e) {
			log.error("Exception encountered retrieving movie ID " + id, e);
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Exception encountered retrieving movie ID " + id);
			return;
		}

	}

}
