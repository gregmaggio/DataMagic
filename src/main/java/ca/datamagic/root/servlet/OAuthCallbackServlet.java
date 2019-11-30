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

import ca.datamagic.root.dao.AdministratorDAO;
import ca.datamagic.root.dao.LoginSessionDAO;
import ca.datamagic.root.dao.OAuthDAO;
import ca.datamagic.root.dto.AdministratorDTO;
import ca.datamagic.root.dto.LoginSessionDTO;
import ca.datamagic.root.util.UTCUtils;

/**
 * @author Greg
 *
 */
public class OAuthCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(OAuthCallbackServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("code");
		String error = request.getParameter("error");
		String state = request.getParameter("state");
		logger.debug("code: " + code);
		logger.debug("error: " + error);
		logger.debug("state: " + state);
		
		OAuthDAO dao = new OAuthDAO();
		String accessToken = dao.getAccessToken(dao.getClientID(), dao.getClientSecret(), dao.getRedirectUri(), code);
		logger.debug("accessToken: " + accessToken);
		
		String email = dao.getEmail(accessToken);
		logger.debug("email: " + email);
		
		String ipAddress = request.getRemoteAddr();
		logger.debug("ipAddress: " + ipAddress);
		
		try {
			AdministratorDAO administratorDAO = new AdministratorDAO();
			AdministratorDTO administrator = administratorDAO.read(email);
			String expireDate = administrator.getExpireDate();
			if ((expireDate != null) && (expireDate.length() > 0)) {
				String utcNow = UTCUtils.getTimeStamp();
				if (utcNow.compareToIgnoreCase(expireDate) > 0) {
					// Expired
				}
			}
			LoginSessionDAO loginSessionDAO = new LoginSessionDAO();
			String sessionID = loginSessionDAO.create(email, ipAddress);
			LoginSessionDTO loginSession = loginSessionDAO.read(sessionID);
			Cookie cookie = new Cookie(dao.getCookieName(), loginSession.getSessionID());
			cookie.setDomain(dao.getDomain());
			cookie.setSecure(dao.isSecure());
			cookie.setPath(dao.getPath());
			cookie.setHttpOnly(dao.isHttpOnly());
			cookie.setMaxAge(dao.getMaxAge());
			response.addCookie(cookie);
			
			String html = dao.getHTML(sessionID, state);
			response.getWriter().print(html);
			response.getWriter().flush();
		} catch (SQLException ex) {
			logger.error("SQLException", ex);
			throw new ServletException(ex.getMessage());
		}
	}
}
