/**
 * 
 */
package ca.datamagic.root.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ca.datamagic.root.dto.LoginSessionDTO;
import ca.datamagic.root.util.UTCUtils;

/**
 * @author Greg
 *
 */
public class LoginSessionDAO extends BaseDAO {
	private static Logger logger = LogManager.getLogger(LoginSessionDAO.class);
	private String database = null;
	private String connnectionString = null;
	
	public LoginSessionDAO() {
		this.database = MessageFormat.format("{0}/loginsessions.db", getDataPath());
		this.connnectionString = MessageFormat.format("jdbc:sqlite:{0}", this.database);
	}
	
	public String create(String userName, String ipAddress) throws SQLException {
		logger.debug("userName: " + userName);
		logger.debug("ipAddress: " + ipAddress);
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			String sessionID = UUID.randomUUID().toString().toUpperCase();
			logger.debug("sessionID: " + sessionID);
			String createdDate = UTCUtils.getTimeStamp();
			logger.debug("createdDate: " + createdDate);
			Calendar expireDateCalendar = Calendar.getInstance();
			expireDateCalendar.add(Calendar.HOUR_OF_DAY, 2);
			String expireDate = UTCUtils.getTimeStamp(expireDateCalendar.getTime());
			logger.debug("expireDate: " + expireDate);
			connection = DriverManager.getConnection(this.connnectionString);
			statement = connection.prepareStatement("INSERT INTO loginsession (session_id, username, ip_address, created_date, expire_date) VALUES (?,?,?,?,?)");
			statement.setString(1, sessionID);
			statement.setString(2, userName);
			statement.setString(3, ipAddress);
			statement.setString(4, createdDate);
			statement.setString(5, expireDate);
			int affectedRecords = statement.executeUpdate();
			logger.debug("affectedRecords: " + affectedRecords);
			return sessionID;
		} finally {
			if (statement != null) {
				close(statement);
			}
			if (connection != null) {
				close(connection);
			}
		}
	}
	
	public LoginSessionDTO read(String sessionID) throws SQLException {
		logger.debug("sessionID: " + sessionID);
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DriverManager.getConnection(this.connnectionString);
			statement = connection.prepareStatement("SELECT * FROM loginsession WHERE session_id = ?");
			statement.setString(1, sessionID);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return new LoginSessionDTO(resultSet);
			}
			return null;
		} finally {
			if (resultSet != null) {
				close(resultSet);
			}
			if (statement != null) {
				close(statement);
			}
			if (connection != null) {
				close(connection);
			}
		}
	}
	
	public void delete(String sessionID) throws SQLException {
		logger.debug("sessionID: " + sessionID);
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DriverManager.getConnection(this.connnectionString);
			statement = connection.prepareStatement("DELETE FROM loginsession WHERE session_id = ?");
			statement.setString(1, sessionID);
			int affectedRecords = statement.executeUpdate();
			logger.debug("affectedRecords: " + affectedRecords);
		} finally {
			if (statement != null) {
				close(statement);
			}
			if (connection != null) {
				close(connection);
			}
		}
	}
}
