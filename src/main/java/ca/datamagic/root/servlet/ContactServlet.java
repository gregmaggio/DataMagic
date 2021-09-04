/**
 * 
 */
package ca.datamagic.root.servlet;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import ca.datamagic.root.dao.RecaptchaDAO;
import ca.datamagic.root.dao.SendMailDAO;
import ca.datamagic.root.dto.ContactDTO;
import ca.datamagic.root.dto.ContactResponseDTO;

/**
 * @author Greg
 *
 */
public class ContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(ContactServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		ContactResponseDTO responseDTO = new ContactResponseDTO();
		responseDTO.setInvalidFields("");
		responseDTO.setCaptchaValid(false);
		responseDTO.setSent(false);
		try {
			ContactDTO dto = gson.fromJson(new InputStreamReader(request.getInputStream()), ContactDTO.class);
			StringBuilder invalidFields = new StringBuilder();
			if ((dto.getMailFrom() == null) || (dto.getMailFrom().length() < 1)) {
				if (invalidFields.length() > 0) {
					invalidFields.append(",");
				}
				invalidFields.append("mailFrom");
			}
			if ((dto.getMailSubject() == null) || (dto.getMailSubject().length() < 1)) {
				if (invalidFields.length() > 0) {
					invalidFields.append(",");
				}
				invalidFields.append("mailSubject");
			}
			if ((dto.getMailBody() == null) || (dto.getMailBody().length() < 1)) {
				if (invalidFields.length() > 0) {
					invalidFields.append(",");
				}
				invalidFields.append("mailBody");
			}
			responseDTO.setInvalidFields(invalidFields.toString());
			if ((responseDTO.getInvalidFields() == null) || (responseDTO.getInvalidFields().length() < 1)) {
				if ((dto.getRecaptchaResponse() != null) && (dto.getRecaptchaResponse().length() > 0)) {
					RecaptchaDAO dao = new RecaptchaDAO();
					boolean success = dao.validate(dto.getRecaptchaResponse(), request.getRemoteAddr());
					responseDTO.setCaptchaValid(success);
					if (success) {
						SendMailDAO.sendMessage(dto.getMailFrom(), "gregorymaggio@gmail.com", dto.getMailSubject(), dto.getMailBody());
						responseDTO.setSent(true);
					}
				}
			}
		} catch (Throwable t) {
			logger.error("Exception", t);			
		}
		response.setContentType("application/json");
		response.getWriter().print(gson.toJson(responseDTO));
	}
}
