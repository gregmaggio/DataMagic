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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import ca.datamagic.root.dao.LoginSessionDAO;
import ca.datamagic.root.dao.OAuthDAO;
import ca.datamagic.root.dto.LoginSessionDTO;
import ca.datamagic.root.util.UTCUtils;

/**
 * @author Greg
 *
 */
public class WhoAmIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(WhoAmIServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String origin = request.getHeader("Origin");
		logger.debug("origin: " + origin);
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
			LoginSessionDTO loginSession = loginSessionDAO.read(sessionID);
			String now = UTCUtils.getTimeStamp();
			if (now.compareToIgnoreCase(loginSession.getExpireDate()) > 0) {
				response.sendError(401);
			} else {
				response.setContentType("application/json");
				response.getWriter().print((new Gson()).toJson(loginSession.getUserName()));
			}
		} catch (SQLException ex) {
			logger.error("SQLException", ex);
			throw new ServletException(ex.getMessage());
		}
	}
}
