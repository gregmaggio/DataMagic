/**
 * 
 */
package ca.datamagic.root.dto;

/**
 * @author Greg
 *
 */
public class ContactResponseDTO {	
	private String invalidFields = null;
	private Boolean captchaValid = null;
	private Boolean sent = null;

	public String getInvalidFields() {
		return this.invalidFields;
	}
	public void setInvalidFields(String newVal) {
		this.invalidFields = newVal;
	}
	public Boolean getCaptchaValid() {
		return this.captchaValid;
	}
	public void setCaptchaValid(Boolean newVal) {
		this.captchaValid = newVal;
	}
	public Boolean getSent() {
		return this.sent;
	}
	public void setSent(Boolean newVal) {
		this.sent = newVal;
	}	
}
