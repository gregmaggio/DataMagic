/**
 * 
 */
package ca.datamagic.root.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Greg
 *
 */
public class LoginSessionDTO {
	private String sessionID = null;
	private String userName = null;
	private String ipAddress = null;
	private String createdDate = null;
	private String expireDate = null;
	
	public LoginSessionDTO() {
		
	}
	
	public LoginSessionDTO(ResultSet resultSet) throws SQLException {
		this.sessionID = resultSet.getString("session_id");
		this.userName = resultSet.getString("username");
		this.ipAddress = resultSet.getString("ip_address");
		this.createdDate = resultSet.getString("created_date");
		this.expireDate = resultSet.getString("expire_date");
	}

	public String getSessionID() {
		return this.sessionID;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getIPAddress() {
		return this.ipAddress;
	}
	
	public String getCreatedDate() {
		return this.createdDate;
	}
	
	public String getExpireDate() {
		return this.expireDate;
	}
	
	public void setSessionID(String newVal) {
		this.sessionID = newVal;
	}
	
	public void setUserName(String newVal) {
		this.userName = newVal;
	}
	
	public void setIPAddress(String newVal) {
		this.ipAddress = newVal;
	}
	
	public void setCreatedDate(String newVal) {
		this.createdDate = newVal;
	}
	
	public void setExpireDate(String newVal) {
		this.expireDate = newVal;
	}
}
