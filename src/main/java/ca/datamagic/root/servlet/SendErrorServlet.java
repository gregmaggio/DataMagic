/**
 * 
 */
package ca.datamagic.root.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author Greg
 *
 */
public class SendErrorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(SendErrorServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream inputStream = null;
		try {
			String mailFrom = null;
			String mailSubject = null;
			String mailBody = null;
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				ServletFileUpload upload = new ServletFileUpload();
				FileItemIterator iterator = upload.getItemIterator(request);
				while (iterator.hasNext()) {
					FileItemStream item = iterator.next();					
					if (item.isFormField()) {
						String name = item.getFieldName();
						inputStream = item.openStream();
						String value = Streams.asString(inputStream);
						if (name.compareToIgnoreCase("mailFrom") == 0) {
							mailFrom = value;
						} else if (name.compareToIgnoreCase("mailSubject") == 0) {
							mailSubject = value;
						} else if (name.compareToIgnoreCase("mailBody") == 0) {
							mailBody = value;
						}
						inputStream.close();
						inputStream = null;
					}
				}
			} else {
				Enumeration<String> parameterNames = request.getParameterNames();
				while (parameterNames.hasMoreElements()) {
					String parameterName = parameterNames.nextElement();
					logger.debug("parameterName: " + parameterName);
					if (parameterName.compareToIgnoreCase("mailFrom") == 0) {
						mailFrom = request.getParameter(parameterName);
					} else if (parameterName.compareToIgnoreCase("mailSubject") == 0) {
						mailSubject = request.getParameter(parameterName);
					} else if (parameterName.compareToIgnoreCase("mailBody") == 0) {
						mailBody = request.getParameter(parameterName);
					}				
				}
			}
			logger.debug("mailFrom: " + mailFrom);
			logger.debug("mailSubject: " + mailSubject);
			logger.debug("mailBody: " + mailBody);
        	Properties props = new Properties();
            props.put("mail.smtp.host", "localhost");
            Session session = Session.getInstance(props);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("gregorymaggio@gmail.com"));
            message.setSubject(mailSubject);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mailBody);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);
		} catch (Throwable t) {
			logger.error("Exception", t);
			throw new ServletException(t);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Throwable t) {
					logger.warn("Exception", t);
				}
			}
		}
	}
}
