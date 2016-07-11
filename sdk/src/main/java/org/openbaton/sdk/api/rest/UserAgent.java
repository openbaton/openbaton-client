package org.openbaton.sdk.api.rest;

import org.openbaton.catalogue.security.User;
import org.openbaton.sdk.api.util.AbstractRestAgent;

public class UserAgent extends AbstractRestAgent<User> {

  public UserAgent(
      String username,
      String password,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String path,
      String version) {
    super(username, password, projectId, sslEnabled, nfvoIp, nfvoPort, path, version, User.class);
  }
}
