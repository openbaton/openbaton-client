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

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.openbaton.sdk.api.annotations.Help;
import org.openbaton.sdk.api.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can be extended by explicit request agents which then obtain methods for sending
 * create, get, delete and update requests to the NFVO API.
 *
 * <p>Created by lto on 03/07/15.
 */
public class AbstractRestAgent<T extends Serializable> extends RestRequest {

  private final Class<T> clazz;
  private Logger log = LoggerFactory.getLogger(this.getClass());
  private static final String SDK_PROPERTIES_FILE = "sdk.api.properties";
  private static final PropertyReader propertyReader = new PropertyReader(SDK_PROPERTIES_FILE);

  /**
   * @param username the username used for sending requests
   * @param password the password used for sending requests
   * @param projectId the NFVO Project's ID that will be used in the requests to the NFVO
   * @param sslEnabled true if the NFVO uses SSL
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   * @param tClass the RestAgent works with objects of this class type
   */
  public AbstractRestAgent(
      String username,
      String password,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String version,
      Class<T> tClass) {
    super(
        username,
        password,
        projectId,
        sslEnabled,
        nfvoIp,
        nfvoPort,
        propertyReader.getRestUrl(tClass.getSimpleName()),
        version);
    clazz = tClass;
  }

  public AbstractRestAgent(
      String serviceName,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String version,
      String serviceKey,
      Class<T> tClass) {
    super(
        serviceName,
        projectId,
        sslEnabled,
        nfvoIp,
        nfvoPort,
        propertyReader.getRestUrl(tClass.getSimpleName()),
        version,
        serviceKey);
    clazz = tClass;
  }

  public Class<T> getClazz() {
    return clazz;
  }

  /**
   * Sends a request for creating an instance of type T to the NFVO API.
   *
   * @param object the object that is sent in the create request
   * @return the created object
   * @throws SDKException if the request fails
   */
  @Help(help = "Create the object of type {#}")
  public T create(final T object) throws SDKException {
    return (T) requestPost(object);
  }

  /**
   * Sends a request to the NFVO API for deleting an instance of type T specified by its ID.
   *
   * @param id the ID of the object that shall be deleted
   * @throws SDKException if the request fails
   */
  @Help(help = "Delete the object of type {#} passing the id")
  public void delete(final String id) throws SDKException {
    requestDelete(id);
  }

  /**
   * Sends a request for finding all instances of type T to the NFVO API.
   *
   * @return the list of found objects
   * @throws SDKException if the request fails
   */
  @Help(help = "Find all the objects of type {#}")
  public List<T> findAll() throws SDKException {
    return Arrays.asList((T[]) requestGet(null, clazz));
  }

  /**
   * Sends a request to the NFVO API for finding an instance of type T specified by it's ID.
   *
   * @param id the ID of the object that shall be retrieved
   * @return the found object
   * @throws SDKException if the request fails
   */
  @Help(help = "Find the object of type {#} through the id")
  public T findById(final String id) throws SDKException {
    return (T) requestGet(id, clazz);
  }

  /**
   * Sends a request to the NFVO API for updating an instance of type T specified by its ID.
   *
   * @param object the new object that is sent in the update request
   * @param id the ID of the object to update
   * @return the updated object
   * @throws SDKException if the request fails
   */
  @Help(help = "Update the object of type {#} passing the new object and the id of the old object")
  public T update(final T object, final String id) throws SDKException {
    return (T) requestPut(id, object);
  }
}
