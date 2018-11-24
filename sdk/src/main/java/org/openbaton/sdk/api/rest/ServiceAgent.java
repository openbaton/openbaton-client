/*
 *
 *  * Copyright (c) 2016 Fraunhofer FOKUS
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 *
 */

package org.openbaton.sdk.api.rest;

import java.util.HashMap;
import java.util.List;
import org.openbaton.catalogue.security.ServiceMetadata;
import org.openbaton.sdk.api.annotations.Help;
import org.openbaton.sdk.api.exception.SDKException;
import org.openbaton.sdk.api.util.AbstractRestAgent;
import org.springframework.http.MediaType;

/**
 * This class is a Rest Request Agent for sending requests regarding User objects to the NFVO API.
 * It is thread safe.
 */
public class ServiceAgent extends AbstractRestAgent<ServiceMetadata> {

  /**
   * @param username the username used for sending requests
   * @param password the password used for sending requests
   * @param projectId the NFVO Project's ID that will be used in the requests to the NFVO
   * @param sslEnabled true if the NFVO uses SSL
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   */
  public ServiceAgent(
      String username,
      String password,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String version) {
    super(
        username,
        password,
        projectId,
        sslEnabled,
        nfvoIp,
        nfvoPort,
        version,
        ServiceMetadata.class);
  }

  /**
   * @param serviceName the service name used for sending requests
   * @param projectId the NFVO Project's ID that will be used in the requests to the NFVO
   * @param sslEnabled true if the NFVO uses SSL
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   * @param serviceKey the key for authenticating the service
   * @throws IllegalArgumentException if the service key is null
   */
  public ServiceAgent(
      String serviceName,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String version,
      String serviceKey)
      throws IllegalArgumentException {
    super(
        serviceName,
        projectId,
        sslEnabled,
        nfvoIp,
        nfvoPort,
        version,
        serviceKey,
        ServiceMetadata.class);
  }

  /**
   * Creates a new service.
   *
   * @param serviceName Name of the service to create
   * @param roles projects to access
   * @throws SDKException if the request fails
   * @return key for the service to authenticate
   */
  @Help(help = "Creates a new service")
  public String create(String serviceName, List<String> roles) throws SDKException {
    HashMap<String, Object> requestBody = new HashMap<>();
    requestBody.put("name", serviceName);
    requestBody.put("roles", roles);

    return new String(
        (byte[])
            requestPost(
                "create",
                requestBody,
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                MediaType.APPLICATION_JSON_VALUE));
  }
}
