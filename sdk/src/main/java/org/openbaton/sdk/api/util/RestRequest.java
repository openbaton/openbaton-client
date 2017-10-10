/*
 * Copyright (c) 2016 Open Baton (http://www.openbaton.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.openbaton.sdk.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.JsonNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.openbaton.catalogue.nfvo.VNFPackage;
import org.openbaton.nfvo.common.utils.key.KeyHelper;
import org.openbaton.sdk.api.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** OpenBaton api request abstraction for all requester. Shares common data and methods. */
public abstract class RestRequest {

  private static final String KEY_FILE_PATH = "/etc/openbaton/service-key";
  private static final String SDK_PROPERTIES_FILE = "sdk.api.properties";
  private String keyFilePath;

  private Logger log = LoggerFactory.getLogger(this.getClass());
  protected final String baseUrl;
  protected final String pathUrl;

  //	protected final String url;

  protected Gson mapper;
  private String username;
  private String password;
  private boolean isService;
  private String serviceName;
  private String serviceTokenUrl;
  private static final PropertyReader propertyReader = new PropertyReader(SDK_PROPERTIES_FILE);

  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  private String projectId;
  private String authStr = "openbatonOSClient" + ":" + "secret";
  private String encoding = Base64.encodeBase64String(authStr.getBytes());
  private final String provider;
  private String token = null;
  private String bearerToken = null;

  private CloseableHttpClient httpClient;
  private RequestConfig config =
      RequestConfig.custom().setConnectionRequestTimeout(10000).setConnectTimeout(60000).build();

  /**
   * RestRequest constructor for normal users.
   *
   * @param username
   * @param password
   * @param projectId
   * @param sslEnabled
   * @param nfvoIp
   * @param nfvoPort
   * @param path
   * @param version
   */
  public RestRequest(
      String username,
      String password,
      String projectId,
      boolean sslEnabled,
      final String nfvoIp,
      String nfvoPort,
      String path,
      String version) {
    if (sslEnabled) {
      this.baseUrl = "https://" + nfvoIp + ":" + nfvoPort + "/api/v" + version;
      this.provider = "https://" + nfvoIp + ":" + nfvoPort + "/oauth/token";
      this.httpClient = getHttpClientForSsl();
    } else {
      this.baseUrl = "http://" + nfvoIp + ":" + nfvoPort + "/api/v" + version;
      this.provider = "http://" + nfvoIp + ":" + nfvoPort + "/oauth/token";
      this.httpClient =
          HttpClientBuilder.create()
              .setDefaultRequestConfig(config)
              .setConnectionManager(new PoolingHttpClientConnectionManager())
              .build();
    }
    this.pathUrl = this.baseUrl + path;
    this.serviceTokenUrl = this.baseUrl + propertyReader.getRestUrl("Service") + "/register";
    this.username = username;
    this.password = password;
    this.isService = false;
    this.projectId = projectId;

    GsonBuilder builder = new GsonBuilder();
    //builder.registerTypeAdapter(Date.class, new GsonSerializerDate());
    //builder.registerTypeAdapter(Date.class, new GsonDeserializerDate());
    this.mapper = builder.setPrettyPrinting().create();
  }

  /**
   * RestRequest constructor for services.
   *
   * @param serviceName
   * @param projectId
   * @param sslEnabled
   * @param nfvoIp
   * @param nfvoPort
   * @param path
   * @param version
   * @param keyFilePath
   */
  public RestRequest(
      String serviceName,
      String projectId,
      boolean sslEnabled,
      final String nfvoIp,
      String nfvoPort,
      String path,
      String version,
      String keyFilePath)
      throws FileNotFoundException {
    if (sslEnabled) {
      this.baseUrl = "https://" + nfvoIp + ":" + nfvoPort + "/api/v" + version;
      this.provider = "https://" + nfvoIp + ":" + nfvoPort + "/oauth/token";
      this.httpClient = getHttpClientForSsl();
    } else {
      this.baseUrl = "http://" + nfvoIp + ":" + nfvoPort + "/api/v" + version;
      this.provider = "http://" + nfvoIp + ":" + nfvoPort + "/oauth/token";
      this.httpClient =
          HttpClientBuilder.create()
              .setDefaultRequestConfig(config)
              .setConnectionManager(new PoolingHttpClientConnectionManager())
              .build();
    }
    this.pathUrl = this.baseUrl + path;
    this.serviceTokenUrl = this.baseUrl + propertyReader.getRestUrl("Service") + "/register";
    this.serviceName = serviceName;
    this.isService = true;
    this.projectId = projectId;

    GsonBuilder builder = new GsonBuilder();
    //builder.registerTypeAdapter(Date.class, new GsonSerializerDate());
    //builder.registerTypeAdapter(Date.class, new GsonDeserializerDate());
    this.mapper = builder.setPrettyPrinting().create();
    if (keyFilePath != null) this.keyFilePath = keyFilePath;
    else this.keyFilePath = propertyReader.getSimpleProperty("key-file-location", KEY_FILE_PATH);
    if (!new File(this.keyFilePath).exists()) {
      log.error("missing key file for services");
      throw new FileNotFoundException("missing key file for services");
    }
  }

  /**
   * Does the POST Request
   *
   * @param id
   * @return String
   * @throws SDKException
   */
  public String requestPost(final String id) throws SDKException {
    CloseableHttpResponse response = null;
    HttpPost httpPost = null;
    try {
      log.debug("pathUrl: " + pathUrl);
      log.debug("id: " + pathUrl + "/" + id);

      checkToken();

      // call the api here
      log.debug("Executing post on: " + this.pathUrl + "/" + id);
      httpPost = new HttpPost(this.pathUrl + "/" + id);
      preparePostHeader(httpPost, "application/json", "application/json");

      response = httpClient.execute(httpPost);

      // check response status
      RestUtils.checkStatus(response, HttpURLConnection.HTTP_CREATED);
      // return the response of the request
      String result = "";
      if (response.getEntity() != null) {
        result = EntityUtils.toString(response.getEntity());
      }
      response.close();
      log.trace("received: " + result);

      httpPost.releaseConnection();
      return result;
    } catch (IOException e) {
      // catch request exceptions here
      log.error(e.getMessage(), e);
      if (httpPost != null) {
        httpPost.releaseConnection();
      }
      throw new SDKException(
          "Could not http-post or open the object properly",
          e.getStackTrace(),
          "Could not http-post or open the object properly because: " + e.getMessage());
    } catch (SDKException e) {
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
        token = null;
        if (httpPost != null) {
          httpPost.releaseConnection();
        }
        return requestPost(id);
      } else {
        if (httpPost != null) {
          httpPost.releaseConnection();
        }
        try {
          throw new SDKException(
              "Status is " + response.getStatusLine().getStatusCode(),
              new StackTraceElement[0],
              EntityUtils.toString(response.getEntity()));
        } catch (IOException e1) {
          e1.printStackTrace();
          throw new SDKException(
              "Status is " + response.getStatusLine().getStatusCode(),
              new StackTraceElement[0],
              "could not provide reason because: " + e.getMessage());
        }
      }
    }
  }

  /**
   * Executes a http post with to a given url, while serializing the object content as json and
   * returning the response
   *
   * @param object the object content to be serialized as json
   * @return a string containing the response content
   */
  public Serializable requestPost(final Serializable object) throws SDKException {
    return requestPost("", object);
  }

  public Serializable requestPost(
      final String id, final Serializable object, final String acceptMime, final String contentMime)
      throws SDKException {
    CloseableHttpResponse response = null;
    HttpPost httpPost = null;
    try {
      log.trace("Object is: " + object);
      String fileJSONNode;
      if (object instanceof String) {
        fileJSONNode = (String) object;
      } else {
        fileJSONNode = mapper.toJson(object);
      }

      log.trace("sending: " + fileJSONNode.toString());
      log.debug("pathUrl: " + pathUrl);
      log.debug("id: " + pathUrl + "/" + id);

      checkToken();

      // call the api here
      log.debug("Executing post on: " + this.pathUrl + "/" + id);
      httpPost = new HttpPost(this.pathUrl + "/" + id);
      preparePostHeader(httpPost, acceptMime, contentMime);
      httpPost.setEntity(new StringEntity(fileJSONNode));

      response = httpClient.execute(httpPost);

      // check response status
      RestUtils.checkStatus(response, HttpURLConnection.HTTP_CREATED);
      // return the response of the request
      Serializable result = null;
      if (response.getEntity() != null) {
        result = EntityUtils.toByteArray(response.getEntity());
      }
      return result;
    } catch (IOException e) {
      // catch request exceptions here
      log.error(e.getMessage(), e);
      if (httpPost != null) {
        httpPost.releaseConnection();
      }
      throw new SDKException(
          "Could not http-post or open the object properly",
          e.getStackTrace(),
          "Could not http-post or open the object properly because: " + e.getMessage());
    } catch (SDKException e) {
      if (response != null
          && response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
        token = null;
        if (httpPost != null) {
          httpPost.releaseConnection();
        }
        return requestPost(id);
      } else if (response != null) {
        throw e;
      } else {
        throw e;
      }
    }
  }

  /**
   * Add accept, content-type, projectId and token headers to an HttpPost object.
   *
   * @param httpPost
   * @param acceptMimeType
   * @param contentMimeType
   */
  private void preparePostHeader(HttpPost httpPost, String acceptMimeType, String contentMimeType) {
    if (acceptMimeType != null && !acceptMimeType.equals("")) {
      httpPost.setHeader(new BasicHeader("accept", acceptMimeType));
    }
    if (contentMimeType != null && !contentMimeType.equals("")) {
      httpPost.setHeader(new BasicHeader("Content-Type", contentMimeType));
    }
    httpPost.setHeader(new BasicHeader("project-id", projectId));
    if (token != null && bearerToken != null) {
      httpPost.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));
    }
  }

  private CloseableHttpResponse genericPost(
      String id, Serializable object, String acceptMimeType, String contentMimeType)
      throws SDKException, IOException {
    CloseableHttpResponse response = null;
    HttpPost httpPost = null;
    log.trace("Object is: " + object);
    String fileJSONNode;
    if (object instanceof String) {
      fileJSONNode = (String) object;
    } else {
      fileJSONNode = mapper.toJson(object);
    }

    log.trace("sending: " + fileJSONNode.toString());
    log.debug("pathUrl: " + pathUrl);
    log.debug("id: " + pathUrl + "/" + id);

    checkToken();

    // call the api here
    log.debug("Executing post on: " + this.pathUrl + "/" + id);
    httpPost = new HttpPost(this.pathUrl + "/" + id);
    preparePostHeader(httpPost, acceptMimeType, contentMimeType);
    httpPost.setEntity(new StringEntity(fileJSONNode));

    response = httpClient.execute(httpPost);
    return response;
  }

  public Serializable requestPost(final String id, final Serializable object) throws SDKException {
    CloseableHttpResponse response = null;
    HttpPost httpPost = null;
    try {
      log.trace("Object is: " + object);
      String fileJSONNode;
      if (object instanceof String) {
        fileJSONNode = (String) object;
      } else {
        fileJSONNode = mapper.toJson(object);
      }

      log.trace("sending: " + fileJSONNode.toString());
      log.debug("pathUrl: " + pathUrl);
      log.debug("id: " + pathUrl + "/" + id);

      checkToken();

      // call the api here
      log.debug("Executing post on: " + this.pathUrl + "/" + id);
      httpPost = new HttpPost(this.pathUrl + "/" + id);
      if (!(object instanceof String)) {
        preparePostHeader(httpPost, "application/json", "application/json");
      } else {
        preparePostHeader(httpPost, null, null);
      }
      httpPost.setEntity(new StringEntity(fileJSONNode));

      response = httpClient.execute(httpPost);

      // check response status
      RestUtils.checkStatus(response, HttpURLConnection.HTTP_CREATED);
      // return the response of the request
      String result = "";
      if (response.getEntity() != null) {
        result = EntityUtils.toString(response.getEntity());
      }

      if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_NO_CONTENT) {
        if (object instanceof String) {
          return result;
        }
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(result);
        result = mapper.toJson(jsonElement);
        log.trace("received: " + result);

        log.trace("Casting it into: " + object.getClass());
        return mapper.fromJson(result, object.getClass());
      }
      response.close();
      httpPost.releaseConnection();
      return null;
    } catch (IOException e) {
      // catch request exceptions here
      log.error(e.getMessage(), e);
      if (httpPost != null) {
        httpPost.releaseConnection();
      }
      throw new SDKException(
          "Could not http-post or open the object properly",
          e.getStackTrace(),
          "Could not http-post or open the object properly because: " + e.getMessage());
    } catch (SDKException e) {
      if (response != null
          && response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
        token = null;
        if (httpPost != null) {
          httpPost.releaseConnection();
        }
        return requestPost(id);
      } else if (response != null) {
        throw e;
      } else {
        throw e;
      }
    }
  }

  /**
   * Executes a http post with to a given url, while serializing the object content as json. The
   * type parameter specifies to which object type the post response should be mapped before
   * returning it. This can be useful if the method's return type is not the same as the type of the
   * object parameter.
   *
   * @param object the object content to be serialized as json
   * @param type the object type to which the response should be mapped
   * @return a string containing the response content
   */
  public Serializable requestPost(final String id, final Serializable object, final Type type)
      throws SDKException {
    CloseableHttpResponse response = null;
    HttpPost httpPost = null;
    try {
      log.trace("Object is: " + object);
      String fileJSONNode = mapper.toJson(object);
      log.trace("sending: " + fileJSONNode.toString());
      log.debug("pathUrl: " + pathUrl);
      log.debug("id: " + pathUrl + "/" + id);

      checkToken();

      // call the api here
      log.debug("Executing post on: " + this.pathUrl + "/" + id);
      httpPost = new HttpPost(this.pathUrl + "/" + id);
      preparePostHeader(httpPost, "application/json", "application/json");
      httpPost.setEntity(new StringEntity(fileJSONNode));

      response = httpClient.execute(httpPost);

      // check response status
      RestUtils.checkStatus(response, HttpURLConnection.HTTP_CREATED);
      // return the response of the request
      String result = "";
      if (response.getEntity() != null) {
        result = EntityUtils.toString(response.getEntity());
      }

      if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_NO_CONTENT) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(result);
        result = mapper.toJson(jsonElement);
        log.trace("received: " + result);

        log.trace("Casting it into: " + type);
        return mapper.fromJson(result, type);
      }
      response.close();
      httpPost.releaseConnection();
      return null;
    } catch (IOException e) {
      // catch request exceptions here
      log.error(e.getMessage(), e);
      if (httpPost != null) {
        httpPost.releaseConnection();
      }
      throw new SDKException(
          "Could not http-post or open the object properly",
          e.getStackTrace(),
          "Could not http-post or open the object properly because: " + e.getMessage());
    } catch (SDKException e) {
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
        token = null;
        if (httpPost != null) {
          httpPost.releaseConnection();
        }
        return requestPost(id);
      } else {
        if (httpPost != null) {
          httpPost.releaseConnection();
        }
        try {
          throw new SDKException(
              "Status is " + response.getStatusLine().getStatusCode(),
              new StackTraceElement[0],
              EntityUtils.toString(response.getEntity()));
        } catch (IOException e1) {
          e1.printStackTrace();
          throw new SDKException(
              "Status is " + response.getStatusLine().getStatusCode(),
              new StackTraceElement[0],
              "could not provide reason because: " + e.getMessage());
        }
      }
    }
  }

  /**
   * Used to upload tar files to the NFVO for creating VNFPackages.
   *
   * @param f the tar file containing the VNFPackage
   * @return the created VNFPackage object
   * @throws SDKException
   */
  public VNFPackage requestPostPackage(final File f) throws SDKException {
    CloseableHttpResponse response = null;
    HttpPost httpPost = null;

    try {
      checkToken();
      log.debug("Executing post on " + pathUrl);
      httpPost = new HttpPost(this.pathUrl);
      preparePostHeader(httpPost, "multipart/form-data", null);

      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
      multipartEntityBuilder.addBinaryBody("file", f);
      httpPost.setEntity(multipartEntityBuilder.build());

      response = httpClient.execute(httpPost);
    } catch (ClientProtocolException e) {
      httpPost.releaseConnection();
      throw new SDKException(
          "Could not create VNFPackage from file " + f.getName(),
          e.getStackTrace(),
          e.getMessage());
    } catch (IOException e) {
      httpPost.releaseConnection();
      throw new SDKException(
          "Could not create VNFPackage from file " + f.getName(),
          e.getStackTrace(),
          e.getMessage());
    }

    // check response status
    RestUtils.checkStatus(response, HttpURLConnection.HTTP_OK);
    // return the response of the request
    String result = "";
    if (response.getEntity() != null) {
      try {
        result = EntityUtils.toString(response.getEntity());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_NO_CONTENT) {
      JsonParser jsonParser = new JsonParser();
      JsonElement jsonElement = jsonParser.parse(result);
      result = mapper.toJson(jsonElement);
      log.debug("Uploaded the VNFPackage");
      log.trace("received: " + result);

      log.trace("Casting it into: " + VNFPackage.class);
      httpPost.releaseConnection();
      return mapper.fromJson(result, VNFPackage.class);
    }
    httpPost.releaseConnection();
    return null;
  }

  private void checkToken() throws SDKException {
    try {
      if (this.token == null
          && ((isService && this.serviceName != null && !this.serviceName.equals(""))
              || (!((this.username == null && this.username.equals(""))
                  || (this.password == null || this.password.equals("")))))) {
        getAccessToken();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new SDKException(
          (isService ? "Could not get service token" : "Could not get user token"),
          e.getStackTrace(),
          e.getMessage());
    }
  }

  private JsonNode getJsonNode(Serializable object) throws IOException {
    return new JsonNode(mapper.toJson(object));
  }

  /**
   * Executes a http delete with to a given id
   *
   * @param id the id path used for the api request
   */
  public void requestDelete(final String id) throws SDKException {
    CloseableHttpResponse response = null;
    HttpDelete httpDelete = null;
    try {
      log.debug("pathUrl: " + pathUrl);
      log.debug("id: " + pathUrl + "/" + id);

      checkToken();

      // call the api here
      log.info("Executing delete on: " + this.pathUrl + "/" + id);
      httpDelete = new HttpDelete(this.pathUrl + "/" + id);
      httpDelete.setHeader(new BasicHeader("project-id", projectId));
      if (token != null) {
        httpDelete.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));
      }

      response = httpClient.execute(httpDelete);

      // check response status
      RestUtils.checkStatus(response, HttpURLConnection.HTTP_NO_CONTENT);
      httpDelete.releaseConnection();
      // return the response of the request

    } catch (IOException e) {
      // catch request exceptions here
      log.error(e.getMessage(), e);
      if (httpDelete != null) {
        httpDelete.releaseConnection();
      }
      throw new SDKException("Could not http-delete", e.getStackTrace(), e.getMessage());
    } catch (SDKException e) {
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
        token = null;
        if (httpDelete != null) {
          httpDelete.releaseConnection();
        }
        requestDelete(id);
        return;
      }
      if (httpDelete != null) {
        httpDelete.releaseConnection();
      }
      throw new SDKException(
          "Could not http-delete or the api response was wrong", e.getStackTrace(), e.getMessage());
    }
  }

  /**
   * Executes a http get with to a given id
   *
   * @param id the id path used for the api request
   * @return a string containing he response content
   */
  public Object requestGet(final String id, Class type) throws SDKException {
    String url = this.pathUrl;
    if (id != null) {
      url += "/" + id;
      return requestGetWithStatus(url, null, type);
    } else {
      return requestGetAll(url, type, null);
    }
  }

  protected Object requestGetAll(String url, Class type) throws SDKException {
    url = this.pathUrl + "/" + url;
    return requestGetAll(url, type, null);
  }

  private Object requestGetAll(String url, Class type, final Integer httpStatus)
      throws SDKException {
    CloseableHttpResponse response = null;
    HttpGet httpGet = null;
    try {
      checkToken();

      // call the api here
      log.debug("Executing get on: " + url);
      httpGet = new HttpGet(url);
      httpGet.setHeader(new BasicHeader("project-id", projectId));
      if (token != null) {
        httpGet.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));
      }

      response = httpClient.execute(httpGet);

      // check response status
      if (httpStatus != null) {
        RestUtils.checkStatus(response, httpStatus);
      } else {
        RestUtils.checkStatus(response, HttpURLConnection.HTTP_OK);
      }
      // return the response of the request
      String result = "";
      if (response.getEntity() != null) {
        result = EntityUtils.toString(response.getEntity());
      }
      response.close();
      httpGet.releaseConnection();
      log.trace("result is: " + result);

      Class<?> aClass = Array.newInstance(type, 3).getClass();
      log.trace("class is: " + aClass);
      Object[] o = (Object[]) this.mapper.fromJson(result, aClass);
      log.trace("deserialized is: " + o);

      return o;
    } catch (IOException e) {
      // catch request exceptions here
      log.error(e.getMessage(), e);
      if (httpGet != null) {
        httpGet.releaseConnection();
      }
      throw new SDKException("Could not http-get", e.getStackTrace(), e.getMessage());
    } catch (SDKException e) {
      if (response != null) {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
          token = null;
          if (httpGet != null) {
            httpGet.releaseConnection();
          }
          return requestGetAll(url, type, httpStatus);
        } else {
          log.error(e.getMessage(), e);
          if (httpGet != null) {
            httpGet.releaseConnection();
          }
          throw new SDKException("Could not authorize", e.getStackTrace(), e.getMessage());
        }
      } else {
        log.error(e.getMessage(), e);
        if (httpGet != null) {
          httpGet.releaseConnection();
        }
        throw e;
      }
    }
  }

  /**
   * Executes a http get with to a given id, and possible executed an http (accept) status check of
   * the response if an httpStatus is delivered. If httpStatus is null, no check will be executed.
   *
   * @param url the id path used for the api request
   * @param httpStatus the http status to be checked.
   * @param type
   * @return a string containing the response content
   */
  private Object requestGetWithStatus(final String url, final Integer httpStatus, Class type)
      throws SDKException {
    CloseableHttpResponse response = null;
    HttpGet httpGet = null;
    try {
      checkToken();

      // call the api here
      log.debug("Executing get on: " + url);
      httpGet = new HttpGet(url);
      httpGet.setHeader(new BasicHeader("project-id", projectId));
      if (token != null) {
        httpGet.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));
      }

      response = httpClient.execute(httpGet);

      // check response status
      if (httpStatus != null) {
        RestUtils.checkStatus(response, httpStatus);
      } else {
        RestUtils.checkStatus(response, HttpURLConnection.HTTP_OK);
      }
      // return the response of the request
      String result = "";
      if (response.getEntity() != null) {
        result = EntityUtils.toString(response.getEntity());
      }
      response.close();
      httpGet.releaseConnection();
      log.trace("result is: " + result);

      JsonParser jsonParser = new JsonParser();
      JsonElement jsonElement = jsonParser.parse(result);
      result = mapper.toJson(jsonElement);
      log.trace("result is: " + result);
      Class<?> aClass = Array.newInstance(type, 1).getClass();
      log.trace("class is: " + aClass);

      return mapper.fromJson(result, type);
    } catch (IOException e) {
      // catch request exceptions here
      log.error(e.getMessage(), e);
      if (httpGet != null) {
        httpGet.releaseConnection();
      }
      throw new SDKException("Could not http-get", e.getStackTrace(), e.getMessage());
    } catch (SDKException e) {
      if (response != null) {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
          token = null;
          if (httpGet != null) {
            httpGet.releaseConnection();
          }
          return requestGetWithStatus(url, httpStatus, type);
        } else {
          log.error(e.getMessage(), e);
          if (httpGet != null) {
            httpGet.releaseConnection();
          }
          throw new SDKException("Could not authorize", e.getStackTrace(), e.getMessage());
        }
      } else {
        log.error(e.getMessage(), e);
        if (httpGet != null) {
          httpGet.releaseConnection();
        }
        throw e;
      }
    } finally {
      if (httpGet != null) {
        httpGet.releaseConnection();
      }
    }
  }

  /**
   * Executes a http get with to a given url, in contrast to the normal get it uses an http (accept)
   * status check of the response
   *
   * @param url the url path used for the api request
   * @return a string containing the response content
   */
  public Object requestGetWithStatusAccepted(String url, Class type) throws SDKException {
    url = this.pathUrl + "/" + url;
    return requestGetWithStatus(url, new Integer(HttpURLConnection.HTTP_ACCEPTED), type);
  }

  /**
   * Executes a http put with to a given id, while serializing the object content as json and
   * returning the response
   *
   * @param id the id path used for the api request
   * @param object the object content to be serialized as json
   * @return a string containing the response content
   */
  public Serializable requestPut(final String id, final Serializable object) throws SDKException {
    CloseableHttpResponse response = null;
    HttpPut httpPut = null;
    try {
      log.trace("Object is: " + object);
      String fileJSONNode = mapper.toJson(object);

      checkToken();

      // call the api here
      log.debug("Executing put on: " + this.pathUrl + "/" + id);
      httpPut = new HttpPut(this.pathUrl + "/" + id);
      httpPut.setHeader(new BasicHeader("accept", "application/json"));
      httpPut.setHeader(new BasicHeader("Content-Type", "application/json"));
      httpPut.setHeader(new BasicHeader("project-id", projectId));
      if (token != null) {
        httpPut.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));
      }
      httpPut.setEntity(new StringEntity(fileJSONNode));

      response = httpClient.execute(httpPut);

      // check response status
      RestUtils.checkStatus(response, HttpURLConnection.HTTP_ACCEPTED);
      // return the response of the request
      String result = "";
      if (response.getEntity() != null) {
        result = EntityUtils.toString(response.getEntity());
      }

      if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_NO_CONTENT) {
        response.close();
        httpPut.releaseConnection();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(result);
        result = mapper.toJson(jsonElement);
        log.trace("received: " + result);

        log.trace("Casting it into: " + object.getClass());
        return mapper.fromJson(result, object.getClass());
      }
      response.close();
      httpPut.releaseConnection();
      return null;
    } catch (IOException e) {
      // catch request exceptions here
      log.error(e.getMessage(), e);
      if (httpPut != null) {
        httpPut.releaseConnection();
      }
      throw new SDKException(
          "Could not http-put or the api response was wrong or open the object properly",
          e.getStackTrace(),
          e.getMessage());
    } catch (SDKException e) {
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
        token = null;
        if (httpPut != null) {
          httpPut.releaseConnection();
        }
        return requestPut(id, object);
      } else {
        if (httpPut != null) {
          httpPut.releaseConnection();
        }
        throw new SDKException(
            "Could not http-put or the api response was wrong or open the object properly",
            e.getStackTrace(),
            e.getMessage());
      }
    } finally {
      if (httpPut != null) {
        httpPut.releaseConnection();
      }
    }
  }

  private void getAccessToken() throws IOException, SDKException {
    if (isService) {
      try {
        log.debug("Registering Service " + serviceName);
        String key_data =
            new String(Files.readAllBytes(Paths.get(this.keyFilePath)), StandardCharsets.UTF_8);

        String encryptedMessage =
            KeyHelper.encryptNew(
                "{\"name\":\"" + serviceName + "\",\"action\":\"register\"}", key_data);

        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(this.serviceTokenUrl);
        httpPost.setHeader(new BasicHeader("accept", "text/plain,application/json"));
        httpPost.setHeader(new BasicHeader("Content-Type", "text/plain"));

        httpPost.setEntity(new StringEntity(encryptedMessage));

        log.debug("Post: " + httpPost.getURI());
        response = httpClient.execute(httpPost);
        RestUtils.checkStatus(response, HttpURLConnection.HTTP_CREATED);
        String encryptedToken = "";
        if (response.getEntity() != null) {
          encryptedToken = EntityUtils.toString(response.getEntity());
        }
        response.close();
        httpPost.releaseConnection();

        String decryptedToken = KeyHelper.decryptNew(encryptedToken, key_data);
        log.trace("Token is: " + decryptedToken);
        this.token = decryptedToken;
        this.bearerToken = "Bearer " + this.token;
      } catch (Exception e) {
        throw new SDKException(e);
      }
    } else {

      HttpPost httpPost = new HttpPost(provider);
      httpPost.setHeader("Authorization", "Basic " + encoding);
      List<BasicNameValuePair> parametersBody = new ArrayList<>();
      parametersBody.add(new BasicNameValuePair("grant_type", "password"));
      parametersBody.add(new BasicNameValuePair("username", this.username));
      parametersBody.add(new BasicNameValuePair("password", this.password));

      log.debug("Username is: " + username);
      log.debug("Password is: " + password);

      httpPost.setEntity(new UrlEncodedFormEntity(parametersBody, StandardCharsets.UTF_8));

      CloseableHttpResponse response = null;
      log.debug("httpPost is: " + httpPost.toString());
      response = httpClient.execute(httpPost);
      String responseString = null;
      responseString = EntityUtils.toString(response.getEntity());
      int statusCode = response.getStatusLine().getStatusCode();
      response.close();
      httpPost.releaseConnection();
      log.trace(statusCode + ": " + responseString);

      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      if (statusCode != 200) {
        JsonObject error = gson.fromJson(responseString, JsonObject.class);

        JsonElement detailMessage = error.get("detailMessage");
        if (detailMessage == null) {
          detailMessage = error.get("errorMessage");
        }
        if (detailMessage == null) {
          detailMessage = error.get("message");
        }
        if (detailMessage == null) {
          detailMessage = error.get("description");
        }
        if (detailMessage == null) {
          detailMessage = error.get("errorDescription");
        }

        log.error(
            "Status Code ["
                + statusCode
                + "]: Error signing-in ["
                + (detailMessage != null ? detailMessage.getAsString() : "no error description")
                + "]");

        if (detailMessage == null) {
          log.error("Got Error from server: \n" + gson.toJson(error));
        }
        throw new SDKException(
            "Status Code ["
                + statusCode
                + "]: Error signing-in ["
                + (detailMessage != null ? detailMessage.getAsString() : "no error description")
                + "]",
            new StackTraceElement[0],
            (detailMessage != null ? detailMessage.getAsString() : "no error description"));
      }
      JsonObject jobj = new Gson().fromJson(responseString, JsonObject.class);
      log.trace("JsonTokenAccess is: " + jobj.toString());
      try {
        String token = jobj.get("value").getAsString();
        log.trace(token);
        bearerToken = "Bearer " + token;
        this.token = token;
      } catch (NullPointerException e) {
        String error = jobj.get("error").getAsString();
        if (error.equals("invalid_grant")) {
          throw new SDKException(
              "Error during authentication: " + jobj.get("error_description").getAsString(),
              e.getStackTrace(),
              e.getMessage());
        }
      }
    }
  }

  private CloseableHttpClient getHttpClientForSsl() {
    SSLContext sslContext = null;
    try {
      sslContext =
          SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
    } catch (NoSuchAlgorithmException e) {
      log.error("Could not initialize the HttpClient for SSL connections");
      log.error(e.getMessage(), e);
    } catch (KeyManagementException e) {
      log.error("Could not initialize the HttpClient for SSL connections");
      log.error(e.getMessage(), e);
    } catch (KeyStoreException e) {
      log.error("Could not initialize the HttpClient for SSL connections");
      log.error(e.getMessage(), e);
    }

    // necessary to trust self signed certificates
    SSLConnectionSocketFactory sslConnectionSocketFactory =
        new SSLConnectionSocketFactory(
            sslContext, new String[] {"TLSv1"}, null, new NoopHostnameVerifier());

    Registry<ConnectionSocketFactory> socketFactoryRegistry =
        RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", sslConnectionSocketFactory)
            .build();

    return HttpClientBuilder.create()
        .setDefaultRequestConfig(config)
        .setConnectionManager(new PoolingHttpClientConnectionManager(socketFactoryRegistry))
        .setSSLSocketFactory(sslConnectionSocketFactory)
        .build();
  }

  public static void main(String[] args) {}
}
