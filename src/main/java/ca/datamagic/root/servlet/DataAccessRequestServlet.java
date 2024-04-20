/**
 * 
 */
package ca.datamagic.root.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.datamagic.root.dao.SendMailDAO;

/**
 * @author gregm
 *
 */
public class DataAccessRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(DataAccessRequestServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String name = request.getParameter("name");
			if ((name == null) || (name.length() < 1)) {
				throw new ServletException("name paramter is required");
			}
			String email = request.getParameter("email");
			if ((email == null) || (email.length() < 1)) {
				throw new ServletException("email paramter is required");
			}
			String request_as = request.getParameter("request_as");
			if ((request_as == null) || (request_as.length() < 1)) {
				throw new ServletException("request_as paramter is required");
			}
			String compliance_type = request.getParameter("compliance_type");
			if ((compliance_type == null) || (compliance_type.length() < 1)) {
				throw new ServletException("compliance_type paramter is required");
			}
			String action = request.getParameter("action");
			if ((action == null) || (action.length() < 1)) {
				throw new ServletException("action paramter is required");
			}
			String content = request.getParameter("content");
			String declare_information = request.getParameter("declare_information");
			if ((declare_information == null) || (declare_information.length() < 1)) {
				throw new ServletException("declare_information paramter is required");
			}
			String understand_deletion = request.getParameter("understand_deletion");
			if ((understand_deletion == null) || (understand_deletion.length() < 1)) {
				throw new ServletException("understand_deletion paramter is required");
			}
			String validate_email = request.getParameter("validate_email");
			if ((validate_email == null) || (validate_email.length() < 1)) {
				throw new ServletException("validate_email paramter is required");
			}
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			printWriter.println("Name: " + name);
			printWriter.println("Email: " + email);
			printWriter.println("Request As: " + request_as);
			printWriter.println("Compliance Type: " + compliance_type);
			printWriter.println("Request To: " + action);
			if ((content != null) && (content.length() > 0)) {
				printWriter.println("Request Details: " + content);
			}
			if (declare_information.compareToIgnoreCase("true") == 0) {
				printWriter.println("Under penalty of perjury, I declare all the above information to be true and accurate");
			}
			if (understand_deletion.compareToIgnoreCase("true") == 0) {
				printWriter.println("I understand that the deletion or restriction of my personal data is irreversible");
			}
			if (validate_email.compareToIgnoreCase("true") == 0) {
				printWriter.println("I understand that I will be required to validate my request by email, and I may be contacted in order to complete the request");
			}
			printWriter.flush();			
			String mailBody = writer.toString();
			logger.debug(mailBody);
			printWriter.close();
			writer.close();
			SendMailDAO.sendMessage(email, "gregorymaggio@gmail.com", null, null, "Data Access Request Form", mailBody, "text/plain");
			response.getWriter().println("<strong>Data Access Request - Someone will get back to you shortly</strong>");
		} catch (Throwable t) {
			logger.error("Exception", t);
			throw new ServletException(t);
		}
	}
}
