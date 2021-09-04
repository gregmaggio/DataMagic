/**
 * 
 */
package ca.datamagic.root.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.datamagic.root.util.IOUtils;

/**
 * @author Greg
 *
 */
public class OAuthDAO extends BaseDAO {
	private static Logger logger = LogManager.getLogger(OAuthDAO.class);
	
	public String getClientID() throws IOException {
		return getProperties().getProperty("clientID");
	}
	
	public String getClientSecret() throws IOException {
		return getProperties().getProperty("clientSecret");
	}
	
	public String getRedirectUri() throws IOException {
		return getProperties().getProperty("redirectUri");
	}
	
	public String getCookieName() throws IOException {
		return getProperties().getProperty("cookieName");
	}
	
	public String getDomain() throws IOException {
		return getProperties().getProperty("domain");
	}
	
	public boolean isSecure() throws IOException {
		return Boolean.parseBoolean(getProperties().getProperty("secure"));
	}
	
	public boolean isHttpOnly() throws IOException {
		return Boolean.parseBoolean(getProperties().getProperty("httpOnly"));
	}
	
	public int getMaxAge() throws IOException {
		return Integer.parseInt(getProperties().getProperty("maxAge"));
	}
	
	public String getPath() throws IOException {
		return getProperties().getProperty("path");
	}
	
	public String getAccessToken(String clientID, String clientSecret, String redirectUri, String code) throws IOException {
		URL url = new URL("https://accounts.google.com/o/oauth2/token");
        logger.debug("url: " + url.toString());        
        StringBuilder postData = new StringBuilder();
        postData.append("code=" + code);
        postData.append("&client_id=" + clientID);
        postData.append("&client_secret=" + clientSecret);
        postData.append("&redirect_uri=" + redirectUri);
        postData.append("&grant_type=authorization_code");
        logger.debug("postData: " + postData.toString());        
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(2000);
            connection.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
            connection.connect();
            IOUtils.writeToStream(connection.getOutputStream(), postData.toString());
            String responseText = IOUtils.readEntireStream(connection.getInputStream());
            logger.debug("responseText: " + responseText);
            JSONObject jsonObject = new JSONObject(responseText);
            String error = jsonObject.optString("error");
            if ((error != null) && (error.length() > 0)) {
            	throw new IOException(error);
            }
            String accessToken = jsonObject.optString("access_token");
            if ((accessToken == null) || (accessToken.length() < 1)) {
            	throw new IOException("No access token");
            }
            return accessToken;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Throwable t) {
                	logger.warn("Throwable", t);
                }
            }
        }
	}
	
	public String getEmail(String accessToken) throws IOException {
		String urlSpec = MessageFormat.format("https://www.googleapis.com/oauth2/v1/tokeninfo?access_token={0}", accessToken);
		URL url = new URL(urlSpec);
        logger.debug("url: " + url.toString());  
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.connect();
            String responseText = IOUtils.readEntireStream(connection.getInputStream());
            logger.debug("responseText: " + responseText);
            JSONObject jsonObject = new JSONObject(responseText);
            String error = jsonObject.optString("error");
            if ((error != null) && (error.length() > 0)) {
            	throw new IOException(error);
            }
            String email = jsonObject.optString("email");
            if ((email == null) || (email.length() < 1)) {
            	throw new IOException("No email");
            }
            return email;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Throwable t) {
                	logger.warn("Throwable", t);
                }
            }
        }
	}
	
	public String getHTML(String sessionID, String redirectURL) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(MessageFormat.format("{0}/OAuthCallback.html", getDataPath()));
			String html = IOUtils.readEntireStream(inputStream);
			html = html.replace("{{sessionID}}", sessionID);
			html = html.replace("{{redirectURL}}", redirectURL);
			return html;
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
}
