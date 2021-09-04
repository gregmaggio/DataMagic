/**
 * 
 */
package ca.datamagic.root.dto;

/**
 * @author Greg
 *
 */
public class RecaptchaDTO {
	private Boolean success = null;
	private String challenge_ts = null;
	private String hostname = null;
	
	public Boolean getSuccess() {
		return this.success;
	}
	public void setSuccess(Boolean newVal) {
		this.success = newVal;
	}
	public String getChallenge_ts() {
		return this.challenge_ts;
	}
	public void setChallenge_ts(String newVal) {
		this.challenge_ts = newVal;
	}
	public String getHostname() {
		return this.hostname;
	}
	public void setHostname(String newVal) {
		this.hostname = newVal;
	}
	
}
