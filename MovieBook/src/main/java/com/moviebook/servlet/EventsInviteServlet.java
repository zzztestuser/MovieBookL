package com.moviebook.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EventsInviteServlet
 */
public class EventsInviteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EventsInviteServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Check authentication
		if ((request.getSession(false) == null) || (request.getSession(false).getAttribute("currentUserBean") == null)) {
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Unauthorized access");
			return;
		}
		
		// Placeholder
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		return;

	}

}
