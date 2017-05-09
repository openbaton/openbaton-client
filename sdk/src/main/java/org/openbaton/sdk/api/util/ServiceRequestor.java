package org.openbaton.sdk.api.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.openbaton.catalogue.nfvo.ServiceCredentials;
import org.openbaton.sdk.api.exception.SDKException;
import org.openbaton.utils.key.KeyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides the functionality for sending requests to the NFVO that are related to the
 * de/registration of a service.
 */
public class ServiceRequestor {

  private Gson gson = new Gson();
  private CloseableHttpClient httpClient;
  private String serviceUrl;
  private static final String KEY_FILE_PATH = "/etc/openbaton/service-key";
  private static final String SDK_PROPERTIES_FILE = "sdk.api.properties";
  private PropertyReader propertyReader = new PropertyReader(SDK_PROPERTIES_FILE);
  private Logger log = LoggerFactory.getLogger(this.getClass());

  /**
   * Requestor for registering and deregistering a Service on the NFVO.
   *
   * @param httpClient
   * @param apiUrl
   */
  public ServiceRequestor(CloseableHttpClient httpClient, String apiUrl) {
    this.httpClient = httpClient;
    this.serviceUrl = apiUrl + propertyReader.getRestUrl("Service");
  }

  /**
   * Send a request to the NFVO for registering a Service.
   *
   * @param serviceName
   * @return
   * @throws IllegalBlockSizeException
   * @throws InvalidKeyException
   * @throws BadPaddingException
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws IOException
   * @throws SDKException
   */
  protected String requestRegisterService(String serviceName)
      throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
          NoSuchAlgorithmException, NoSuchPaddingException, IOException, SDKException {

    log.debug("Registering Service " + serviceName);
    CloseableHttpResponse response = null;
    HttpPost httpPost = new HttpPost(this.serviceUrl + "/register");
    httpPost.setHeader(new BasicHeader("accept", "application/json"));
    httpPost.setHeader(new BasicHeader("Content-Type", "application/octet-stream"));
    byte[] key_data = Files.readAllBytes(Paths.get(KEY_FILE_PATH));
    byte[] encrypted =
        KeyHelper.encrypt(
            "{\"name\":\"" + serviceName + "\",\"action\":\"register\"}",
            KeyHelper.restoreKey(key_data));
    httpPost.setEntity(new ByteArrayEntity(encrypted));


    log.debug("Post: " + httpPost.getURI());
    response = httpClient.execute(httpPost);
    RestUtils.checkStatus(response, HttpURLConnection.HTTP_CREATED);
    String result = "";
    if (response.getEntity() != null) result = EntityUtils.toString(response.getEntity());
    response.close();
    httpPost.releaseConnection();
    ServiceCredentials serviceCredentials = gson.fromJson(result, ServiceCredentials.class);
    String encryptedToken = serviceCredentials.getToken();
    String decryptedToken = KeyHelper.decrypt(encryptedToken, KeyHelper.restoreKey(key_data));
    log.trace("Token is: " + decryptedToken);
    return decryptedToken;
  }
}
