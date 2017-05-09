package org.openbaton.sdk.api.util;

import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.openbaton.sdk.api.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestUtils {
  private static Logger log = LoggerFactory.getLogger(RestUtils.class);

  /**
   * Check whether a json repsonse has the right http status. If not, an SDKException is thrown.
   *
   * @param httpResponse the http response
   * @param httpStatus the (desired) http status of the repsonse
   */
  public static void checkStatus(CloseableHttpResponse httpResponse, final int httpStatus)
      throws SDKException {

    if (httpResponse.getStatusLine().getStatusCode() != httpStatus) {
      log.error(
          "Status expected: "
              + httpStatus
              + " obtained: "
              + httpResponse.getStatusLine().getStatusCode());
      log.error("httpresponse: " + httpResponse.toString());
      String body;
      try {
        body = EntityUtils.toString(httpResponse.getEntity());
      } catch (IOException e) {
        e.printStackTrace();
        throw new SDKException(
            "Status is " + httpResponse.getStatusLine().getStatusCode(),
            new StackTraceElement[0],
            "could not provide reason because: " + e.getMessage());
      }
      log.error("Body: " + body);
      throw new SDKException(
          "Status is " + httpResponse.getStatusLine().getStatusCode(),
          new StackTraceElement[0],
          body);
    }
  }
}
