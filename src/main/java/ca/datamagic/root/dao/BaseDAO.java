/**
 * 
 */
package ca.datamagic.root.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.datamagic.root.util.IOUtils;

/**
 * @author Greg
 *
 */
public class BaseDAO {
	private static Logger logger = LogManager.getLogger(BaseDAO.class);
	private static String dataPath = "C:/Dev/Applications/DataMagic/src/main/resources";
	private Properties properties = null;
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Throwable t) {
			logger.error("Throwable", t);
		}
	}
	
	public static String getDataPath() {
		return dataPath;
	}
	
	public static void setDataPath(String newVal) {
		dataPath = newVal;
	}
    
	public Properties getProperties() throws IOException {
		if (this.properties != null) {
			return this.properties;
		}
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(MessageFormat.format("{0}/secure.properties", getDataPath()));
			this.properties = new Properties();
			this.properties.load(inputStream);
			return this.properties;
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	protected static void close(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Throwable t) {
			logger.warn("Throwable", t);
		}
	}
	
	protected static void close(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (Throwable t) {
			logger.warn("Throwable", t);
		}
	}
	
	protected static void close(ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (Throwable t) {
			logger.warn("Throwable", t);
		}
	}
}
