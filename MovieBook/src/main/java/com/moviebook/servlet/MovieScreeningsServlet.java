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
import com.moviebook.bean.ScreeningBean;
import com.moviebook.database.ScreeningsManager;

/**
 * Servlet implementation class MovieScreeningsServlet
 */
public class MovieScreeningsServlet extends HttpServlet {

	private static final Logger log = LogManager.getLogger(MovieScreeningsServlet.class);

	public MovieScreeningsServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Check authentication
		if ((request.getSession(false) == null) || (request.getSession(false).getAttribute("currentUserBean") == null)) {
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Unauthorized access");
			return;
		}

		// Check for movie parameter
		int movieID;

		if (StringUtils.isEmpty(StringUtils.trim(request.getParameter("movie")))) {
			// No movie passed in, invalid request!
			log.error("No movie parameter passed");
			// Invalid ID, return
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("No movie parameter passed");
			return;
		} else {
			try {
				movieID = Integer.parseInt(StringUtils.trim(request.getParameter("movie")));
			} catch (NumberFormatException e) {
				log.error("Invalid moveID passed", e);
				// Invalid ID, return
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Invalid movie ID passed ");
				return;
			}

		}

		try {
			log.info("Getting screening locations for movie " + movieID);
			List<ScreeningBean> screenings = ScreeningsManager.getScreenings(movieID);
			if ((screenings == null) || (screenings.isEmpty())) {
				log.info("No screenings for movie " + movieID);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				return;
			} else {
				log.info(screenings.size() + " screenings for movie " + movieID);
				Gson gs = new Gson();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(gs.toJson(screenings));
				log.debug("JSON output for screenings:");
				log.debug(gs.toJson(screenings));

			}

		} catch (SQLException e) {
			log.error("Exception encountered retrieving screenings for movie " + movieID, e);
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Exception encountered retrieving screenings for movie " + movieID);
			return;
		}

	}

}
