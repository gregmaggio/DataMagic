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
public class AdministratorDTO {
	private String email = null;
	private String createdDate = null;
	private String expireDate = null;
	
	public AdministratorDTO() {
		
	}
	
	public AdministratorDTO(ResultSet resultSet) throws SQLException {
		this.email = resultSet.getString("email");
		this.createdDate = resultSet.getString("created_date");
		this.expireDate = resultSet.getString("expire_date");
	}

	public String getEmail() {
		return this.email;
	}
	
	public String getCreatedDate() {
		return this.createdDate;
	}
	
	public String getExpireDate() {
		return this.expireDate;
	}
	
	public void setEmail(String newVal) {
		this.email = newVal;
	}
	
	public void setCreatedDate(String newVal) {
		this.createdDate = newVal;
	}
	
	public void setExpireDate(String newVal) {
		this.expireDate = newVal;
	}
}
