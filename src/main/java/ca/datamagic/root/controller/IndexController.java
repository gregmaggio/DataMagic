/**
 * 
 */
package ca.datamagic.root.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import ca.datamagic.root.dto.SwaggerConfigurationDTO;
import ca.datamagic.root.dto.SwaggerResourceDTO;

/**
 * @author Greg
 *
 */
@Controller
@RequestMapping("")
public class IndexController {
	private static Logger _logger = LogManager.getLogger(IndexController.class);
	private static String _tempPath = null; 
	private static SwaggerConfigurationDTO _swaggerConfiguration = null;
	private static SwaggerResourceDTO[] _swaggerResources = null;
	private static String _swagger = null;
	
	static {
		FileInputStream swaggerStream = null;
		try {
			DefaultResourceLoader loader = new DefaultResourceLoader();
			Resource tempResource = loader.getResource("classpath:temp");
		    Resource metaInfResource = loader.getResource("META-INF");
		    _tempPath = tempResource.getFile().getAbsolutePath();
		    String metaInfPath = metaInfResource.getFile().getAbsolutePath();
		    _logger.debug("tempPath: " + _tempPath);
		    _logger.debug("metaInfPath: " + metaInfPath);
		    
		    String swaggerFileName = MessageFormat.format("{0}/swagger.json", metaInfPath);
		    swaggerStream = new FileInputStream(swaggerFileName);
		    _swagger = IOUtils.toString(swaggerStream, "UTF-8");
			_swaggerConfiguration = new SwaggerConfigurationDTO();
			_swaggerResources = new SwaggerResourceDTO[] { new SwaggerResourceDTO() };
		} catch (Throwable t) {
			_logger.error("Exception", t);
		}
		if (swaggerStream != null) {
			IOUtils.closeQuietly(swaggerStream);
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="/api/error")
	@ResponseStatus(value = HttpStatus.OK)
	public void sendError(@RequestParam(value = "mailFrom") String mailFrom, @RequestParam(value = "mailSubject") String mailSubject, @RequestParam(value = "mailBody") String mailBody, @RequestParam(value = "attachment", required = false) MultipartFile attachment) {
		_logger.debug("mailFrom: " + mailFrom);
		_logger.debug("mailSubject: " + mailSubject);
		_logger.debug("mailBody: " + mailSubject);
		if (attachment != null) {
			_logger.debug("contentType: " + attachment.getContentType());
			_logger.debug("name: " + attachment.getName());
			_logger.debug("Original Filename: " + attachment.getOriginalFilename());
			_logger.debug("size: " + attachment.getSize());
		}
		File attachmentFile = null;
		OutputStream attachmentStream = null;
        try {
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
            if (attachment != null) {
            	attachmentFile = new File(MessageFormat.format("{0}/{1}", _tempPath, attachment.getOriginalFilename()));
            	if (attachmentFile.exists()) {
            		attachmentFile.delete();
            	}
            	attachmentStream = new FileOutputStream(attachmentFile);
            	IOUtils.write(attachment.getBytes(), attachmentStream);
            	IOUtils.closeQuietly(attachmentStream);
            	attachmentStream = null;
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentFile);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(attachmentFile.getName());
                multipart.addBodyPart(messageBodyPart);
    		}
            message.setContent(multipart);
            Transport.send(message);
        } catch (Throwable t) {
            _logger.error("Exception", t);
        } finally {
        	if (attachmentStream != null) {
        		IOUtils.closeQuietly(attachmentStream);
        	}
        	if ((attachmentFile != null) && (attachmentFile.exists())) {
        		attachmentFile.delete();
        	}
        }
	}
	
	@RequestMapping(value="/swagger-resources/configuration/ui", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public SwaggerConfigurationDTO getSwaggerConfigurationUI() {
		return _swaggerConfiguration;
	}
	
	@RequestMapping(value="/swagger-resources", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public SwaggerResourceDTO[] getSwaggerResources() {
		return _swaggerResources;
	}
	
	@RequestMapping(value="/v2/api-docs", method=RequestMethod.GET, produces="application/json")
	public void getSwagger(Writer responseWriter) throws IOException {
		responseWriter.write(_swagger);
	}
}
