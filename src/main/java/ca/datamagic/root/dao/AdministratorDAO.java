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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.datamagic.root.dto.AdministratorDTO;

/**
 * @author Greg
 *
 */
public class AdministratorDAO extends BaseDAO {
	private static Logger logger = LogManager.getLogger(AdministratorDAO.class);
	private String database = null;
	private String connnectionString = null;
	
	public AdministratorDAO() {
		this.database = MessageFormat.format("{0}/administrators.db", getDataPath());
		this.connnectionString = MessageFormat.format("jdbc:sqlite:{0}", this.database);
	}
	
	public AdministratorDTO read(String email) throws SQLException {
		logger.debug("email: " + email);
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DriverManager.getConnection(this.connnectionString);
			statement = connection.prepareStatement("SELECT * FROM administrator WHERE email = ?");
			statement.setString(1, email);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return new AdministratorDTO(resultSet);
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
}
