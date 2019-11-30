/**
 * 
 */
package ca.datamagic.root.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ca.datamagic.root.dao.LoginSessionDAO;
import ca.datamagic.root.dao.OAuthDAO;

/**
 * @author Greg
 *
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(LogoutServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OAuthDAO dao = new OAuthDAO();
		String sessionID = null;
		Cookie[] cookies = request.getCookies();
		for (int ii = 0; ii < cookies.length; ii++) {
			if (cookies[ii].getName().compareToIgnoreCase(dao.getCookieName()) == 0) {
				sessionID = cookies[ii].getValue();
				break;
			}
		}
		if (sessionID == null) {
			response.sendError(401);
			return;
		}
		try {
			LoginSessionDAO loginSessionDAO = new LoginSessionDAO();
			loginSessionDAO.delete(sessionID);
		} catch (SQLException ex) {
			logger.error("SQLException", ex);
			throw new ServletException(ex.getMessage());
		}
		Cookie cookie = new Cookie(dao.getCookieName(), "DELETED");
		cookie.setDomain(dao.getDomain());
		cookie.setSecure(dao.isSecure());
		cookie.setPath(dao.getPath());
		cookie.setHttpOnly(dao.isHttpOnly());
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
