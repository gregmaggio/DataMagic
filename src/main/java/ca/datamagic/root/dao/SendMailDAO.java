/**
 * 
 */
package ca.datamagic.root.dao;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Greg
 *
 */
public class SendMailDAO {
	public static void sendMessage(String mailFrom, String mailTo, String mailSubject, String mailBody) throws AddressException, MessagingException {
		InternetAddress[] mailToAddresses = InternetAddress.parse(mailTo);
		if (mailToAddresses.length < 1) {
			throw new AddressException("Could not parse " + mailTo + " into internet mail addresses.");
		}
		InternetAddress[] mailFromAddresses = InternetAddress.parse(mailFrom);
		if (mailFromAddresses.length < 1) {
			throw new AddressException("Could not parse " + mailFrom + " into internet mail addresses.");
		}
		InternetAddress mailFromAddress = mailFromAddresses[0];
		Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        Session session = Session.getInstance(props);
        Message message = new MimeMessage(session);
        message.setFrom(mailFromAddress);
        message.setRecipients(Message.RecipientType.TO, mailToAddresses);
        message.setSubject(mailSubject);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(mailBody);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        Transport.send(message);
	}
}
