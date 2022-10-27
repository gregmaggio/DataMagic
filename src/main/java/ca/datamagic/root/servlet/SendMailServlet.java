/**
 * 
 */
package ca.datamagic.root.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import ca.datamagic.root.dao.SendMailDAO;
import ca.datamagic.root.dto.MessageDTO;
import ca.datamagic.root.util.IOUtils;

/**
 * @author gregm
 *
 */
public class SendMailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(SendMailServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String contentType = request.getContentType();
			logger.debug("contentType: " + contentType);
			
			String json = IOUtils.readEntireStream(request.getInputStream());
			logger.debug("json: " + json);
			
			Gson gson = new Gson();
			MessageDTO dto = gson.fromJson(json, MessageDTO.class);
			
			SendMailDAO.sendMessage(dto.getMailFrom(), dto.getMailTo(), dto.getMailCC(), dto.getMailBCC(), dto.getMailSubject(), dto.getMailBody(), dto.getContentType());
		} catch (Throwable t) {
			logger.error("Exception", t);
			throw new ServletException(t);
		}
	}
}
