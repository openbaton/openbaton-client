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

import org.openbaton.catalogue.security.Key;
import org.openbaton.sdk.api.annotations.Help;
import org.openbaton.sdk.api.exception.SDKException;
import org.openbaton.sdk.api.util.AbstractRestAgent;

/**
 * OpenBaton key-related api requester.
 */
public class KeyAgent extends AbstractRestAgent<Key> {

  public KeyAgent(
      String username,
      String password,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String path,
      String version) {
    super(username, password, projectId, sslEnabled, nfvoIp, nfvoPort, path, version, Key.class);
  }

  /**
   * Use the generateKey method instead.
   *
   * @param key
   * @return null
   */
  @Override
  @Deprecated
  public Key create(Key key) {
    return null;
  }

  /**
   * Generate a new Key in the NFVO.
   *
   * @param name the name of the new Key
   * @return the private Key
   */
  @Help(help = "Generate a new Key in the NFVO")
  public String generateKey(String name) throws SDKException {
    return (String) requestPost("generate", name);
  }

  /**
   * Import a Key into the NFVO by providing name and public key.
   *
   * @param name
   * @param publicKey
   * @return the imported Key
   * @throws SDKException
   */
  @Help(help = "Import a Key into the NFVO by providing name and public key")
  public Key importKey(String name, String publicKey) throws SDKException {
    Key key = new Key();
    key.setName(name);
    key.setPublicKey(publicKey);
    return (Key) requestPost(key);
  }

  @Override
  @Deprecated
  public Key update(Key key, String id) {
    return null;
  }
}
