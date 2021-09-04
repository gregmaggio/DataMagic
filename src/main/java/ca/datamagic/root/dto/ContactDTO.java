/**
 * 
 */
package ca.datamagic.root.dto;

/**
 * @author Greg
 *
 */
public class ContactDTO {
	private String mailFrom = null;
	private String mailSubject = null;
	private String mailBody = null;
	private String recaptchaResponse = null;
	
	public String getMailFrom() {
		return this.mailFrom;
	}
	public void setMailFrom(String newVal) {
		this.mailFrom = newVal;
	}
	public String getMailSubject() {
		return this.mailSubject;
	}
	public void setMailSubject(String newVal) {
		this.mailSubject = newVal;
	}
	public String getMailBody() {
		return this.mailBody;
	}
	public void setMailBody(String newVal) {
		this.mailBody = newVal;
	}
	public String getRecaptchaResponse() {
		return this.recaptchaResponse;
	}
	public void setRecaptchaResponse(String newVal) {
		this.recaptchaResponse = newVal;
	}
}
