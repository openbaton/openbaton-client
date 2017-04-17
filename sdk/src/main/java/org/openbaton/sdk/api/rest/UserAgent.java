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

package org.openbaton.sdk.api.rest;

import java.util.HashMap;
import org.openbaton.catalogue.security.User;
import org.openbaton.sdk.api.annotations.Help;
import org.openbaton.sdk.api.exception.SDKException;
import org.openbaton.sdk.api.util.AbstractRestAgent;

/**
 * This class is a Rest Request Agent for sending requests regarding User objects to the NFVO API.
 */
public class UserAgent extends AbstractRestAgent<User> {

  /**
   * @param username the username used for sending requests
   * @param password the password used for sending requests
   * @param projectId the NFVO Project's ID that will be used in the requests to the NFVO
   * @param sslEnabled true if the NFVO uses SSL
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   */
  public UserAgent(
      String username,
      String password,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String version) {
    super(username, password, projectId, sslEnabled, nfvoIp, nfvoPort, version, User.class);
  }

  @Override
  @Deprecated
  public User findById(String id) {
    return null;
  }

  @Help(help = "Find a User by his name")
  public User findByName(String name) throws SDKException {
    return (User) requestGet(name, User.class);
  }

  @Help(help = "Change a user's password")
  public void changePassword(String oldPassword, String newPassword) throws SDKException {
    HashMap<String, String> requestBody = new HashMap<>();
    requestBody.put("old_pwd", oldPassword);
    requestBody.put("new_pwd", newPassword);

    requestPut("changepwd", requestBody);
  }
}
