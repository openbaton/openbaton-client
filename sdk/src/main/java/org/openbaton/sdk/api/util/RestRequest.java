package org.openbaton.sdk.api.util;


import com.google.gson.*;
import com.mashape.unirest.http.JsonNode;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.openbaton.catalogue.nfvo.VNFPackage;
import org.openbaton.sdk.api.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * OpenBaton api request request abstraction for all requester. Shares common data and methods.
 */
public abstract class RestRequest {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    protected final String baseUrl;

//	protected final String url;

    protected Gson mapper;
    private String username;
    private String password;
    private String projectId;
    private boolean sslEnabled;
    private String authStr = "openbatonOSClient" + ":" + "secret";
    private String encoding = Base64.encodeBase64String(authStr.getBytes());
    private final String provider;
    private String token = null;
    private String bearerToken = null;

    private CloseableHttpClient httpClient;
    private RequestConfig config = RequestConfig.custom()
            .setConnectionRequestTimeout(10000).setConnectTimeout(10000).build();

    /**
     * Create a request with a given url path
     */
    public RestRequest(String username, String password, String projectId, boolean sslEnabled, final String nfvoIp, String nfvoPort, String path, String version) {
        if (sslEnabled) {
            this.baseUrl = "https://" + nfvoIp + ":" + nfvoPort + "/api/v" + version + path;
            this.provider = "https://" + nfvoIp + ":" + nfvoPort + "/oauth/token";
        } else {
            this.baseUrl = "http://" + nfvoIp + ":" + nfvoPort + "/api/v" + version + path;
            this.provider = "http://" + nfvoIp + ":" + nfvoPort + "/oauth/token";
        }
        this.username = username;
        this.password = password;
        this.projectId = projectId;

        GsonBuilder builder = new GsonBuilder();
        /*builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });*/
        this.mapper = builder.setPrettyPrinting().create();

        if (sslEnabled)
            this.httpClient = getHttpClientForSsl();
        else
            this.httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

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
        try {
            log.debug("baseUrl: " + baseUrl);
            log.debug("id: " + baseUrl + "/" + id);

            try {
                checkToken();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new SDKException("Could not get token", e);
            }

            // call the api here
            log.debug("Executing post on: " + this.baseUrl + "/" + id);
            HttpPost httpPost = new HttpPost(this.baseUrl + "/" + id);
            httpPost.setHeader(new BasicHeader("accept", "application/json"));
            httpPost.setHeader(new BasicHeader("Content-Type", "application/json"));
            httpPost.setHeader(new BasicHeader("project-id", projectId));
            if (token != null)
                httpPost.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));

            response = httpClient.execute(httpPost);

            // check response status
            checkStatus(response, HttpURLConnection.HTTP_CREATED);
            // return the response of the request
            String result = "";
            if (response.getEntity() != null)
                result = EntityUtils.toString(response.getEntity());
            response.close();
            log.trace("received: " + result);

            return result;
        } catch (IOException e) {
            // catch request exceptions here
            log.error(e.getMessage(), e);
            throw new SDKException("Could not http-post or open the object properly", e);
        } catch (SDKException e) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                token = null;
                return requestPost(id);
            } else
                throw new SDKException("Status is " + response.getStatusLine().getStatusCode());
        }
    }

    /**
     * Executes a http post with to a given url, while serializing the object content as json
     * and returning the response
     *
     * @param object the object content to be serialized as json
     * @return a string containing the response content
     */
    public Serializable requestPost(final Serializable object) throws SDKException {
        return requestPost("", object);
    }


    public Serializable requestPost(final String id, final Serializable object) throws SDKException {
        CloseableHttpResponse response = null;
        try {
            log.trace("Object is: " + object);
            String fileJSONNode = mapper.toJson(object);
            log.trace("sending: " + fileJSONNode.toString());
            log.debug("baseUrl: " + baseUrl);
            log.debug("id: " + baseUrl + "/" + id);

            try {
                checkToken();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new SDKException("Could not get token", e);
            }

            // call the api here
            log.debug("Executing post on: " + this.baseUrl + "/" + id);
            HttpPost httpPost = new HttpPost(this.baseUrl + "/" + id);
            httpPost.setHeader(new BasicHeader("accept", "application/json"));
            httpPost.setHeader(new BasicHeader("Content-Type", "application/json"));
            httpPost.setHeader(new BasicHeader("project-id", projectId));
            if (token != null)
                httpPost.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));
            httpPost.setEntity(new StringEntity(fileJSONNode));

            response = httpClient.execute(httpPost);

            // check response status
            checkStatus(response, HttpURLConnection.HTTP_CREATED);
            // return the response of the request
            String result = "";
            if (response.getEntity() != null)
                result = EntityUtils.toString(response.getEntity());
            response.close();

            if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_NO_CONTENT) {
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(result);
                result = mapper.toJson(jsonElement);
                log.trace("received: " + result);

                log.trace("Casting it into: " + object.getClass());
                return mapper.fromJson(result, object.getClass());
            }
            return null;
        } catch (IOException e) {
            // catch request exceptions here
            log.error(e.getMessage(), e);
            throw new SDKException("Could not http-post or open the object properly", e);
        } catch (SDKException e) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                token = null;
                return requestPost(id);
            } else
                throw new SDKException("Status is " + response.getStatusLine().getStatusCode());
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

        try {
            try {
                checkToken();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new SDKException("Could not get token", e);
            }
            log.debug("Executing post on " + baseUrl);
            HttpPost httpPost = new HttpPost(this.baseUrl);
            httpPost.setHeader(new BasicHeader("accept", "multipart/form-data"));
            httpPost.setHeader(new BasicHeader("project-id", projectId));
            if (token != null)
                httpPost.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addBinaryBody("file", f);
            httpPost.setEntity(multipartEntityBuilder.build());


            response = httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            throw new SDKException("Could not create VNFPackage from file " + f.getName(), e);
        } catch (IOException e) {
            throw new SDKException("Could not create VNFPackage from file " + f.getName(), e);
        }

        // check response status
        checkStatus(response, HttpURLConnection.HTTP_OK);
        // return the response of the request
        String result = "";
        if (response.getEntity() != null)
            try {
                result = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

        if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_NO_CONTENT) {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(result);
            result = mapper.toJson(jsonElement);
            log.debug("Uploaded the VNFPackage");
            log.trace("received: " + result);

            log.trace("Casting it into: " + VNFPackage.class);
            return mapper.fromJson(result, VNFPackage.class);
        }
        return null;
    }

    private void checkToken() throws IOException, SDKException {
        if (!(this.username == null || this.password == null))
            if (token == null && (!this.username.equals("") || !this.password.equals(""))) {
                getAccessToken();
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
        try {
            log.debug("baseUrl: " + baseUrl);
            log.debug("id: " + baseUrl + "/" + id);

            try {
                checkToken();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new SDKException("Could not get token", e);
            }

            // call the api here
            log.info("Executing delete on: " + this.baseUrl + "/" + id);
            HttpDelete httpDelete = new HttpDelete(this.baseUrl + "/" + id);
            httpDelete.setHeader(new BasicHeader("project-id", projectId));
            if (token != null)
                httpDelete.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));

            response = httpClient.execute(httpDelete);

            // check response status
            checkStatus(response, HttpURLConnection.HTTP_NO_CONTENT);
            // return the response of the request

        } catch (IOException e) {
            // catch request exceptions here
            log.error(e.getMessage(), e);
            throw new SDKException("Could not http-delete", e);
        } catch (SDKException e) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                token = null;
                requestDelete(id);
                return;
            }
            throw new SDKException("Could not http-delete or the api response was wrong", e);
        }
    }


    /**
     * Executes a http get with to a given id
     *
     * @param id the id path used for the api request
     * @return a string containing he response content
     */
    public Object requestGet(final String id, Class type) throws SDKException {
        String url = this.baseUrl;
        if (id != null) {
            url += "/" + id;
            return requestGetWithStatus(url, null, type);
        } else return requestGetAll(url, type, null);
    }

    protected Object requestGetAll(String url, Class type) throws SDKException {
        url = this.baseUrl + "/" + url;
        return requestGetAll(url, type, null);
    }

    private Object requestGetAll(String url, Class type, final Integer httpStatus) throws SDKException {
        CloseableHttpResponse response = null;
        try {
            try {
                checkToken();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new SDKException("Could not get token", e);
            }

            // call the api here
            log.debug("Executing get on: " + url);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(new BasicHeader("project-id", projectId));
            if (token != null)
                httpGet.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));

            response = httpClient.execute(httpGet);

            // check response status
            if (httpStatus != null) {
                checkStatus(response, httpStatus);
            } else {
                checkStatus(response, HttpURLConnection.HTTP_OK);
            }
            // return the response of the request
            String result = "";
            if (response.getEntity() != null)
                result = EntityUtils.toString(response.getEntity());
            response.close();
            log.trace("result is: " + result);

            Class<?> aClass = Array.newInstance(type, 3).getClass();
            log.trace("class is: " + aClass);
            Object[] o = (Object[]) mapper.fromJson(result, aClass);
            log.trace("deserialized is: " + o);

            return o;
        } catch (IOException e) {
            // catch request exceptions here
            log.error(e.getMessage(), e);
            throw new SDKException("Could not http-get", e);
        } catch (SDKException e) {
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                    token = null;
                    return requestGetAll(url, type, httpStatus);
                } else {
                    log.error(e.getMessage(), e);
                    throw new SDKException("Could not authorize", e);
                }
            } else {
                log.error(e.getMessage(), e);
                throw e;
            }
        }
    }


    /**
     * Executes a http get with to a given id, and possible executed an http (accept) status check of the response if an httpStatus is delivered.
     * If httpStatus is null, no check will be executed.
     *
     * @param url        the id path used for the api request
     * @param httpStatus the http status to be checked.
     * @param type
     * @return a string containing the response content
     */
    private Object requestGetWithStatus(final String url, final Integer httpStatus, Class type) throws SDKException {
        CloseableHttpResponse response = null;
        try {
            try {
                checkToken();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new SDKException("Could not get token", e);
            }

            // call the api here
            log.debug("Executing get on: " + url);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(new BasicHeader("project-id", projectId));
            if (token != null)
                httpGet.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));

            response = httpClient.execute(httpGet);

            // check response status
            if (httpStatus != null) {
                checkStatus(response, httpStatus);
            } else {
                checkStatus(response, HttpURLConnection.HTTP_OK);
            }
            // return the response of the request
            String result = "";
            if (response.getEntity() != null)
                result = EntityUtils.toString(response.getEntity());
            response.close();
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
            throw new SDKException("Could not http-get", e);
        } catch (SDKException e) {
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                    token = null;
                    return requestGetWithStatus(url, httpStatus, type);
                } else {
                    log.error(e.getMessage(), e);
                    throw new SDKException("Could not authorize", e);
                }
            } else {
                log.error(e.getMessage(), e);
                throw e;
            }
        }
    }

    /**
     * Executes a http get with to a given url, in contrast to the normal get it uses an http (accept) status check of the response
     *
     * @param url the url path used for the api request
     * @return a string containing the response content
     */
    public Object requestGetWithStatusAccepted(String url, Class type) throws SDKException {
        url = this.baseUrl + "/" + url;
        return requestGetWithStatus(url, new Integer(HttpURLConnection.HTTP_ACCEPTED), type);
    }

    /**
     * Executes a http put with to a given id, while serializing the object content as json
     * and returning the response
     *
     * @param id     the id path used for the api request
     * @param object the object content to be serialized as json
     * @return a string containing the response content
     */
    public Serializable requestPut(final String id, final Serializable object) throws SDKException {
        CloseableHttpResponse response = null;
        try {
            log.trace("Object is: " + object);
            String fileJSONNode = mapper.toJson(object);

            try {
                checkToken();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new SDKException("Could not get token", e);
            }

            // call the api here
            log.debug("Executing put on: " + this.baseUrl + "/" + id);
            HttpPut httpPut = new HttpPut(this.baseUrl + "/" + id);
            httpPut.setHeader(new BasicHeader("accept", "application/json"));
            httpPut.setHeader(new BasicHeader("Content-Type", "application/json"));
            httpPut.setHeader(new BasicHeader("project-id", projectId));
            if (token != null)
                httpPut.setHeader(new BasicHeader("authorization", bearerToken.replaceAll("\"", "")));
            httpPut.setEntity(new StringEntity(fileJSONNode));

            response = httpClient.execute(httpPut);

            // check response status
            checkStatus(response, HttpURLConnection.HTTP_ACCEPTED);
            // return the response of the request
            String result = "";
            if (response.getEntity() != null)
                result = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_NO_CONTENT) {
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(result);
                result = mapper.toJson(jsonElement);
                log.trace("received: " + result);

                log.trace("Casting it into: " + object.getClass());
                return mapper.fromJson(result, object.getClass());
            }
            response.close();
            return null;
        } catch (IOException e) {
            // catch request exceptions here
            log.error(e.getMessage(), e);
            throw new SDKException("Could not http-put or the api response was wrong or open the object properly", e);
        } catch (SDKException e) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                token = null;
                return requestPut(id, object);
            } else
                throw new SDKException("Could not http-put or the api response was wrong or open the object properly", e);
        }
    }

    /**
     * Check wether a json repsonse has the right http status. If not, an SDKException is thrown.
     *
     * @param httpResponse the http response
     * @param httpStatus   the (desired) http status of the repsonse
     */
    private void checkStatus(CloseableHttpResponse httpResponse, final int httpStatus) throws SDKException {
        if (httpResponse.getStatusLine().getStatusCode() != httpStatus) {
            System.out.println("Status expected: " + httpStatus + " obtained: " + httpResponse.getStatusLine().getStatusCode());
            throw new SDKException("Received wrong API HTTPStatus");
        }
    }

    private void getAccessToken() throws IOException, SDKException {

        HttpPost httpPost = new HttpPost(provider);

        httpPost.setHeader("Authorization", "Basic " + encoding);
        List<BasicNameValuePair> parametersBody = new ArrayList<>();
        parametersBody.add(new BasicNameValuePair("grant_type", "password"));
        parametersBody.add(new BasicNameValuePair("username", this.username));
        parametersBody.add(new BasicNameValuePair("password", this.password));

        log.debug("Username is: " + username);
        log.debug("Password is: " + password);

        httpPost.setEntity(new UrlEncodedFormEntity(parametersBody, StandardCharsets.UTF_8));

        org.apache.http.HttpResponse response = null;
        log.debug("httpPost is: " + httpPost.toString());
        response = httpClient.execute(httpPost);

        String responseString = null;
        responseString = EntityUtils.toString(response.getEntity());
        int statusCode = response.getStatusLine().getStatusCode();
        log.trace(statusCode + ": " + responseString);

        if (statusCode != 200) {
            ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
            log.error("Status Code [" + statusCode + "]: Error signing-in [" + error.error + "] - " + error.error_description);
            throw new SDKException("Status Code [" + statusCode + "]: Error signing-in [" + error.error + "] - " + error.error_description);
        }
        JsonObject jobj = new Gson().fromJson(responseString, JsonObject.class);
        log.trace("JsonTokeAccess is: " + jobj.toString());
        try {
            String token = jobj.get("value").getAsString();
            log.trace(token);
            bearerToken = "Bearer " + token;
            this.token = token;
        } catch (NullPointerException e) {
            String error = jobj.get("error").getAsString();
            if (error.equals("invalid_grant")) {
                throw new SDKException("Error during authentication: " + jobj.get("error_description").getAsString(), e);
            }
        }

    }

    private CloseableHttpClient getHttpClientForSsl() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null,
                    new TrustSelfSignedStrategy()).build();
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
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                new String[]{"TLSv1"}, null, new NoopHostnameVerifier());

        return HttpClientBuilder.create().setDefaultRequestConfig(config)
                .setSSLSocketFactory(sslConnectionSocketFactory).build();
    }


    private class ParseComError implements Serializable {
        String error_description;
        String error;
    }
}
