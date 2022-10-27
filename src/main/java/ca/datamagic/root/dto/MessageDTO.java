/**
 * 
 */
package ca.datamagic.root.dto;

/**
 * @author gregm
 *
 */
public class MessageDTO {
	private String mailTo = null;
	private String mailFrom = null;
	private String mailCC = null;
	private String mailBCC = null;
	private String mailSubject = null;
	private String mailBody = null;
	private String contentType = null;
	
	public String getMailTo() {
		return this.mailTo;
	}
	public void setMailTo(String newVal) {
		this.mailTo = newVal;
	}
	public String getMailFrom() {
		return this.mailFrom;
	}
	public void setMailFrom(String newVal) {
		this.mailFrom = newVal;
	}
	public String getMailCC() {
		return this.mailCC;
	}
	public void setMailCC(String newVal) {
		this.mailCC = newVal;
	}
	public String getMailBCC() {
		return this.mailBCC;
	}
	public void setMailBCC(String newVal) {
		this.mailBCC = newVal;
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
	public String getContentType() {
		return this.contentType;
	}
	public void setContentType(String newVal) {
		this.contentType = newVal;
	}
}
