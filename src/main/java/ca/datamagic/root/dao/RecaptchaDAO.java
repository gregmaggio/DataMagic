/**
 * 
 */
package ca.datamagic.root.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import ca.datamagic.root.dto.RecaptchaDTO;
import ca.datamagic.root.util.IOUtils;


/**
 * @author Greg
 *
 */
public class RecaptchaDAO extends BaseDAO {
	private static final Logger logger = LogManager.getLogger(RecaptchaDAO.class);
	
	public boolean validate(String captchaResponse, String ipAddress) throws IOException {
		String captchaVerifyUrl = getProperties().getProperty("captchaVerifyUrl");
		String captchaSiteKey = getProperties().getProperty("captchaSiteKey");
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	    postParameters.add(new BasicNameValuePair("secret", captchaSiteKey));
	    postParameters.add(new BasicNameValuePair("remoteip", ipAddress));
	    postParameters.add(new BasicNameValuePair("response", captchaResponse));
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(captchaVerifyUrl);
		httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		String responseText = IOUtils.readEntireStream(entity.getContent());
		logger.debug("responseText: " + responseText);
		Gson gson = new Gson();
		RecaptchaDTO dto = gson.fromJson(responseText, RecaptchaDTO.class);
		if (dto.getSuccess() != null) {
			return dto.getSuccess().booleanValue();
		}
		return false;
	}
}
